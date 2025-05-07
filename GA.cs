using System;
using System.Collections.Generic;
using System.Diagnostics.CodeAnalysis;
using System.Linq;

namespace ManyTSPD_GA
{
    public class City
    {
        public int Id { get; set; }
        public double X { get; set; }
        public double Y { get; set; }
        public int VisitCount { get; set; }

        public City(int id, double x, double y, int visitCount = 1)
        {
            Id = id;
            X = x;
            Y = y;
            VisitCount = visitCount;
        }

        public double DistanceTo(City other)
        {
            return Math.Sqrt(Math.Pow(X - other.X, 2) + Math.Pow(Y - other.Y, 2));
        }
    }

    public class Drone
    {
        public int Id { get; set; }
        public List<int> Route { get; set; }

        public Drone(int id)
        {
            Id = id;
            Route = new List<int>();
        }
    }

    public class Truck
    {
        public int Id { get; set; }
        public List<int> Route { get; set; }
        public List<Drone> Drones { get; set; }

        public Truck(int id, int numDrones)
        {
            Id = id;
            Route = new List<int>();
            Drones = new List<Drone>();
            for (int i = 0; i < numDrones; i++)
            {
                Drones.Add(new Drone(i));
            }
        }
    }

    public class Individual
    {
        public List<int> CitySequence { get; set; }
        public List<Truck> Trucks { get; set; }
        public double Fitness { get; set; }
        private readonly int SequenceLength;
        private readonly double[,] DistanceCache;

        public Individual(int numTrucks, int minDronesPerTruck, List<City> cities, Random rand, int sequenceLength)
        {
            SequenceLength = sequenceLength;
            DistanceCache = BuildDistanceCache(cities);
            Trucks = new List<Truck>(numTrucks);
            for (int i = 0; i < numTrucks; i++)
            {
                int numDrones = rand.Next(minDronesPerTruck, minDronesPerTruck + 3);
                Trucks.Add(new Truck(i, numDrones));
            }
            CitySequence = GenerateValidCitySequence(cities, rand);
            AssignRoutes(cities, rand);
            Fitness = CalculateFitness(cities);
        }

        public Individual(Individual other, Random rand, List<City> cities, int sequenceLength)
        {
            SequenceLength = sequenceLength;
            DistanceCache = BuildDistanceCache(cities);
            Trucks = new List<Truck>(other.Trucks.Count);
            foreach (var truck in other.Trucks)
            {
                Trucks.Add(new Truck(truck.Id, truck.Drones.Count));
            }
            CitySequence = new List<int>(other.CitySequence);
            AssignRoutes(cities, rand);
            Fitness = CalculateFitness(cities);
        }

        private static double[,] BuildDistanceCache(List<City> cities)
        {
            int n = cities.Count;
            var cache = new double[n, n];
            for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                cache[i, j] = cities[i].DistanceTo(cities[j]);
            return cache;
        }

        private List<int> GenerateValidCitySequence(List<City> cities, Random rand)
        {
            var sequence = new List<int>(SequenceLength);
            var availableCities = cities.Where(c => c.Id != 0 && c.VisitCount > 0).Select(c => c.Id).ToList();
            var visitCounts = cities.ToDictionary(c => c.Id, c => c.VisitCount);
            var cityPool = new List<int>(SequenceLength);

            foreach (var city in availableCities)
            {
                for (int i = 0; i < visitCounts[city]; i++)
                    cityPool.Add(city);
            }

            while (cityPool.Count < SequenceLength && availableCities.Count > 0)
                cityPool.Add(availableCities[rand.Next(availableCities.Count)]);

            cityPool = cityPool.OrderBy(_ => rand.Next()).ToList();

            for (int i = 0; i < SequenceLength; i++)
            {
                int lastCity = sequence.Count > 0 ? sequence[sequence.Count - 1] : -1;
                var validCities = cityPool.Where(c => c != lastCity).ToList();
                if (validCities.Count == 0)
                {
                    validCities = availableCities.Where(c => c != lastCity).ToList();
                    if (validCities.Count == 0) validCities = availableCities;
                }

                int nextCity = validCities[rand.Next(validCities.Count)];
                sequence.Add(nextCity);
                cityPool.Remove(nextCity);
            }

            return sequence;
        }

        private bool IsValidRoute(List<int> route)
        {
            if (route.Count < 2) return false;
            for (int i = 1; i < route.Count; i++)
            {
                if (route[i] == route[i - 1])
                    return false;
            }
            return true;
        }

