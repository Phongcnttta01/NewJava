package ACO;

import java.util.*;

class AntColonyOptimization {

    static int NUM_CITIES; // Số thành phố
    static final int NUM_ANTS = 10; // Số kiến
    final static int MAX_ITERATIONS = 100;
    static final double ALPHA = 1.5; // Trọng số pheromone
    static final double BETA = 2.0; // Trọng số heuristic
    static final double EVAPORATION = 0.5; // Tỷ lệ bay hơi pheromone
    static final double Q = 10000.0; // Hằng số pheromone
    static double[][] distances; // Ma trận khoảng cách giữa các thành phố
    static double[][] pheromones; // Ma trận pheromone

    public static void nhap(){
        Scanner sc = new Scanner(System.in);
        System.out.print("Mời nhập số thành phố: ");
        NUM_CITIES = sc.nextInt(); // Nhập số lượng thành phố
        distances = new double[NUM_CITIES][NUM_CITIES];
        pheromones = new double[NUM_CITIES][NUM_CITIES];

        // Nhập khoảng cách giữa các thành phố
        System.out.println("Mời nhập khoảng cách giữa các thành phố:");
        for(int i = 0; i < NUM_CITIES; ++i) {
            for (int j = 0; j < NUM_CITIES; ++j) {

                    distances[i][j] = sc.nextDouble();
                    distances[j][i] = distances[i][j]; // Khoảng cách giữa thành phố i và j là đối xứng

            }
        }

        // Khởi tạo pheromone
        for (int i = 0; i < NUM_CITIES; i++) {
            Arrays.fill(pheromones[i], 1.0); // Khởi tạo pheromone với giá trị 1.0
        }
    }

    public static void main(String[] args) {
        nhap(); // Nhập số thành phố và khoảng cách
        int m = 20;
        while(m!=0) {
            double bestLength = Double.MAX_VALUE;
            List<Integer> bestTour = null;

            for (int iteration = 0; iteration < MAX_ITERATIONS; iteration++) {
                List<Ant> ants = new ArrayList<>();
                for (int i = 0; i < NUM_ANTS; i++) {
                    ants.add(new Ant());
                }

                for (Ant ant : ants) {
                    ant.findTour();
                    double tourLength = ant.getTourLength();
                    if (tourLength < bestLength) {
                        bestLength = tourLength;
                        bestTour = new ArrayList<>(ant.tour);
                    }
                }

                updatePheromones(ants);
            }

            System.out.println("Best tour: " + bestTour);
            System.out.println("Best length: " + bestLength);
            --m;
        }

    }

    static void updatePheromones(List<Ant> ants) {
        for (int i = 0; i < NUM_CITIES; i++) {
            for (int j = 0; j < NUM_CITIES; j++) {
                pheromones[i][j] *= (1 - EVAPORATION); // Bay hơi pheromone
            }
        }

        for (Ant ant : ants) {
            double contribution = Q / ant.getTourLength();
            for (int i = 0; i < ant.tour.size() - 1; i++) {
                int city1 = ant.tour.get(i);
                int city2 = ant.tour.get(i + 1);
                pheromones[city1][city2] += contribution;
                pheromones[city2][city1] += contribution;
            }
        }
    }

    static class Ant {
        List<Integer> tour = new ArrayList<>();
        boolean[] visited = new boolean[NUM_CITIES];

        void findTour() {
            int currentCity = new Random().nextInt(NUM_CITIES);
            tour.add(currentCity);
            visited[currentCity] = true;

            for (int i = 1; i <= NUM_CITIES; i++) {
                int nextCity = selectNextCity(currentCity);
                tour.add(nextCity);
                visited[nextCity] = true;
                currentCity = nextCity;
            }
            tour.add(tour.get(0)); // Quay lại thành phố ban đầu
        }

        int selectNextCity(int currentCity) {
            double[] probabilities = new double[NUM_CITIES];
            double total = 0.0;

            for (int i = 0; i < NUM_CITIES; i++) {
                if (!visited[i]) {
                    probabilities[i] = Math.pow(pheromones[currentCity][i], ALPHA)
                            * Math.pow(1.0 / distances[currentCity][i], BETA);
                    total += probabilities[i];
                }
            }
            for (int i = 0; i < NUM_CITIES; i++) {
                if (!visited[i]) {
                    probabilities[i] /= total;
                }
            }
            int bestNode = currentCity;
            double maxProbability = -1.0;
            for(int i =0;i<NUM_CITIES;++i)
            {
                if (!visited[i]){
                    if(probabilities[i] > maxProbability)
                    {
                        maxProbability = probabilities[i];
                        bestNode =i;
                    }
                }
            }
            return bestNode;
//            double random = new Random().nextDouble();
//            for (int i = 0; i < NUM_CITIES; i++) {
//                if (!visited[i]) {
//                    random -= probabilities[i];
//                    if (random <= 0) {
//                        return i;
//                    }
//                }
//            }
//            return currentCity; // Không hợp lệ
        }

        double getTourLength() {
            double length = 0.0;
            for (int i = 0; i < tour.size() - 1; i++) {
                int city1 = tour.get(i);
                int city2 = tour.get(i + 1);
                length += distances[city1][city2];
            }
            return length;
        }
    }
}
