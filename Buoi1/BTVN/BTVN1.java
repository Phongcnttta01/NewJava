import java.util.Scanner;

public class BTVN1 {

    public static String convertToBase(int n, int b) {
        String digits = "0123456789ABCDEF";
        StringBuilder result = new StringBuilder();

        if (n == 0) {
            return "0";
        }

        while (n > 0) {
            int remainder = n % b;
            result.insert(0, digits.charAt(remainder));
            n /= b;
        }

        return result.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nhap so tu nhien n: ");
        int n = scanner.nextInt();
        System.out.print("Nhap co so b (2 <= b <= 16): ");
        int b = scanner.nextInt();


        if (b < 2 || b > 16) {
            System.out.println("Co so khong hop le. Vui long nhap co so tu 2 den 16.");
        } else {

            String result = convertToBase(n, b);
            System.out.println("So " + n + " trong he co so " + b + " la: " + result);
        }

    }
}

