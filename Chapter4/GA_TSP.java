package Chapter4;

public class GA_TSP {
    public static int maxGenerations = 3000;
    public static void main(String[] args) {
        int numCities = 8;
        City cities[] = new City[numCities];
// Tạo thành phố ngẫu nhiên
        for (int cityIndex = 0; cityIndex < numCities; cityIndex++) {
            int xPos = (int) (100 * Math.random());
            int yPos = (int) (100 * Math.random());
            cities[cityIndex] = new City(xPos, yPos);
        }
// Tạo 1 quy trình tiến hóa
        GeneticAlgorithm ga = new GeneticAlgorithm(100, 0.001, 0.9, 2, 5);
// Tạo quần thể mới
        Population population = ga.initPopulation(cities.length);
// Đánh giá quần thể
        ga.evalPopulation(population, cities);
// Theo dõi thế hệ hiện tại
        int generation = 1;
// Bắt đầu tiến hóa
        while (ga.isTerminationConditionMet(generation, maxGenerations) == false) {
// In ra cá thể có mức đánh giá
            Route route = new Route(population.getFittest(0), cities);
           // System.out.println("G"+generation+" Best distance: " + route.getDistance());
// TODO: Chạy lại tạo
            population = ga.crossoverPopulation(population);
// TODO: Chạy đột biến
            population = ga.mutatePopulation(population);
// Đánh giá quần thể
            ga.evalPopulation(population, cities);
            // Tăng thế hệ
            generation++;
        }
// Kết quả
        System.out.println("Dừng lại sau " + maxGenerations + " thế hệ.");
                Route route = new Route(population.getFittest(0), cities);
        System.out.println("Giá trị tối ưu : " + route.getDistance());
        for(int i = 0;i<numCities;++i)
            System.out.print(population.getIndividual(population.size()-1).getGene(i)+" --> ");
        System.out.print(population.getIndividual(population.size()-1).getGene(0));
    }

}
