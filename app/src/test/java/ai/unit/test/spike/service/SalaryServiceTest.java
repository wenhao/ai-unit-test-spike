package ai.unit.test.spike.service;

import ai.unit.test.spike.domain.EmployeeTYpe;
import ai.unit.test.spike.request.SalaryRequest;
import ai.unit.test.spike.response.SalaryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class SalaryServiceTest {

    @Mock
    private WorkService workService;

    @InjectMocks
    private SalaryService salaryService;

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
        salaryRequest.setOverTimeHours(10);
        salaryRequest.setEmployeeTYpe(EmployeeTYpe.SALES);
        salaryRequest.setSalesRevenue(100000);

        when(workService.get(1L)).thenReturn(salaryRequest);

        SalaryResponse response = salaryService.calculate(1L);

        assertEquals(15000, response.getBaseSalary());
        assertEquals(10000, response.getSalesCommissionSalary());
    }

    @Test
    void testCalculate_Attendance() {
        SalaryRequest salaryRequest = new SalaryRequest();
        salaryRequest.setBaseSalaryRate(2000);
        salaryRequest.setWorkDays(22);
        salaryRequest.setOverTimeHours(10);
        salaryRequest.setEmployeeTYpe(EmployeeTYpe.FACTORY_WORKER);

        when(workService.get(1L)).thenReturn(salaryRequest);

        SalaryResponse response = salaryService.calculate(1L);

        assertEquals(15000, response.getBaseSalary());
        assertEquals(1500, response.getOverTimeSalary());
        assertEquals(100, response.getAttendanceSalary());
    }

    @Test
    void testCalculate_Seniority() {
        SalaryRequest salaryRequest = new SalaryRequest();
        salaryRequest.setBaseSalaryRate(2000);
        salaryRequest.setWorkDays(22);
        salaryRequest.setOverTimeHours(10);
        salaryRequest.setEmployeeTYpe(EmployeeTYpe.FACTORY_WORKER);
        salaryRequest.setSeniority(5);

        when(workService.get(1L)).thenReturn(salaryRequest);

        SalaryResponse response = salaryService.calculate(1L);

        assertEquals(15000, response.getBaseSalary());
        assertEquals(1500, response.getOverTimeSalary());
        assertEquals(20, response.getSenioritySalary());
    }
}
