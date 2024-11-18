import java.util.Scanner;

import javax.xml.crypto.AlgorithmMethod;

public class Main {
    public static void main(String[] args) throws Exception {
       try{
         Scanner sc= new Scanner(System.in);
        int n = 0;
        while(n<2 || n >5){
        System.out.print("Moi nhap so luong cong nhan tu 2 den 5 : ");
         n = sc.nextInt();
         if(n<2 || n >5)
         System.out.println("Moi nhap lai !");
        }
        FullTimeEmployee[] full = new FullTimeEmployee[n];
        PartTimeEmployee[] part = new PartTimeEmployee[n];
        System.out.println("Cong nhan toan thoi gian : ");
        for(int i =0;i<n;++i){
            System.out.println("Moi nhap cong nhan "+ (i+1) + " : ");
            full[i] = new FullTimeEmployee();
            full[i].inputData();
            System.out.print("Moi nhap muc luong hang thang : ");
            double luong = sc.nextDouble();
            full[i].setMonthlySalary(luong);
            full[i].displayInfo();
            System.out.println("Luong cua cong nhan nay la : "+full[i].calculateSalaty());
             System.out.println("Luong thuong cua cong nhan nay la : "+full[i].calculateBonus());
        }
        double[] fullluong = new double[n];
        for(int i =0;i<n;++i){
            fullluong[i] = full[i].calculateSalaty()+full[i].calculateSalaty();
        }
        for(int i =0;i<n;++i){
           for(int j=i;j<n;++j)
           {
              if(fullluong[i]>fullluong[j]){
                double temp = fullluong[i];
                fullluong[i] = fullluong[j];
                fullluong[j] = temp;
              }
           }
        }
        for(int i =0;i<n;++i){
             for(int j=i;j<n;++j)
             {
                if(full[j].calculateSalaty()+full[j].calculateSalaty() == fullluong[i]){
                    FullTimeEmployee temp = full[i];
                full[i] = full[j];
                full[j] = temp;
                }
             }
        }
        System.out.println("Cac cong nhan sap xep theo muc luong tang dan : ");
        for(int i =0;i<n;++i){
            full[i].displayInfo();
            System.out.println("Luong va thuong cua cong nhan nay la : "+fullluong[i]);
        }
        System.out.println("Cong nhan ban thoi gian : ");
        for(int i =0;i<n;++i){
            System.out.println("Moi nhap cong nhan "+ (i+1) + " : ");
            part[i] = new PartTimeEmployee();
            part[i].inputData();
            System.out.print("Moi nhap muc luong hang gio : ");
            double luong = sc.nextDouble();
            part[i].setHourlyRate(luong);
            System.out.print("Tong gio lam la : ");
            int gio = sc.nextInt(i);
            part[i].setHoursWorked(gio);
            part[i].displayInfo();
           System.out.println("Luong cua cong nhan nay la : "+part[i].calculateSalaty());
           System.out.println("Luong thuong cua cong nhan nay la : "+part[i].calculateBonus());
        }
        double[] partluong = new double[n];
        for(int i =0;i<n;++i){
            partluong[i] = part[i].calculateSalaty()+part[i].calculateSalaty();
        }
        for(int i =0;i<n;++i){
           for(int j=i;j<n;++j)
           {
              if(partluong[i]>partluong[j]){
                double temp = partluong[i];
                partluong[i] = partluong[j];
                partluong[j] = temp;
              }
           }
        }
        for(int i =0;i<n;++i){
             for(int j=i;j<n;++j)
             {
                if(part[j].calculateSalaty()+part[j].calculateSalaty() == partluong[i]){
                    PartTimeEmployee temp = part[i];
                part[i] = part[j];
                part[j] = temp;
                }
             }
        }
        System.out.println("Cac cong nhan sap xep theo muc luong tang dan : ");
        for(int i =0;i<n;++i){
            part[i].displayInfo();
            System.out.println("Luong va thuong cua cong nhan nay la : "+partluong[i]);
        }
       }catch(Exception e){
        System.out.println("Loi la " + e.getMessage());
       }

    }
}
