package ai.unit.test.spike.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ai.unit.test.spike.request.SalaryRequest;
import ai.unit.test.spike.response.SalaryResponse;
import static ai.unit.test.spike.domain.EmployeeType.FACTORY_WORKER;
import static ai.unit.test.spike.domain.EmployeeType.SALES;

@Service
public class SalaryService {
    private final WorkService workService;

    @Autowired
    public SalaryService(final WorkService workService) {
        this.workService = workService;
    }

    public SalaryResponse calculate(Long userId) {
        SalaryRequest salaryRequest = workService.get(userId);
        SalaryResponse salaryResponse = new SalaryResponse();
        salaryResponse.setBaseSalary(Double.valueOf(salaryRequest.getBaseSalaryRate() / 22 * salaryRequest.getWorkDays()).intValue());
        if (salaryRequest.getEmployeeTYpe().equals(FACTORY_WORKER)) {
            double preHourSalary = salaryRequest.getBaseSalaryRate() / 22 / 8 * 1.5;
            salaryResponse.setOverTimeSalary(Double.valueOf((preHourSalary * salaryRequest.getOverTimeHours())).intValue());
        }
        salaryResponse.setSenioritySalary(Math.min((salaryRequest.getSeniority() - 1) * 3 + 10, 30));
        if (salaryRequest.getWorkDays() == 22) {
            salaryResponse.setAttendanceSalary(100);
        }
        if (salaryRequest.getEmployeeTYpe().equals(SALES)) {
            salaryResponse.setSalesCommissionSalary(Double.valueOf((Math.round(salaryRequest.getSalesRevenue() / 10) * 1000)).intValue());
        }
        return salaryResponse;
    }
}
