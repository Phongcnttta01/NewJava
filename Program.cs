using System;
using System.Collections.Generic;

namespace ManyTSPD_GA
{
    class Program
    {
        static void Main(string[] args)
{
    Console.InputEncoding = System.Text.Encoding.UTF8;
    Console.OutputEncoding = System.Text.Encoding.UTF8;
    Random random = new Random();
    List<City> cities = new List<City>();

    // Depot (City 0) with 0 visits
    cities.Add(new City(0, 0, 0, 0));

    // Predefined list of 20 cities with coordinates and 3 visits each
    double[,] cityData = new double[,]
    {
        {10.5, 20.7}, {30.2, 15.4}, {25.8, 35.1}, {45.3, 10.9}, {15.6, 40.2},
        {50.1, 25.3}, {22.4, 18.7}, {33.7, 45.6}, {12.9, 30.4}, {28.5, 22.1},
        {40.8, 38.2}, {18.3, 12.5}, {36.4, 28.9}, {27.1, 50.3}, {14.2, 33.8},
        {48.6, 17.4}, {20.9, 42.7}, {31.5, 23.6}, {16.7, 19.8}, {43.2, 36.5}
    };

    for (int i = 0; i < 20; i++)
    {
        cities.Add(new City(i + 1, cityData[i, 0], cityData[i, 1], 3)); // Each city requires 3 visits
    }

    Console.WriteLine("Cities:");
    foreach (var city in cities)
    {
        Console.WriteLine($"City {city.Id}: ({city.X:F2}, {city.Y:F2}), Visits Required: {city.VisitCount}");
    }

    int numTrucks = 3;

    GeneticAlgorithm ga = new GeneticAlgorithm(cities, numTrucks);
    Individual bestSolution = ga.Run();

    if (bestSolution != null)
    {
        Console.WriteLine("\nBest Solution:");
        Console.WriteLine("City Sequence: " + string.Join(" ", bestSolution.CitySequence));
        
        Console.WriteLine($"Fitness: {bestSolution.Fitness:F6}, Total Distance: {1 / bestSolution.Fitness:F2}");

        // Kiểm tra số lần thăm
        var visitCounts = new Dictionary<int, int>();
        foreach (var cityId in bestSolution.CitySequence)
        {
            if (!visitCounts.ContainsKey(cityId)) visitCounts[cityId] = 0;
            visitCounts[cityId]++;
        }
        foreach (var truck in bestSolution.Trucks)
        {
            Console.WriteLine($"Truck {truck.Id} Route: {string.Join(" -> ", truck.Route)}");
            foreach (var drone in truck.Drones)
            {
                Console.WriteLine($"\tDrone {drone.Id} Route: {(drone.Route.Count > 0 ? string.Join(" -> ", drone.Route) : "No route assigned")}");
            }
        }
    }
    else
    {
        Console.WriteLine("\nNo valid solution found.");
    }
}
    }
}