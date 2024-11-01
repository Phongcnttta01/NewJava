
import java.util.Scanner;

    public class Bai1 {

        public static final String[] units = {
                "", "một", "hai", "ba", "bốn", "năm", "sáu", "bảy", "tám", "chín"
        };
        public static final String[] teens = {
                "mười", "mười một", "mười hai", "mười ba", "mười bốn", "mười lăm",
                "mười sáu", "mười bảy", "mười tám", "mười chín"
        };
        public static final String[] tens = {
                "", "", "hai mươi", "ba mươi", "bốn mươi", "năm mươi",
                "sáu mươi", "bảy mươi", "tám mươi", "chín mươi"
        };


        public static String convertToWords(int number) {
            if (number == 0) {
                return "không";
            } else if (number < 10) {
                return units[number];
            } else if (number < 20) {
                return teens[number - 10];
            } else if (number < 100) {
                return tens[number / 10] + (number % 10 != 0 ? " " + units[number % 10] : "");
            } else {
                String result = units[number / 100] + " trăm";
                int remainder = number % 100;

                if (remainder > 0) {
                    if (remainder < 10) {
                        result += " lẻ " + units[remainder];
                    } else if (remainder < 20) {
                        result += " " + teens[remainder - 10];
                    } else {
                        result += " " + tens[remainder / 10] + (remainder % 10 != 0 ? " " + units[remainder % 10] : "");
                    }
                }
                return result;
            }
        }
        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Nhập vào một số nhỏ hơn 1000: ");
            int number = scanner.nextInt();

            if (number < 0 || number >= 1000) {
                System.out.println("Vui lòng nhập số trong khoảng từ 0 đến 999.");
            } else {
                System.out.println("Kết quả: " + convertToWords(number));
            }
        }
    }


