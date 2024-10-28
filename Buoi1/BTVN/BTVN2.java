import java.util.Scanner;

public class BTVN2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Nhập số nguyên: ");
        int number = scanner.nextInt();

        int product = 1;
        int currentDigit;

        while (number != 0) {
            currentDigit = number % 10;
            product *= currentDigit;
            number /= 10;
        }

        System.out.println("Tích của các chữ số: " + product);
    }
}

