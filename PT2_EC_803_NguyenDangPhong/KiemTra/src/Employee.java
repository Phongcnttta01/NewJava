import java.util.Scanner;

public abstract class Employee {
    protected String name;
    protected String employeeID;

    public Employee() {
        this.name = "";
        this.employeeID = "";
    }
     public Employee(String name, String employeeID) {
        this.name = name;
        this.employeeID = employeeID;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmployeeID() {
        return employeeID;
    }
    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public abstract double calculateSalaty();

    public void inputData(){
        try{
            Scanner sc= new Scanner(System.in);
            System.out.print("Moi nhap ten : ");
            this.name = sc.nextLine();
            System.out.print("Moi nhap ma cong nhan : ");
            this.employeeID = sc.nextLine();
        }catch(Exception e){
            System.out.println("Loi la "+e.getMessage());
        }
    }

    public void displayInfo(){
        System.out.println("Ten la : "+name);
        System.out.println("Ma cong nhan la : "+employeeID);
    }
    
}
