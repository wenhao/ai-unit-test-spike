package ai.unit.test.spike.response;

public class SalaryResponse {
    private int baseSalary;
    private int overTimeSalary;
    private int senioritySalary;
    private int attendanceSalary;
    private int salesCommissionSalary;
    private int totalSalary;

    public int getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(final int baseSalary) {
        this.baseSalary = baseSalary;
    }

    public int getOverTimeSalary() {
        return overTimeSalary;
    }

    public void setOverTimeSalary(final int overTimeSalary) {
        this.overTimeSalary = overTimeSalary;
    }

    public int getSenioritySalary() {
        return senioritySalary;
    }

    public void setSenioritySalary(final int senioritySalary) {
        this.senioritySalary = senioritySalary;
    }

    public int getSalesCommissionSalary() {
        return salesCommissionSalary;
    }

    public void setSalesCommissionSalary(final int salesCommissionSalary) {
        this.salesCommissionSalary = salesCommissionSalary;
    }

    public int getAttendanceSalary() {
        return attendanceSalary;
    }

    public void setAttendanceSalary(final int attendanceSalary) {
        this.attendanceSalary = attendanceSalary;
    }

    public int getTotalSalary() {
        return this.baseSalary + this.attendanceSalary + this.senioritySalary + this.overTimeSalary + this.salesCommissionSalary;
    }
}
