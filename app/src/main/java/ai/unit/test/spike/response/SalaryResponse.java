package ai.unit.test.spike.response;

public class SalaryResponse {
    private double baseSalary;
    private double overTimeSalary;
    private double senioritySalary;
    private double attendanceSalary;
    private double salesCommissionSalary;
    private double totalSalary;

    public double getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(final double baseSalary) {
        this.baseSalary = baseSalary;
    }

    public double getOverTimeSalary() {
        return overTimeSalary;
    }

    public void setOverTimeSalary(final double overTimeSalary) {
        this.overTimeSalary = overTimeSalary;
    }

    public double getSenioritySalary() {
        return senioritySalary;
    }

    public void setSenioritySalary(final double senioritySalary) {
        this.senioritySalary = senioritySalary;
    }

    public double getSalesCommissionSalary() {
        return salesCommissionSalary;
    }

    public void setSalesCommissionSalary(final double salesCommissionSalary) {
        this.salesCommissionSalary = salesCommissionSalary;
    }

    public double getAttendanceSalary() {
        return attendanceSalary;
    }

    public void setAttendanceSalary(final double attendanceSalary) {
        this.attendanceSalary = attendanceSalary;
    }

    public double getTotalSalary() {
        return this.baseSalary + this.attendanceSalary + this.senioritySalary + this.overTimeSalary + this.salesCommissionSalary;
    }
}
