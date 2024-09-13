package ai.unit.test.spike.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import ai.unit.test.spike.domain.EmployeeTYpe;
import ai.unit.test.spike.request.SalaryRequest;
import ai.unit.test.spike.response.SalaryResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class SalaryServiceTest {

    @Mock private WorkService workService;

    @InjectMocks private SalaryService salaryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCalculate_FactoryWorker() {
        SalaryRequest salaryRequest = new SalaryRequest();
        salaryRequest.setBaseSalaryRate(2000);
        salaryRequest.setWorkDays(22);
        salaryRequest.setOverTimeHours(10);
        salaryRequest.setEmployeeTYpe(EmployeeTYpe.FACTORY_WORKER);

        when(workService.get(1L)).thenReturn(salaryRequest);

        SalaryResponse response = salaryService.calculate(1L);

        assertEquals(15000, response.getBaseSalary());
        assertEquals(1500, response.getOverTimeSalary());
    }

    @Test
    void testCalculate_Sales() {
        SalaryRequest salaryRequest = new SalaryRequest();
        salaryRequest.setBaseSalaryRate(2000);
        salaryRequest.setWorkDays(22);
        salaryRequest.setEmployeeTYpe(EmployeeTYpe.SALES);
        salaryRequest.setSalesRevenue(10000);

        when(workService.get(1L)).thenReturn(salaryRequest);

        SalaryResponse response = salaryService.calculate(1L);

        assertEquals(1000000, response.getSalesCommissionSalary());
    }

    @Test
    void testCalculate_AttendanceBonus() {
        SalaryRequest salaryRequest = new SalaryRequest();
        salaryRequest.setBaseSalaryRate(2000);
        salaryRequest.setWorkDays(22);

        when(workService.get(1L)).thenReturn(salaryRequest);

        SalaryResponse response = salaryService.calculate(1L);

        assertEquals(100, response.getAttendanceSalary());
    }

    @Test
    void testCalculate_SeniorityBonus() {
        SalaryRequest salaryRequest = new SalaryRequest();
        salaryRequest.setBaseSalaryRate(2000);
        salaryRequest.setWorkDays(22);
        salaryRequest.setSeniority(10);

        when(workService.get(1L)).thenReturn(salaryRequest);

        SalaryResponse response = salaryService.calculate(1L);

        assertEquals(200, response.getSenioritySalary());
    }

    @Test
    void testCalculate_NoBonus() {
        SalaryRequest salaryRequest = new SalaryRequest();
        salaryRequest.setBaseSalaryRate(2000);
        salaryRequest.setWorkDays(20);

        when(workService.get(1L)).thenReturn(salaryRequest);

        SalaryResponse response = salaryService.calculate(1L);

        assertEquals(6666.67, response.getBaseSalary());
        assertEquals(0, response.getOverTimeSalary());
        assertEquals(0, response.getSenioritySalary());
        assertEquals(0, response.getSalesCommissionSalary());
        assertEquals(0, response.getAttendanceSalary());
    }
}