        private bool CheckVisitCounts(List<int> sequence, List<City> cities)
        {
            var visitCounts = new Dictionary<int, int>();
            foreach (var cityId in sequence)
            {
                visitCounts[cityId] = visitCounts.GetValueOrDefault(cityId) + 1;
            }

            foreach (var city in cities.Where(c => c.Id != 0))
            {
                if (visitCounts.GetValueOrDefault(city.Id) < city.VisitCount)
                    return false;
            }
            return true;
        }

        private double CalculateTravelTime(List<int> route, bool isDrone = false)
        {
            double totalDistance = 0;
            for (int i = 0; i < route.Count - 1; i++)
            {
                double distance = DistanceCache[route[i], route[i + 1]];
                totalDistance += isDrone ? distance * 0.5 : distance;
            }
            return totalDistance;
        }

        private List<int> CreateInitialRoute(List<int> citiesForTruck, List<City> cities, Random rand)
        {
            var route = new List<int> { 0 };
            var remainingCities = new List<int>(citiesForTruck);
            int currentCity = 0;

            while (remainingCities.Count > 0)
            {
                var validCities = remainingCities.Where(c => c != currentCity).ToList();
                if (validCities.Count == 0) break;

                int closestCity = -1;
                double minDistance = double.MaxValue;
                int closestIndex = -1;

                for (int j = 0; j < validCities.Count; j++)
                {
                    double distance = DistanceCache[currentCity, validCities[j]];
                    if (distance < minDistance)
                    {
                        minDistance = distance;
                        closestCity = validCities[j];
                        closestIndex = remainingCities.IndexOf(validCities[j]);
                    }
                }

                if (closestCity != -1)
                {
                    route.Add(closestCity);
                    remainingCities.RemoveAt(closestIndex);
                    currentCity = closestCity;
                }
                else
                {
                    break;
                }
            }

            route.Add(0);
            return route;
        }

        private List<int> SwapNeighborhood(List<int> route, Random rand)
        {
            var newRoute = new List<int>(route);
            int startIndex = 1;
            int endIndex = newRoute.Count - 2;
            if (endIndex - startIndex < 1) return newRoute;

            int maxAttempts = 5;
            for (int attempt = 0; attempt < maxAttempts; attempt++)
            {
                int i = rand.Next(startIndex, endIndex + 1);
                int j = rand.Next(startIndex, endIndex + 1);
                while (i == j) j = rand.Next(startIndex, endIndex + 1);

                (newRoute[i], newRoute[j]) = (newRoute[j], newRoute[i]);
                if (IsValidRoute(newRoute))
                    return newRoute;
                (newRoute[i], newRoute[j]) = (newRoute[j], newRoute[i]);
            }

            return newRoute;
        }

        private List<int> ReverseNeighborhood(List<int> route, Random rand)
        {
            var newRoute = new List<int>(route);
            int startIndex = 1;
            int endIndex = newRoute.Count - 2;
            if (endIndex - startIndex < 1) return newRoute;

            int maxAttempts = 5;
            for (int attempt = 0; attempt < maxAttempts; attempt++)
            {
                int i = rand.Next(startIndex, endIndex + 1);
                int j = rand.Next(startIndex, endIndex + 1);
                if (i > j) (i, j) = (j, i);

                var tempRoute = new List<int>(newRoute);
                while (i < j)
                {
                    (tempRoute[i], tempRoute[j]) = (tempRoute[j], tempRoute[i]);
                    i++;
                    j--;
                }
                if (IsValidRoute(tempRoute))
                    return tempRoute;
            }

            return newRoute;
        }

        private List<int> RelocateNeighborhood(List<int> route, Random rand)
        {
            var newRoute = new List<int>(route);
            int startIndex = 1;
            int endIndex = newRoute.Count - 2;
            if (endIndex - startIndex < 1) return newRoute;

            int maxAttempts = 5;
            for (int attempt = 0; attempt < maxAttempts; attempt++)
            {
                int i = rand.Next(startIndex, endIndex + 1);
                int j = rand.Next(startIndex, endIndex + 1);
                while (i == j) j = rand.Next(startIndex, endIndex + 1);

                int city = newRoute[i];
                newRoute.RemoveAt(i);
                if (j > i) j--;
                newRoute.Insert(j, city);
                if (IsValidRoute(newRoute))
                    return newRoute;
                newRoute.RemoveAt(j);
                newRoute.Insert(i, city);
            }

            return newRoute;
        }

