package Buoi1.BTVN1;

import java.util.Scanner;

public class BTVN3 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);


        System.out.println("Nhập 5 số :");
        int[] numbers = new int[5];
        for (int i = 0; i < 5; i++) {
            numbers[i] = scanner.nextInt();
        }


        int max1 = numbers[0];
        int max2 = numbers[0];

        for (int i = 1; i < 5; i++) {
            if (numbers[i] > max1) {
                max2 = max1;
                max1 = numbers[i];
            } else if (numbers[i] > max2) {
                max2 = numbers[i];
            }
        }


        System.out.println("2 số lớn nhất là : " + max1 + " " + max2);
    }
}

