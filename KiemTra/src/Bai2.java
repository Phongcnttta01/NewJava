import java.util.Scanner;

public class Bai2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nhập vào chuỗi: ");
        String input = scanner.nextLine();

        String chuoiChu = "";
        String chuoiSo = "";

        String chuHienTai = "";
        String soHienTai = "";

        for (char c : input.toCharArray()) {
            if (Character.isLetter(c)) {
                if (!soHienTai.isEmpty()) {

                    chuoiSo += soHienTai + " ";
                    soHienTai = "";
                }
                chuHienTai += c;
            } else if (Character.isDigit(c)) {
                if (!chuHienTai.isEmpty()) {

                    chuoiChu += chuHienTai + " ";
                    chuHienTai = "";
                }
                soHienTai += c;
            }
        }


        if (!chuHienTai.isEmpty()) {
            chuoiChu += chuHienTai + " ";
        }
        if (!soHienTai.isEmpty()) {
            chuoiSo += soHienTai + " ";
        }


        System.out.println("Chuỗi chữ: " + chuoiChu);
        System.out.println("Chuỗi số: " + chuoiSo);
    }
}