        private List<int> ApplyVNS(List<int> initialRoute, List<City> cities, Random rand)
        {
            List<int> bestRoute = new List<int>(initialRoute);
            double bestDistance = CalculateTravelTime(bestRoute);
            int maxIterations = 5;
            int kMax = 3;

            for (int iter = 0; iter < maxIterations; iter++)
            {
                int k = 1;
                while (k <= kMax)
                {
                    List<int> newRoute;
                    if (k == 1)
                        newRoute = SwapNeighborhood(bestRoute, rand);
                    else if (k == 2)
                        newRoute = ReverseNeighborhood(bestRoute, rand);
                    else
                        newRoute = RelocateNeighborhood(bestRoute, rand);

                    if (!IsValidRoute(newRoute))
                    {
                        k++;
                        continue;
                    }

                    double newDistance = CalculateTravelTime(newRoute);
                    if (newDistance < bestDistance)
                    {
                        bestRoute = newRoute;
                        bestDistance = newDistance;
                        k = 1;
                    }
                    else
                    {
                        k++;
                    }
                }
            }

            return bestRoute;
        }

        [SuppressMessage("ReSharper.DPA", "DPA0002: Excessive memory allocations in SOH")]
        private void AssignRoutesUsingVNS(List<City> cities, Random rand)
        {
            var availableCities = new List<int>(CitySequence);
            int citiesPerTruck = availableCities.Count / Math.Max(1, Trucks.Count);
            int currentIndex = 0;

            foreach (var truck in Trucks)
            {
                truck.Route = new List<int> { 0 };
                int citiesToAssign = Math.Min(citiesPerTruck, availableCities.Count - currentIndex);
                if (citiesToAssign <= 0) continue;

                var citiesForTruck = availableCities.Skip(currentIndex).Take(citiesToAssign).ToList();
                currentIndex += citiesToAssign;

                var initialRoute = CreateInitialRoute(citiesForTruck, cities, rand);
                truck.Route = ApplyVNS(initialRoute, cities, rand);
            }

            while (currentIndex < availableCities.Count)
            {
                var truck = Trucks[rand.Next(Trucks.Count)];
                int city = availableCities[currentIndex];
                var tempRoute = new List<int>(truck.Route);
                int insertPos = tempRoute.Count - 1;
                tempRoute.Insert(insertPos, city);
                if (IsValidRoute(tempRoute))
                {
                    truck.Route = tempRoute;
                }
                currentIndex++;
            }
        }

        public void AssignRoutes(List<City> cities, Random rand)
        {
            foreach (var truck in Trucks)
            {
                truck.Route.Clear();
                foreach (var drone in truck.Drones)
                {
                    drone.Route.Clear();
                }
            }
            AssignRoutesUsingVNS(cities, rand);

            foreach (var truck in Trucks)
            {
                // Step 1: Select ~40% of truck's cities (excluding depot) for drones
                var truckCities = truck.Route.Where(c => c != 0).ToList();
                if (truckCities.Count == 0) continue;

                int numCitiesForDrones = (int)Math.Ceiling(truckCities.Count * 0.4);
                var droneCities = new List<int>();
                var remainingTruckCities = new List<int>(truckCities);

                // Randomly select cities for drones
                while (droneCities.Count < numCitiesForDrones && remainingTruckCities.Count > 0)
                {
                    int index = rand.Next(remainingTruckCities.Count);
                    droneCities.Add(remainingTruckCities[index]);
                    remainingTruckCities.RemoveAt(index);
                }

                // Step 2: Rebuild truck route with remaining cities, ensuring no adjacent duplicates
                var newTruckRoute = new List<int> { 0 };
                foreach (var city in remainingTruckCities)
                {
                    int lastCity = newTruckRoute[newTruckRoute.Count - 1];
                    if (city != lastCity)
                    {
                        newTruckRoute.Add(city);
                    }
                }
                newTruckRoute.Add(0);
                if (IsValidRoute(newTruckRoute))
                {
                    truck.Route = newTruckRoute;
                }
                else
                {
                    // Fallback to original route if new route is invalid
                    truck.Route = ApplyVNS(truck.Route, cities, rand);
                }

                // Step 3: Assign routes to drones, allowing multiple drones to use the same start city
                if (droneCities.Count == 0 || truck.Route.Count < 3 || truck.Drones.Count == 0) continue;

                // Shuffle drones to ensure fair distribution
                var shuffledDrones = truck.Drones.OrderBy(_ => rand.Next()).ToList();
                int droneIndex = 0;

                // Iterate through truck route to assign start cities
                for (int truckIndex = 1; truckIndex < truck.Route.Count - 1 && droneCities.Count > 0; truckIndex++)
                {
                    int startCity = truck.Route[truckIndex];
                    // Try to assign a route to the current drone
                    var currentDrone = shuffledDrones[droneIndex % shuffledDrones.Count];

                    // Select a new city from droneCities
                    int newCityIndex = rand.Next(droneCities.Count);
                    int newCity = droneCities[newCityIndex];

                    // Find a valid end city from truck's subsequent cities
                    int endCity = -1;
                    for (int j = truckIndex + 1; j < truck.Route.Count - 1; j++)
                    {
                        int candidateEndCity = truck.Route[j];
                        // Check if start->new->end has no adjacent duplicates
                        if (startCity != newCity && newCity != candidateEndCity)
                        {
                            endCity = candidateEndCity;
                            break;
                        }
                    }

                    if (endCity != -1)
                    {
                        // Add route: start -> new -> end
                        currentDrone.Route.Add(startCity);
                        currentDrone.Route.Add(newCity);
                        currentDrone.Route.Add(endCity);
                        // Remove used newCity from pool
                        droneCities.RemoveAt(newCityIndex);
                    }

                    // Move to next drone in round-robin fashion
                    droneIndex++;
                }
            }
        }

