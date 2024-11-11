package Knapsack_GA;

import java.util.Random;

public class GA extends Population {


    public static Random rand = new Random();

    public int[] selectParent(int[][] populaton) {
        int[] best = population[rand.nextInt(populationsize)];
        for (int i = 0; i < 3; i++) {  // Tournament với kích thước 3
            int[] contender = population[rand.nextInt(populationsize)];
            if (fitness(contender, weights, values, capacity) > fitness(best, weights, values, capacity)) {
                best = contender;
            }
        }
        return best;
    }

    public int[] crossover(int[] parent1, int[] parent2) {
        int[] test = new int[numofgens];
        double find = rand.nextDouble();
        for (int i = 0; i < numofgens; ++i) {
            if (find > 0.5) {
                test[i] = 1;
            } else {
                test[i] = 0;
            }
        }
        int[] offspring = new int[numofgens];
        for (int i = 0; i < numofgens; i++) {
            if (test[i] == 1) {
                offspring[i] = parent1[i];
            } else offspring[i] = parent2[i];
        }
        return offspring;
    }

    public void mutation(int[] individual) {
        for (int i = 0; i < individual.length; i++) {
            if (rand.nextDouble() < MUTATION_RATE) {
                individual[i] = 1 - individual[i];  // Đảo bit từ 0 thành 1 hoặc từ 1 thành 0
            }
        }
    }

    public int[] findBestIndividual(int[][] population){
        int[] bestIndividual = population[0];
        for (int i = 1; i < populationsize; i++) {
            if (fitness(population[i],weights,values,capacity) > fitness(bestIndividual,weights,values,capacity)) {
                bestIndividual = population[i];
            }
        }
        return bestIndividual;
    }

}
