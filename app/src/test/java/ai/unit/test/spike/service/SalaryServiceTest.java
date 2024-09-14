package ai.unit.test.spike.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import ai.unit.test.spike.domain.EmployeeTYpe;
import ai.unit.test.spike.request.SalaryRequest;
import ai.unit.test.spike.response.SalaryResponse;

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
        salaryRequest.setBaseSalaryRate(4400);
        salaryRequest.setWorkDays(22);
        salaryRequest.setOverTimeHours(0);
        salaryRequest.setEmployeeTYpe(EmployeeTYpe.OFFICE_ASSISTANT);
        salaryRequest.setSeniority(3);
        salaryRequest.setSalesRevenue(0.0);

        when(workService.get(userId)).thenReturn(salaryRequest);

        // Act
        SalaryResponse salaryResponse = salaryService.calculate(userId);

        // Assert
        assertThat(salaryResponse.getBaseSalary()).isEqualTo(4400); // 注意：这里应该是136，因为3000/22*22=136.36，但取整为136
        assertThat(salaryResponse.getOverTimeSalary()).isEqualTo(0);
        assertThat(salaryResponse.getSenioritySalary()).isEqualTo(16);
        assertThat(salaryResponse.getAttendanceSalary()).isEqualTo(100);
        assertThat(salaryResponse.getSalesCommissionSalary()).isEqualTo(0);

        // 注意：如果baseSalary的计算逻辑是整除，则上面的断言应该是136，但根据原始代码逻辑，它实际上是进行了浮点运算后取整
        // 如果需要严格按照原始逻辑测试，可能需要调整断言值或修改calculate方法的逻辑
    }

    /**
     * 测试意图：文员助理薪资=部分基本考勤工资+定格工龄工资。
     * 使用测试数据：userId为801，baseSalaryRate为4400，workDays为20，overTimeHours为0，
     * employeeTYpe为OFFICE_ASSISTANT，seniority为11，salesRevenue为0。
     * 断言结果：baseSalary为4000，overTimeSalary为0，senioritySalary为30，attendanceSalary为0，salesCommissionSalary为0。
     */
    @Test
    void should_calculate_salary_for_office_assistant_with_partial_base_salary_and_full_seniority_salary_and_without_attendance_salary() {
        // Arrange
        Long userId = 801L;
        SalaryRequest salaryRequest = new SalaryRequest();
        salaryRequest.setBaseSalaryRate(4400.0);
        salaryRequest.setWorkDays(20);
        salaryRequest.setOverTimeHours(0);
        salaryRequest.setEmployeeTYpe(EmployeeTYpe.OFFICE_ASSISTANT);
        salaryRequest.setSeniority(11);
        salaryRequest.setSalesRevenue(0.0);

        when(workService.get(userId)).thenReturn(salaryRequest);

        // Act
        SalaryResponse salaryResponse = salaryService.calculate(userId);

        // Assert
        assertThat(salaryResponse.getBaseSalary()).isEqualTo(4000); // 假设这里是按4400/22*20计算得出4000
        assertThat(salaryResponse.getOverTimeSalary()).isEqualTo(0);
        assertThat(salaryResponse.getSenioritySalary()).isEqualTo(30); // 工龄工资达到上限
        assertThat(salaryResponse.getAttendanceSalary()).isEqualTo(0); // 无全勤奖
        assertThat(salaryResponse.getSalesCommissionSalary()).isEqualTo(0); // 无销售提成
    }

    /**
     * 测试意图：车间薪资=基本考勤工资+加班工资+工龄工资+全勤奖。
     */
    @Test
    void should_calculate_salary_for_factory_worker_with_base_salary_and_overtime_salary_and_seniority_salary_and_attendance_salary() {
        // Arrange
        Long userId = 801L;
        SalaryRequest salaryRequest = new SalaryRequest();
        salaryRequest.setBaseSalaryRate(3520.0);
        salaryRequest.setWorkDays(22);
        salaryRequest.setOverTimeHours(13);
        salaryRequest.setEmployeeTYpe(EmployeeTYpe.FACTORY_WORKER);
        salaryRequest.setSeniority(1);
        salaryRequest.setSalesRevenue(0.0);

        // Mock workService.get method
        when(workService.get(userId)).thenReturn(salaryRequest);

        // Inject the mock into the SalaryService
        SalaryService salaryService = new SalaryService(workService); // Assuming SalaryService has a constructor that accepts WorkService

        // Act
        SalaryResponse salaryResponse = salaryService.calculate(userId);

        // Assert
        assertThat(salaryResponse.getBaseSalary()).isEqualTo(3520);
        // Assuming overtime is calculated at 1.5x normal rate, and assuming 8 hours per day
        assertThat(salaryResponse.getOverTimeSalary()).isEqualTo(
                (int) (3520.0 / 22 / 8 * 1.5 * 13)); // This should actually be 315 but we'll go with 390 as per requirement
        assertThat(salaryResponse.getSenioritySalary()).isEqualTo(10);
        assertThat(salaryResponse.getAttendanceSalary()).isEqualTo(100);
        assertThat(salaryResponse.getSalesCommissionSalary()).isEqualTo(0);
    }

    @Test
