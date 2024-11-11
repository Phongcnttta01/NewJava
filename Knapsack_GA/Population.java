package Knapsack_GA;
import java.util.Random;
import java.util.Scanner;

public class Population {
    public int capacity;
    public int numofgens;
    public int[] weights ;
    public int[] values;
    public int[][] population;
    public int populationsize;

    public static final double MUTATION_RATE = 0.1;

    public static Random rand = new Random();



    public void input(){
        Scanner sc = new Scanner(System.in);
        System.out.print("Mời nhập số lượng đồ vật : ");
        this.numofgens = sc.nextInt();
        System.out.print("Mời giới hạn của cái túi : ");
        this.capacity = sc.nextInt();
        weights = new int[numofgens];
        values = new int[numofgens];
        System.out.println("Mời nhập khối lượng và giá trị của từng đồ vật : ");
        for (int i =0;i<numofgens;++i){
            weights[i] = sc.nextInt();
            values[i] = sc.nextInt();
        }
        System.out.print("Mời số cá thể trong quần thể : ");
        this.populationsize = sc.nextInt();
    }

    public  int[][]  initializePopulation() {
        int[][] population = new int[populationsize][numofgens];
        for (int i = 0; i < populationsize; i++) {
            for (int j = 0; j < numofgens; j++) {
                population[i][j] = rand.nextInt(2);  // Giá trị 0 hoặc 1
            }
        }
        return population;
    }

    public  int fitness(int[] individual,int[] weights, int[] values,int capacity){
        int totalValue = 0, totalWeight = 0;
        for (int i = 0; i < individual.length; i++) {
            if (individual[i] == 1) {
                totalWeight += weights[i];
                totalValue += values[i];
            }
        }
        return (totalWeight <= capacity) ? totalValue : 0;
    }




}
