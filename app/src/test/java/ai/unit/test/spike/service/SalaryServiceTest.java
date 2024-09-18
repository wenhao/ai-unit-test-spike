package ai.unit.test.spike.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import ai.unit.test.spike.domain.EmployeeType;
import ai.unit.test.spike.request.SalaryRequest;
import ai.unit.test.spike.response.SalaryResponse;

class SalaryServiceTest {

    @Mock
    private WorkService workService;

    @InjectMocks
    private SalaryService salaryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * 测试意图：文员助理仅含基本考勤工资+工龄工资+全勤奖。
     */
    @Test
    void should_calculate_salary_for_office_assistant_with_base_salary_and_seniority_salary_and_attendance_salary() {
        // 测试数据
        SalaryRequest salaryRequest = new SalaryRequest();
        salaryRequest.setBaseSalaryRate(4400);
        salaryRequest.setEmployeeType(EmployeeType.OFFICE_ASSISTANT);
        salaryRequest.setOverTimeHours(0);
        salaryRequest.setSeniority(3);
        salaryRequest.setSalesRevenue(0);
        salaryRequest.setWorkDays(22);

        // 模拟行为
        when(workService.get(801L)).thenReturn(salaryRequest);

        // 执行测试
        SalaryResponse salaryResponse = salaryService.calculate(801L);

        // 断言结果
        assertThat(salaryResponse.getBaseSalary()).isEqualTo(4400);
        assertThat(salaryResponse.getOverTimeSalary()).isEqualTo(0);
        assertThat(salaryResponse.getSenioritySalary()).isEqualTo(16);
        assertThat(salaryResponse.getAttendanceSalary()).isEqualTo(100);
        assertThat(salaryResponse.getSalesCommissionSalary()).isEqualTo(0);
    }

    /**
     * 测试意图：车间薪资=基本考勤工资+加班工资+工龄工资+全勤奖。
     */
    @Test
    void should_calculate_salary_for_factory_worker_with_base_salary_and_overtime_salary_and_seniority_salary_and_attendance_salary() {
        // 测试数据
        SalaryRequest salaryRequest = new SalaryRequest();
        salaryRequest.setBaseSalaryRate(3520);
        salaryRequest.setEmployeeType(EmployeeType.FACTORY_WORKER);
        salaryRequest.setOverTimeHours(13);
        salaryRequest.setSeniority(1);
        salaryRequest.setSalesRevenue(0);
        salaryRequest.setWorkDays(22);

        // 模拟行为
        when(workService.get(803L)).thenReturn(salaryRequest);

        // 执行测试
        SalaryResponse salaryResponse = salaryService.calculate(803L);

        // 断言结果
        assertThat(salaryResponse.getBaseSalary()).isEqualTo(3520); // 3520 / 22 * 22
        assertThat(salaryResponse.getOverTimeSalary()).isEqualTo(390); // (3520 / 22 / 8 * 1.5) * 13
        assertThat(salaryResponse.getSenioritySalary()).isEqualTo(10); // (1 - 1) * 3 + 10
        assertThat(salaryResponse.getAttendanceSalary()).isEqualTo(100);
        assertThat(salaryResponse.getSalesCommissionSalary()).isEqualTo(0);
    }

    /**
     * 测试意图：车间薪资=部分考勤工资+加班工资+工龄工资。
     */
    @Test
    void should_calculate_salary_for_factory_worker_with_partial_base_salary_and_overtime_salary_and_seniority_salary_and_without_attendance_salary() {
        // 测试数据
        SalaryRequest salaryRequest = new SalaryRequest();
        salaryRequest.setBaseSalaryRate(3520);
        salaryRequest.setEmployeeType(EmployeeType.FACTORY_WORKER);
        salaryRequest.setOverTimeHours(6);
        salaryRequest.setSeniority(7);
        salaryRequest.setSalesRevenue(0);
        salaryRequest.setWorkDays(19);

        // 模拟行为
        when(workService.get(804L)).thenReturn(salaryRequest);

        // 执行测试
        SalaryResponse salaryResponse = salaryService.calculate(804L);

        // 断言结果
        assertThat(salaryResponse.getBaseSalary()).isEqualTo(3040); // 3520 / 22 * 19
        assertThat(salaryResponse.getOverTimeSalary()).isEqualTo(180); // (3520 / 22 / 8 * 1.5) * 6
        assertThat(salaryResponse.getSenioritySalary()).isEqualTo(28); // (7 - 1) * 3 + 10
        assertThat(salaryResponse.getAttendanceSalary()).isEqualTo(0);
        assertThat(salaryResponse.getSalesCommissionSalary()).isEqualTo(0);
    }

