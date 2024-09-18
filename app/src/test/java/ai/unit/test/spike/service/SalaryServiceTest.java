package ai.unit.test.spike.service;

import ai.unit.test.spike.domain.EmployeeType;
import ai.unit.test.spike.request.SalaryRequest;
import ai.unit.test.spike.response.SalaryResponse;
import ai.unit.test.spike.service.SalaryService;
import ai.unit.test.spike.service.WorkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class SalaryServiceTest {

    @Mock
    private WorkService workService;

    @InjectMocks
    private SalaryService salaryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * 测试意图：文员助理仅含基本考勤工资+工龄工资+全勤奖。
     */
    @Test
    public void should_calculate_salary_for_office_assistant_with_base_salary_and_seniority_salary_and_attendance_salary() {
        // 测试数据
        Long userId = 801L;
        SalaryRequest salaryRequest = new SalaryRequest();
        salaryRequest.setBaseSalaryRate(4400);
        salaryRequest.setWorkDays(22);
        salaryRequest.setOverTimeHours(0);
        salaryRequest.setEmployeeTYpe(EmployeeType.OFFICE_ASSISTANT);
        salaryRequest.setSeniority(3);
        salaryRequest.setSalesRevenue(0);

        // 模拟workService.get方法返回salaryRequest
        when(workService.get(userId)).thenReturn(salaryRequest);

        // 调用calculate方法
        SalaryResponse salaryResponse = salaryService.calculate(userId);

        // 断言结果
        assertThat(salaryResponse.getBaseSalary()).isEqualTo(4400);
        assertThat(salaryResponse.getOverTimeSalary()).isEqualTo(0);
        assertThat(salaryResponse.getSenioritySalary()).isEqualTo(16);
        assertThat(salaryResponse.getAttendanceSalary()).isEqualTo(100);
        assertThat(salaryResponse.getSalesCommissionSalary()).isEqualTo(0);
    }

    /**
     * 测试意图：文员助理仅含基本考勤工资+工龄工资+全勤奖。
     */
    @Test
    public void should_calculate_salary_for_office_assistant_with_partial_base_salary_and_full_seniority_salary_and_without_attendance_salary() {
        // 准备测试数据
        Long userId = 802L;
        SalaryRequest salaryRequest = new SalaryRequest();
        salaryRequest.setBaseSalaryRate(4400);
        salaryRequest.setWorkDays(20);
        salaryRequest.setOverTimeHours(0);
        salaryRequest.setEmployeeTYpe(EmployeeType.OFFICE_ASSISTANT);
        salaryRequest.setSeniority(11);
        salaryRequest.setSalesRevenue(0);

        when(workService.get(userId)).thenReturn(salaryRequest);

        // 调用被测试方法
        SalaryResponse salaryResponse = salaryService.calculate(userId);

        // 断言结果
        assertThat(salaryResponse.getBaseSalary()).isEqualTo(4000);
        assertThat(salaryResponse.getOverTimeSalary()).isEqualTo(0);
        assertThat(salaryResponse.getSenioritySalary()).isEqualTo(30);
        assertThat(salaryResponse.getAttendanceSalary()).isEqualTo(0);
        assertThat(salaryResponse.getSalesCommissionSalary()).isEqualTo(0);
    }

    /**
     * 测试意图：车间薪资=基本考勤工资+加班工资+工龄工资+全勤奖。
     */
    @Test
    public void should_calculate_salary_for_factory_worker_with_base_salary_and_overtime_salary_and_seniority_salary_and_attendance_salary() {
        // 准备测试数据
        Long userId = 803L;
        SalaryRequest salaryRequest = new SalaryRequest();
        salaryRequest.setBaseSalaryRate(3520);
        salaryRequest.setWorkDays(22);
        salaryRequest.setOverTimeHours(13);
        salaryRequest.setEmployeeTYpe(EmployeeType.FACTORY_WORKER);
        salaryRequest.setSeniority(1);
        salaryRequest.setSalesRevenue(0);

        when(workService.get(userId)).thenReturn(salaryRequest);

        // 调用被测试方法
        SalaryResponse salaryResponse = salaryService.calculate(userId);

        // 断言结果
        assertThat(salaryResponse.getBaseSalary()).isEqualTo(3520);
        assertThat(salaryResponse.getOverTimeSalary()).isEqualTo(390);
        assertThat(salaryResponse.getSenioritySalary()).isEqualTo(10);
        assertThat(salaryResponse.getAttendanceSalary()).isEqualTo(100);
        assertThat(salaryResponse.getSalesCommissionSalary()).isEqualTo(0);
    }

    @Test
    void should_calculate_salary_for_quality_control_with_base_salary_and_seniority_salary_and_attendance_salary() {
        // 准备测试数据
        Long userId = 805L;
        SalaryRequest salaryRequest = new SalaryRequest();
        salaryRequest.setBaseSalaryRate(5280);
        salaryRequest.setWorkDays(22);
        salaryRequest.setOverTimeHours(0);
        salaryRequest.setEmployeeTYpe(EmployeeType.QUALITY_CONTROL);
        salaryRequest.setSeniority(5);
        salaryRequest.setSalesRevenue(0);

        // 模拟workService.get方法返回测试数据
        when(workService.get(userId)).thenReturn(salaryRequest);

        // 调用被测试方法
        SalaryResponse salaryResponse = salaryService.calculate(userId);

        // 断言结果
        assertThat(salaryResponse.getBaseSalary()).isEqualTo(5280);
        assertThat(salaryResponse.getOverTimeSalary()).isEqualTo(0);
        assertThat(salaryResponse.getSenioritySalary()).isEqualTo(22);
        assertThat(salaryResponse.getAttendanceSalary()).isEqualTo(100);
        assertThat(salaryResponse.getSalesCommissionSalary()).isEqualTo(0);
    }

    /**
     * 测试意图：销售人员薪资=基本考勤工资+工龄工资+全勤奖+提成。
     */
    @Test
    void should_calculate_salary_for_sales_with_base_salary_and_seniority_salary_and_attendance_salary_and_sales_commission_salary() {
        // 准备测试数据
        Long userId = 806L;
        SalaryRequest salaryRequest = new SalaryRequest();
        salaryRequest.setBaseSalaryRate(6160);
        salaryRequest.setWorkDays(22);
        salaryRequest.setOverTimeHours(0);
        salaryRequest.setEmployeeTYpe(EmployeeType.SALES);
        salaryRequest.setSeniority(1);
        salaryRequest.setSalesRevenue(7);

        // 模拟workService.get方法返回测试数据
        when(workService.get(userId)).thenReturn(salaryRequest);

        // 调用被测试方法
        SalaryResponse salaryResponse = salaryService.calculate(userId);

        // 断言结果
        assertThat(salaryResponse.getBaseSalary()).isEqualTo(6160);
        assertThat(salaryResponse.getOverTimeSalary()).isEqualTo(0);
        assertThat(salaryResponse.getSenioritySalary()).isEqualTo(10);
        assertThat(salaryResponse.getAttendanceSalary()).isEqualTo(100);
        assertThat(salaryResponse.getSalesCommissionSalary()).isEqualTo(1000);
    }

    /**
     * 测试意图：销售人员薪资=基本考勤工资+工龄工资+全勤奖+提成。
     */
    @Test
    void should_calculate_salary_for_sales_with_base_salary_and_seniority_salary_and_attendance_salary_and_sales_commission_salary_more() {
        // 准备测试数据
        Long userId = 807L;
        SalaryRequest salaryRequest = new SalaryRequest();
        salaryRequest.setBaseSalaryRate(6160);
        salaryRequest.setWorkDays(22);
        salaryRequest.setOverTimeHours(0);
        salaryRequest.setEmployeeTYpe(EmployeeType.SALES);
        salaryRequest.setSeniority(7);
        salaryRequest.setSalesRevenue(38);

        // 模拟workService.get方法返回测试数据
        when(workService.get(userId)).thenReturn(salaryRequest);

        // 调用被测试方法
        SalaryResponse salaryResponse = salaryService.calculate(userId);

        // 断言结果
        assertThat(salaryResponse.getBaseSalary()).isEqualTo(6160);
        assertThat(salaryResponse.getOverTimeSalary()).isEqualTo(0);
        assertThat(salaryResponse.getSenioritySalary()).isEqualTo(28);
        assertThat(salaryResponse.getAttendanceSalary()).isEqualTo(100);
        assertThat(salaryResponse.getSalesCommissionSalary()).isEqualTo(4000);
    }

    /**
     * 测试意图：销售人员薪资=部分考勤工资+工龄工资+提成。
     */
    @Test
    void should_calculate_salary_for_sales_with_partial_base_salary_and_full_seniority_salary_and_without_attendance_salary_and_sales_commission_salary() {
        // 准备测试数据
        Long userId = 808L;
        SalaryRequest salaryRequest = new SalaryRequest();
        salaryRequest.setBaseSalaryRate(6160);
        salaryRequest.setWorkDays(21);
        salaryRequest.setOverTimeHours(0);
        salaryRequest.setEmployeeTYpe(EmployeeType.SALES);
        salaryRequest.setSeniority(12);
        salaryRequest.setSalesRevenue(54);

        // 模拟workService.get方法返回测试数据
        when(workService.get(userId)).thenReturn(salaryRequest);

        // 调用被测试方法
        SalaryResponse salaryResponse = salaryService.calculate(userId);

        // 断言结果
        assertThat(salaryResponse.getBaseSalary()).isEqualTo(5880);
        assertThat(salaryResponse.getOverTimeSalary()).isEqualTo(0);
        assertThat(salaryResponse.getSenioritySalary()).isEqualTo(30);
        assertThat(salaryResponse.getAttendanceSalary()).isEqualTo(0);
        assertThat(salaryResponse.getSalesCommissionSalary()).isEqualTo(5000);
    }
}
