public class FullTimeEmployee extends Employee implements BonusProvider {


protected double monthlySalary;


    public FullTimeEmployee() {
    super();
    this.monthlySalary = 0.0;
}

    public FullTimeEmployee(String name, String employeeID, double monthlySalary) {
    super(name, employeeID);
    this.monthlySalary = monthlySalary;
}

    public double getMonthlySalary() {
        return monthlySalary;
    }

    public void setMonthlySalary(double monthlySalary) {
        this.monthlySalary = monthlySalary;
    }
    @Override
    public double calculateBonus() {

        return monthlySalary*0.1;
    }

    @Override
    public double calculateSalaty() {
 
    return monthlySalary;
    }


    

    
}