        public double CalculateFitness(List<City> cities)
        {
            if (!CheckVisitCounts(CitySequence, cities))
                return double.MinValue;

            // Check CitySequence for adjacent duplicates
            for (int i = 1; i < CitySequence.Count; i++)
            {
                if (CitySequence[i] == CitySequence[i - 1])
                    return double.MinValue;
            }

            double maxTravelTime = 0;
            foreach (var truck in Trucks)
            {
                // Validate truck route
                if (truck.Route.Count < 2 || truck.Route[0] != 0 || truck.Route[truck.Route.Count - 1] != 0)
                    return double.MinValue;
                if (!IsValidRoute(truck.Route))
                    return double.MinValue;

                // Calculate truck travel time
                double truckTime = CalculateTravelTime(truck.Route, false);
                maxTravelTime = Math.Max(maxTravelTime, truckTime);

                // Calculate drone travel times
                foreach (var drone in truck.Drones)
                {
                    if (drone.Route.Count < 3 || drone.Route.Count % 3 != 0)
                        continue; // Invalid drone route

                    double droneTime = CalculateTravelTime(drone.Route, true);
                    maxTravelTime = Math.Max(maxTravelTime, droneTime);
                }
            }

            Fitness = maxTravelTime == 0 ? double.MaxValue : 1 / maxTravelTime;
            return Fitness;
        }
    }

    public class GeneticAlgorithm
    {
        private readonly List<City> Cities;
        private List<Individual> Population;
        private readonly Random Rand;
        private readonly int PopulationSize = 50;
        private readonly int NumGenerations = 100;
        private readonly double CrossoverRate = 0.85;
        private readonly double MutationRate = 0.15;
        private readonly int NumTrucks;
        private readonly int MinDronesPerTruck = 2;
        private readonly List<double> totalDistances = new List<double>();
        private readonly int SequenceLength;

        public GeneticAlgorithm(List<City> cities, int numTrucks)
        {
            Cities = cities;
            NumTrucks = numTrucks;
            Rand = new Random();
            Population = new List<Individual>();
            SequenceLength = Math.Max(cities.Where(c => c.Id != 0).Sum(c => c.VisitCount), cities.Count * 2);
        }

        private void InitializePopulation()
        {
            while (Population.Count < PopulationSize)
            {
                var individual = new Individual(NumTrucks, MinDronesPerTruck, Cities, Rand, SequenceLength);
                if (individual.Fitness != double.MinValue)
                    Population.Add(individual);
            }
            double initialTotalDistance = Population.Average(i => i.Fitness == double.MaxValue ? 0 : 1 / i.Fitness);
            totalDistances.Add(initialTotalDistance);
            Console.WriteLine($"Khởi tạo: Trung bình quãng đường = {initialTotalDistance:F2}");
        }

        private Individual TournamentSelection()
        {
            var tournament = new List<Individual>(5);
            for (int i = 0; i < 5; i++)
                tournament.Add(Population[Rand.Next(Population.Count)]);
            return tournament.OrderByDescending(ind => ind.Fitness).First();
        }

