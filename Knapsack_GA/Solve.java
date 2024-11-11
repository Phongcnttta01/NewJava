package Knapsack_GA;

import java.util.Scanner;
import java.util.Random;

public class Solve {
    public static Random rand = new Random();
    private static final int MAX_GENERATIONS = 100;
    public static void main(String[] args) {
        GA ga = new GA();
        ga.input();
        ga.population= ga.initializePopulation();
        int generation =0;
        while (generation < MAX_GENERATIONS) {
            int[][] newPopulation = new int[ga.populationsize][ga.numofgens];

            for (int i = 0; i < ga.populationsize; i++) {
                int[] parent1 = ga.selectParent(ga.population);
                int[] parent2 = ga.selectParent(ga.population);
                while (parent2 == parent1){
                    parent2 = ga.selectParent(ga.population);
                }
                int[] offspring = ga.crossover(parent1, parent2);
                ga.mutation(offspring);
                newPopulation[i] = offspring;
            }

            ga.population = newPopulation;
            generation++;
        }
        int[] bestIndividual = ga.findBestIndividual(ga.population);
        System.out.println("Cá thể tốt nhất:");
        for (int i =0;i<ga.numofgens;++i) {
            if(bestIndividual[i] == 1)
            System.out.print((i+1) + " ");
        }
        System.out.println("\nGiá trị tối đa: " + ga.fitness(bestIndividual, ga.weights, ga.values, ga.capacity));

    }
}
