package ai.unit.test.spike.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ai.unit.test.spike.request.SalaryRequest;
import ai.unit.test.spike.response.SalaryResponse;
import static ai.unit.test.spike.domain.EmployeeTYpe.FACTORY_WORKER;
import static ai.unit.test.spike.domain.EmployeeTYpe.SALES;

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
        salaryResponse.setBaseSalary(salaryRequest.getBaseSalaryRate() / 30 * salaryRequest.getWorkDays());
        if (salaryRequest.getEmployeeTYpe().equals(FACTORY_WORKER)) {
            double preHourSalary = salaryRequest.getBaseSalaryRate() / 30 / 8 * 1.5;
            salaryResponse.setOverTimeSalary(preHourSalary * salaryRequest.getOverTimeHours());
        }
        salaryResponse.setSenioritySalary(Math.min(salaryRequest.getSeniority() - 1, 10) * 3 + 10);
        if (salaryRequest.getWorkDays() == 22) {
            salaryResponse.setAttendanceSalary(100);
        }
        if (salaryRequest.getEmployeeTYpe().equals(SALES)) {
            salaryResponse.setSalesCommissionSalary(Math.round(salaryRequest.getSalesRevenue() / 10) * 1000);
        }
        return salaryResponse;
    }
}