        private List<int> OrderCrossover(List<int> p1, List<int> p2, Random rand)
        {
            int length = p1.Count;
            int point1 = rand.Next(0, length - 1);
            int point2 = rand.Next(point1 + 1, length);

            var child = new List<int>(new int[length]);
            for (int i = 0; i < length; i++) child[i] = -1;

            var usedCities = new List<int>(length);
            for (int i = point1; i < point2; i++)
            {
                child[i] = p1[i];
                usedCities.Add(p1[i]);
            }

            int index = point2;
            for (int i = 0; i < length; i++)
            {
                if (!usedCities.Contains(p2[i]))
                {
                    while (index < length && child[index] != -1) index++;
                    if (index >= length) index = 0;
                    while (child[index] != -1) index++;
                    child[index] = p2[i];
                    usedCities.Add(p2[i]);
                }
            }

            var availableCities = Cities.Where(c => c.Id != 0).Select(c => c.Id).ToList();
            for (int i = 0; i < length; i++)
            {
                if (child[i] == -1)
                {
                    var validCities = availableCities.Where(c => (i == 0 || c != child[i - 1]) && (i + 1 >= length || c != child[i + 1])).ToList();
                    if (validCities.Count == 0) validCities = availableCities;
                    child[i] = validCities[rand.Next(validCities.Count)];
                }
                else if (i > 0 && child[i] == child[i - 1])
                {
                    var validCities = availableCities.Where(c => c != child[i - 1] && (i + 1 >= length || c != child[i + 1])).ToList();
                    if (validCities.Count == 0) validCities = availableCities;
                    child[i] = validCities[rand.Next(validCities.Count)];
                }
            }

            return child;
        }

        [SuppressMessage("ReSharper.DPA", "DPA0002: Excessive memory allocations in SOH", MessageId = "type: System.Int32[]")]
        private (Individual, Individual) Crossover(Individual p1, Individual p2)
        {
            if (Rand.NextDouble() > CrossoverRate)
                return (new Individual(p1, Rand, Cities, SequenceLength), new Individual(p2, Rand, Cities, SequenceLength));

            var c1 = new Individual(p1, Rand, Cities, SequenceLength);
            var c2 = new Individual(p2, Rand, Cities, SequenceLength);

            c1.CitySequence = OrderCrossover(p1.CitySequence, p2.CitySequence, Rand);
            c2.CitySequence = OrderCrossover(p2.CitySequence, p1.CitySequence, Rand);

            c1.AssignRoutes(Cities, Rand);
            c2.AssignRoutes(Cities, Rand);

            c1.CalculateFitness(Cities);
            c2.CalculateFitness(Cities);
            return (c1, c2);
        }

        [SuppressMessage("ReSharper.DPA", "DPA0002: Excessive memory allocations in SOH", MessageId = "type: System.Int32[]")]
        private void Mutate(Individual ind)
        {
            if (Rand.NextDouble() < MutationRate && ind.CitySequence.Count > 2)
            {
                int i = Rand.Next(ind.CitySequence.Count);
                var availableCities = Cities.Where(c => c.Id != 0)
                    .Select(c => c.Id)
                    .Where(c => (i == 0 || c != ind.CitySequence[i - 1]) && (i + 1 >= ind.CitySequence.Count || c != ind.CitySequence[i + 1]))
                    .ToList();
                if (availableCities.Count > 0)
                    ind.CitySequence[i] = availableCities[Rand.Next(availableCities.Count)];
            }

            ind.AssignRoutes(Cities, Rand);
            ind.CalculateFitness(Cities);
        }

        [SuppressMessage("ReSharper.DPA", "DPA0002: Excessive memory allocations in SOH")]
        public Individual Run()
        {
            InitializePopulation();
            var best = Population.OrderByDescending(i => i.Fitness).First();

            for (int gen = 0; gen < NumGenerations; gen++)
            {
                var newPop = new List<Individual>(PopulationSize) { best };

                while (newPop.Count < PopulationSize)
                {
                    var p1 = TournamentSelection();
                    var p2 = TournamentSelection();
                    var (c1, c2) = Crossover(p1, p2);
                    Mutate(c1);
                    Mutate(c2);
                    if (c1.Fitness != double.MinValue) newPop.Add(c1);
                    if (newPop.Count < PopulationSize && c2.Fitness != double.MinValue) newPop.Add(c2);
                }

                Population = newPop;
                best = Population.OrderByDescending(i => i.Fitness).First();
                double averageDistance = Population.Average(i => i.Fitness == double.MaxValue ? 0 : 1 / i.Fitness);
                totalDistances.Add(averageDistance);
                Console.WriteLine($"Thế hệ {gen + 1}: Fitness = {best.Fitness:F6}, Quãng đường = {(best.Fitness == double.MaxValue ? 0 : 1 / best.Fitness):F2}, Trung bình quãng đường = {averageDistance:F2}");
            }

            double overallAverageDistance = totalDistances.Average();
            Console.WriteLine($"Trung bình quãng đường qua tất cả các thế hệ: {overallAverageDistance:F2}");

            return best;
        }
    }
}