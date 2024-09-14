package ai.unit.test.spike.service;

import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableMap;

import ai.unit.test.spike.domain.EmployeeTYpe;
import ai.unit.test.spike.request.SalaryRequest;
import static ai.unit.test.spike.domain.EmployeeTYpe.FACTORY_WORKER;
import static ai.unit.test.spike.domain.EmployeeTYpe.OFFICE_ASSISTANT;
import static ai.unit.test.spike.domain.EmployeeTYpe.QUALITY_CONTROL;
import static ai.unit.test.spike.domain.EmployeeTYpe.SALES;

@Service
public class WorkService {
    public SalaryRequest get(final Long userId) {
        return ImmutableMap.<Long, SalaryRequest>builder()
                .put(801L, getSalaryRequest(OFFICE_ASSISTANT, 4400, 0, 3, 22, 0))
                .put(802L, getSalaryRequest(OFFICE_ASSISTANT, 4400, 0, 11, 20, 0))
                .put(803L, getSalaryRequest(FACTORY_WORKER, 3520, 13, 1, 22, 0))
                .put(804L, getSalaryRequest(FACTORY_WORKER, 3520, 6, 7, 19, 0))
                .put(805L, getSalaryRequest(QUALITY_CONTROL, 5280, 0, 5, 22, 0))
                .put(806L, getSalaryRequest(SALES, 6160, 0, 1, 22, 7))
                .put(807L, getSalaryRequest(SALES, 6160, 0, 4, 22, 38))
                .put(808L, getSalaryRequest(SALES, 6160, 0, 12, 21, 54))
                .build()
                .get(userId);
    }

    private SalaryRequest getSalaryRequest(EmployeeTYpe employeeTYpe, int baseSalaryRate, int overTimeHours, int seniority,
                                           int workDays, int salesRevenue) {
        SalaryRequest salaryRequest = new SalaryRequest();
        salaryRequest.setEmployeeTYpe(employeeTYpe);
        salaryRequest.setBaseSalaryRate(baseSalaryRate);
        salaryRequest.setOverTimeHours(overTimeHours);
        salaryRequest.setSeniority(seniority);
        salaryRequest.setWorkDays(workDays);
        salaryRequest.setSalesRevenue(salesRevenue);
        return salaryRequest;
    }
}
