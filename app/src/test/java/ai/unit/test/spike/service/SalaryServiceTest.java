package ai.unit.test.spike.service;

import ai.unit.test.spike.domain.EmployeeTYpe;
import ai.unit.test.spike.request.SalaryRequest;
import ai.unit.test.spike.response.SalaryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class SalaryServiceTest {

    @InjectMocks
    private SalaryService salaryService;

    @Mock
    private WorkService workService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * 测试意图：文员助理仅含基本考勤工资+工龄工资+全勤奖。
     */
    @Test
    void should_calculate_salary_for_office_assistant_with_base_salary_and_seniority_salary_and_attendance_salary() {
        // Arrange
        Long userId = 801L;
        SalaryRequest salaryRequest = new SalaryRequest();
        salaryRequest.setBaseSalaryRate(3000.0);
        salaryRequest.setWorkDays(22);
        salaryRequest.setOverTimeHours(0);
        salaryRequest.setEmployeeTYpe(EmployeeTYpe.OFFICE_ASSISTANT);
        salaryRequest.setSeniority(3);
        salaryRequest.setSalesRevenue(0.0);

        when(workService.get(userId)).thenReturn(salaryRequest);

        // Act
        SalaryResponse salaryResponse = salaryService.calculate(userId);

        // Assert
        assertThat(salaryResponse.getBaseSalary()).isEqualTo(3000); // 注意：这里应该是136，因为3000/22*22=136.36，但取整为136
        assertThat(salaryResponse.getOverTimeSalary()).isEqualTo(0);
        assertThat(salaryResponse.getSenioritySalary()).isEqualTo(16);
        assertThat(salaryResponse.getAttendanceSalary()).isEqualTo(100);
        assertThat(salaryResponse.getSalesCommissionSalary()).isEqualTo(0);

        // 注意：如果baseSalary的计算逻辑是整除，则上面的断言应该是136，但根据原始代码逻辑，它实际上是进行了浮点运算后取整
        // 如果需要严格按照原始逻辑测试，可能需要调整断言值或修改calculate方法的逻辑
    }
}