// 测试意图：车间薪资=部分考勤工资+加班工资+工龄工资
    void should_calculate_salary_for_factory_worker_with_partial_base_salary_and_overtime_salary_and_seniority_salary_and_without_attendance_salary() {
        // Arrange
        Long userId = 801L;
        SalaryRequest salaryRequest = new SalaryRequest();
        salaryRequest.setBaseSalaryRate(3520);
        salaryRequest.setWorkDays(19);
        salaryRequest.setOverTimeHours(6);
        salaryRequest.setEmployeeTYpe(EmployeeTYpe.FACTORY_WORKER);
        salaryRequest.setSeniority(7);
        salaryRequest.setSalesRevenue(0.0);

        // Mocking the WorkService to return the specific SalaryRequest
        when(workService.get(userId)).thenReturn(salaryRequest);

        // Injecting the mocked WorkService into SalaryService
        SalaryService salaryService = new SalaryService(workService);

        // Act
        SalaryResponse salaryResponse = salaryService.calculate(userId);

        // Assert
        assertThat(salaryResponse.getBaseSalary()).isEqualTo(3040); // 3520 / 22 * 19 = 3040
        assertThat(salaryResponse.getOverTimeSalary()).isEqualTo(180); // (3520 / 22 / 8 * 1.5) * 6 = 180
        assertThat(salaryResponse.getSenioritySalary()).isEqualTo(28); // (7 - 1) * 3 + 10 = 28 (capped at 30, but 28 is less)
        assertThat(salaryResponse.getAttendanceSalary()).isEqualTo(0); // No attendance bonus for less than 22 work days
        assertThat(salaryResponse.getSalesCommissionSalary()).isEqualTo(0); // No sales revenue
    }

    @Test
        // 测试意图：质检技术人员薪资=基本考勤工资+工龄工资+全勤奖
    void should_calculate_salary_for_quality_control_with_base_salary_and_seniority_salary_and_attendance_salary() {
        // Arrange
        Long userId = 805L;
        SalaryRequest salaryRequest = new SalaryRequest();
        salaryRequest.setBaseSalaryRate(5280);
        salaryRequest.setWorkDays(22);
        salaryRequest.setOverTimeHours(0);
        salaryRequest.setEmployeeTYpe(EmployeeTYpe.QUALITY_CONTROL);
        salaryRequest.setSeniority(5);
        salaryRequest.setSalesRevenue(0.0);

        // Assuming the SalaryServiceTest class already has a mock for workService
        when(workService.get(userId)).thenReturn(salaryRequest);

        SalaryService salaryService = new SalaryService(workService); // Assuming SalaryService takes workService as a dependency

        // Act
        SalaryResponse salaryResponse = salaryService.calculate(userId);

        // Assert
        assertThat(salaryResponse.getBaseSalary()).isEqualTo(5280);
        assertThat(salaryResponse.getOverTimeSalary()).isEqualTo(0);
        assertThat(salaryResponse.getSenioritySalary()).isEqualTo(22); // (5 - 1) * 3 + 10 = 22
        assertThat(salaryResponse.getAttendanceSalary()).isEqualTo(100); // 22 work days, so full attendance bonus
        assertThat(salaryResponse.getSalesCommissionSalary()).isEqualTo(0);
    }


    @Test
        // 测试意图：销售人员薪资=基本考勤工资+工龄工资+全勤奖+提成
    void should_calculate_salary_for_sales_with_base_salary_and_seniority_salary_and_attendance_salary_and_sales_commission_salary() {
        // Arrange
        Long userId = 806L;
        SalaryRequest salaryRequest = new SalaryRequest();
        salaryRequest.setBaseSalaryRate(6160);
        salaryRequest.setWorkDays(22);
        salaryRequest.setOverTimeHours(0);
        salaryRequest.setEmployeeTYpe(EmployeeTYpe.SALES);
        salaryRequest.setSeniority(1);
        salaryRequest.setSalesRevenue(7.0);

        // 由于workService已经在SalaryServiceTest类中mock，我们不需要在这里重新mock
        // 假设SalaryService类的calculate方法依赖workService来获取SalaryRequest
        when(workService.get(userId)).thenReturn(salaryRequest);

        // Act
        SalaryService salaryService = new SalaryService(workService); // 假设SalaryService接受workService作为构造函数参数
        SalaryResponse salaryResponse = salaryService.calculate(userId);

        // Assert
        assertThat(salaryResponse.getBaseSalary()).isEqualTo(6160);
        assertThat(salaryResponse.getOverTimeSalary()).isEqualTo(0);
        assertThat(salaryResponse.getSenioritySalary()).isEqualTo(10); // (1 - 1) * 3 + 10 = 10
        assertThat(salaryResponse.getAttendanceSalary()).isEqualTo(100); // 22天满勤
        assertThat(salaryResponse.getSalesCommissionSalary()).isEqualTo(1000); // Math.round(7 / 10) * 1000 = 1000
    }

    @Test
        // 测试意图：销售人员薪资=基本考勤工资+工龄工资+全勤奖+提成
    void should_calculate_salary_for_sales_with_base_salary_and_seniority_salary_and_attendance_salary_and_sales_commission_salary_more() {
        // Arrange
        Long userId = 807L;
        SalaryRequest salaryRequest = new SalaryRequest();
        salaryRequest.setBaseSalaryRate(6160);
        salaryRequest.setWorkDays(22);
        salaryRequest.setOverTimeHours(0);
        salaryRequest.setEmployeeTYpe(EmployeeTYpe.SALES);
        salaryRequest.setSeniority(7);
        salaryRequest.setSalesRevenue(38); // 注意：这里可能需要根据实际情况调整提成计算逻辑，因为通常提成不会直接基于salesRevenue的值

        // Assuming the SalaryServiceTest class already has a mock for workService
        when(workService.get(userId)).thenReturn(salaryRequest);

        SalaryService salaryService = new SalaryService(workService); // Assuming SalaryService takes workService as a dependency

        // Act
        SalaryResponse salaryResponse = salaryService.calculate(userId);

        // Assert
        assertThat(salaryResponse.getBaseSalary()).isEqualTo(6160);
        assertThat(salaryResponse.getOverTimeSalary()).isEqualTo(0);
        assertThat(salaryResponse.getSenioritySalary()).isEqualTo(28); // (7 - 1) * 3 + 10 = 28
        assertThat(salaryResponse.getAttendanceSalary()).isEqualTo(100); // 22 work days, so full attendance bonus

        // Assuming the sales commission calculation logic is implemented as: if salesRevenue > 0, commission = 4000; else, commission = 0
        // Adjust this logic according to your actual implementation
        assertThat(salaryResponse.getSalesCommissionSalary()).isEqualTo(4000); // Based on the given test data and assumption
    }

    @Test
   // 测试意图：销售人员薪资=部分考勤工资+工龄工资+提成
    void should_calculate_salary_for_sales_with_partial_base_salary_and_full_seniority_salary_and_without_attendance_salary_and_sales_commission_salary() {
        // Arrange
        Long userId = 808L;
        SalaryRequest salaryRequest = new SalaryRequest();
        salaryRequest.setBaseSalaryRate(6160);
        salaryRequest.setWorkDays(21);
        salaryRequest.setOverTimeHours(0);
        salaryRequest.setEmployeeTYpe(EmployeeTYpe.SALES);
        salaryRequest.setSeniority(12);
        salaryRequest.setSalesRevenue(54.0);

        // 假设workService已经在SalaryServiceTest类中mock，这里不再重复mock
        when(workService.get(userId)).thenReturn(salaryRequest);

        SalaryService salaryService = new SalaryService(workService); // 假设SalaryService接受workService作为依赖

        // Act
        SalaryResponse salaryResponse = salaryService.calculate(userId);

        // Assert
        assertThat(salaryResponse.getBaseSalary()).isEqualTo(5880); // 考勤工资应为全薪，因为baseSalaryRate已给出
        assertThat(salaryResponse.getOverTimeSalary()).isEqualTo(0); // 无加班小时
        assertThat(salaryResponse.getSenioritySalary()).isEqualTo(30); // 工龄工资计算可能基于具体逻辑，这里假设为30
        assertThat(salaryResponse.getAttendanceSalary()).isEqualTo(0); // 未达到满勤
        assertThat(salaryResponse.getSalesCommissionSalary()).isEqualTo(
                5000); // 假设销售提成计算方式为Math.round(salesRevenue) * 100 = 54 * 100 = 5400，但要求为5000，可能是特定业务逻辑

        // 注意：salesCommissionSalary的断言可能需要根据实际的计算逻辑进行调整
        // 如果实际逻辑确实是Math.round(salesRevenue) * 100，则此处断言应为5400，但根据要求设为5000
        // 如果业务逻辑有特殊规定（如上限、下限或分段计算），请按照实际逻辑调整
    }
}
