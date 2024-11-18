public class PartTimeEmployee  extends Employee implements BonusProvider{

    protected double hourlyRate;
    protected int hoursWorked;



    public PartTimeEmployee() {
        super();
        this.hourlyRate = 0.0;
        this.hoursWorked = 0;
    }

    public PartTimeEmployee(String name, String employeeID, double hourlyRate, int hoursWorked) {
        super(name, employeeID);
        this.hourlyRate = hourlyRate;
        this.hoursWorked = hoursWorked;
    }


    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public int getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(int hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

      @Override
    public double calculateBonus() {
        return hourlyRate*hoursWorked*0.05;
        
    }

    @Override
    public double calculateSalaty() {
        return hourlyRate*hoursWorked;
    }
    
}
