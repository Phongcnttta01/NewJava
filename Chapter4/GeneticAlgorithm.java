package Chapter4;

import java.util.Arrays;

public class GeneticAlgorithm {
    private int populationSize;
    private double mutationRate;
    private double crossoverRate;
    private int elitismCount;
    private int tournamentSize; // Số cá thể mang đi lai tạo

    public GeneticAlgorithm(int populationSize, double mutationRate,
                            double crossoverRate, int elitismCount, int tournamentSize) {
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        this.elitismCount = elitismCount;
        this.tournamentSize = tournamentSize;
    }

    public Population initPopulation(int chromosomeLength) {
        Population population = new Population(this.populationSize, chromosomeLength);
        return population;
    }

    public boolean isTerminationConditionMet(int generation, int maxGenrations) {
        if (generation < maxGenrations) return false;
        return true;
    }

    public Individual selectParent(Population population) {
// Lấy cá thể
        Individual individuals[] = population.getIndividuals();
// Dùng bánh xe roulette
        double populationFitness = population.getPopulationFitness();
        double rouletteWheelPosition = Math.random() * populationFitness;
        // Tìm bố mẹ
        double spinWheel = 0;
        for (Individual individual : individuals) {
            spinWheel += individual.getFitness();
            if (spinWheel >= rouletteWheelPosition) {
                return individual;
            }
        }
        return individuals[population.size() - 1];
    }

    public double calcFitness(Individual individual, City cities[]) {
        // Lấy đánh giá
        Route route = new Route(individual, cities);
        double fitness = 1 / route.getDistance();

        // Lưu đánh giá
        individual.setFitness(fitness);
        return fitness;
    }

    public void evalPopulation(Population population, City cities[]) {
        double populationFitness = 0;
        // Lặp lại trên quần thể đánh giá các cá thể và tổng hợp độ thích nghi của quần thể
        for (Individual individual : population.getIndividuals()) {
            populationFitness += this.calcFitness(individual, cities);
        }
        double avgFitness = populationFitness / population.size();
        population.setPopulationFitness(avgFitness);
    }

    // Lai tạo bằng cách lấy 1 đoạn của bố mẹ 1 sau đó lấy những gen còn lại ơr bố mẹ 2
    public Population crossoverPopulation(Population population){
// Tạo quần thể mới
        Population newPopulation = new Population(population.size());
// Lặp lại dân số hiện tại theo mức đánh giá
        for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
// Lấy bố mẹ 1 là cá thể có mức đánh giá lớn nhất
            Individual parent1 = population.getFittest(populationIndex);
// Xem bố mẹ có hợp lệ để lại tạo không
            if (this.crossoverRate > Math.random() && populationIndex >= this.elitismCount) {
// Lấy bố mẹ 2 bằng cách chọn ngẫu nhiên
                Individual parent2 = this.selectParent(population);
                // Tạo 1 cá thể con trống
                int offspringChromosome[] = new int[parent1.getChromosomeLength()];
                Arrays.fill(offspringChromosome, -1);
                Individual offspring = new Individual(offspringChromosome);
// Lấy đoạn gen từ bố mẹ
                int substrPos1 = (int) (Math.random() * parent1.getChromosomeLength());
                int substrPos2 = (int) (Math.random() * parent1.getChromosomeLength());
// Lấy vị trí min để bắt đầu và kết thúc tại vị trí max
                final int startSubstr = Math.min(substrPos1, substrPos2);
                final int endSubstr = Math.max(substrPos1, substrPos2);
// Cho chạy vòng lặp để thêm các gen của bố mẹ 1
                for (int i = startSubstr; i < endSubstr; i++) {
                    offspring.setGene(i, parent1.getGene(i));
                }
// Thêm nốt của bố mẹ 2
                for (int i = 0; i < parent2.getChromosomeLength(); i++) {
                    int parent2Gene = i + endSubstr;
                    if (parent2Gene >= parent2.getChromosomeLength()) {
                        parent2Gene -= parent2.getChromosomeLength();
                    }
// Kiểm tra xem gen đã có trong cá thể mới chưa
                    if (offspring.containsGene(parent2.getGene(parent2Gene)) == false) {
// Vòng lặp để tìm một vị trí dự phòng trong cá thể mới
                        for (int ii = 0; ii < offspring.getChromosomeLength(); ii++) {
                            if (offspring.getGene(ii) == -1) {
                                offspring.setGene(ii, parent2.getGene(parent2Gene));
                                break;
                            }
                        }
                    }
                }
                // Thêm vào quần thể mới
                newPopulation.setIndividual(populationIndex, offspring);
            } else {
// Thêm cá thể vào quần thể mới mà không áp dụng phép lai
                newPopulation.setIndividual(populationIndex, parent1);
            }
        }
        return newPopulation;
    }


    // Đột biến bằng cách hoán đổi 2 gen của 1 cá thể
    public Population mutatePopulation(Population population){
// Tạo quần thể mới
        Population newPopulation = new Population(this.populationSize);
// Chạy vòng lặp theo mức đánh giá
        for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
            Individual individual = population.getFittest(populationIndex);
// Bỏ qua đột biến nếu đây là cá thể ưu tú
            if (populationIndex >= this.elitismCount) {
// System.out.println("Mutating population member"+populationIndex);
// Lặp từng gen của cá thể
            for (int geneIndex = 0; geneIndex < individual.getChromosomeLength(); geneIndex++) {
// Kiểm tra xem có cần đột biến không
                    if (this.mutationRate > Math.random()) {
// Lấy vị trí mới của gen
                        int newGenePos = (int) (Math.random() * individual.getChromosomeLength());
// Lấy gen để đổi vị trí
                        int gene1 = individual.getGene(newGenePos);
                        int gene2 = individual.getGene(geneIndex);
// Đổi gen
                        individual.setGene(geneIndex, gene1);
                        individual.setGene(newGenePos, gene2);
                    }
                }
            }
// Thêm cá thể mới vào quần thể
            newPopulation.setIndividual(populationIndex, individual);
        }
// Trả về quần thể
        return newPopulation;
    }

}