    /**
     * 测试意图：质检技术人员薪资=基本考勤工资+工龄工资+全勤奖。
     */
    @Test
    void should_calculate_salary_for_quality_control_with_base_salary_and_seniority_salary_and_attendance_salary() {
        // 测试数据
        SalaryRequest salaryRequest = new SalaryRequest();
        salaryRequest.setBaseSalaryRate(5280);
        salaryRequest.setEmployeeType(EmployeeType.QUALITY_CONTROL);
        salaryRequest.setOverTimeHours(0);
        salaryRequest.setSeniority(5);
        salaryRequest.setSalesRevenue(0);
        salaryRequest.setWorkDays(22);

        // 模拟行为
        when(workService.get(805L)).thenReturn(salaryRequest);

        // 执行测试
        SalaryResponse salaryResponse = salaryService.calculate(805L);

        // 断言结果
        assertThat(salaryResponse.getBaseSalary()).isEqualTo(5280); // 5280 / 22 * 22
        assertThat(salaryResponse.getOverTimeSalary()).isEqualTo(0);
        assertThat(salaryResponse.getSenioritySalary()).isEqualTo(22); // (5 - 1) * 3 + 10
        assertThat(salaryResponse.getAttendanceSalary()).isEqualTo(100);
        assertThat(salaryResponse.getSalesCommissionSalary()).isEqualTo(0);
    }

    /**
     * 测试意图：销售人员薪资=基本考勤工资+工龄工资+全勤奖+提成。
     */
    @Test
    void should_calculate_salary_for_sales_with_base_salary_and_seniority_salary_and_attendance_salary_and_sales_commission_salary() {
        // 测试数据
        SalaryRequest salaryRequest = new SalaryRequest();
        salaryRequest.setBaseSalaryRate(6160);
        salaryRequest.setEmployeeType(EmployeeType.SALES);
        salaryRequest.setOverTimeHours(0);
        salaryRequest.setSeniority(1);
        salaryRequest.setSalesRevenue(7);
        salaryRequest.setWorkDays(22);

        // 模拟行为
        when(workService.get(806L)).thenReturn(salaryRequest);

        // 执行测试
        SalaryResponse salaryResponse = salaryService.calculate(806L);

        // 断言结果
        assertThat(salaryResponse.getBaseSalary()).isEqualTo(6160); // 6160 / 22 * 22
        assertThat(salaryResponse.getOverTimeSalary()).isEqualTo(0);
        assertThat(salaryResponse.getSenioritySalary()).isEqualTo(10); // (1 - 1) * 3 + 10
        assertThat(salaryResponse.getAttendanceSalary()).isEqualTo(100);
        assertThat(salaryResponse.getSalesCommissionSalary()).isEqualTo(1000); // 假设销售提成为每笔业务固定金额
    }

    /**
     * 测试意图：销售人员薪资=基本考勤工资+工龄工资+全勤奖+提成。
     */
    @Test
    void should_calculate_salary_for_sales_with_base_salary_and_seniority_salary_and_attendance_salary_and_sales_commission_salary_more() {
        // 测试数据
        SalaryRequest salaryRequest = new SalaryRequest();
        salaryRequest.setBaseSalaryRate(6160);
        salaryRequest.setEmployeeType(EmployeeType.SALES);
        salaryRequest.setOverTimeHours(0);
        salaryRequest.setSeniority(7);
        salaryRequest.setSalesRevenue(38);
        salaryRequest.setWorkDays(22);

        // 模拟行为
        when(workService.get(807L)).thenReturn(salaryRequest);

        // 执行测试
        SalaryResponse salaryResponse = salaryService.calculate(807L);

        // 断言结果
        assertThat(salaryResponse.getBaseSalary()).isEqualTo(6160); // 6160 / 22 * 22
        assertThat(salaryResponse.getOverTimeSalary()).isEqualTo(0);
        assertThat(salaryResponse.getSenioritySalary()).isEqualTo(28); // (7 - 1) * 3 + 10
        assertThat(salaryResponse.getAttendanceSalary()).isEqualTo(100);
        assertThat(salaryResponse.getSalesCommissionSalary()).isEqualTo(4000); // 假设销售提成为每笔业务固定金额
    }

    /**
     * 测试意图：销售人员薪资=部分考勤工资+工龄工资+提成。
     */
    @Test
    void should_calculate_salary_for_sales_with_partial_base_salary_and_full_seniority_salary_and_without_attendance_salary_and_sales_commission_salary() {
        // 测试数据
        SalaryRequest salaryRequest = new SalaryRequest();
        salaryRequest.setBaseSalaryRate(6160);
        salaryRequest.setEmployeeType(EmployeeType.SALES);
        salaryRequest.setOverTimeHours(0);
        salaryRequest.setSeniority(12);
        salaryRequest.setSalesRevenue(54);
        salaryRequest.setWorkDays(21);

        // 模拟行为
        when(workService.get(808L)).thenReturn(salaryRequest);

        // 执行测试
        SalaryResponse salaryResponse = salaryService.calculate(808L);

        // 断言结果
        assertThat(salaryResponse.getBaseSalary()).isEqualTo(5880); // 6160 / 22 * 21
        assertThat(salaryResponse.getOverTimeSalary()).isEqualTo(0);
        assertThat(salaryResponse.getSenioritySalary()).isEqualTo(30); // (12 - 1) * 3
        assertThat(salaryResponse.getAttendanceSalary()).isEqualTo(0);
        assertThat(salaryResponse.getSalesCommissionSalary()).isEqualTo(5000); // 假设销售提成为每笔业务固定金额
    }
}
