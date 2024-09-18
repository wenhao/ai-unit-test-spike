### 通义灵码

### 第一次Spike
直接选择SalaryService的calculate方法并使用通义灵码进行生成单测，执行的指令是：“/generate unit test”，生成结果如下：

```java
package ai.unit.test.spike.service;

import java.util.*;
import java.math.*;

import ai.unit.test.spike.domain.EmployeeType;
import ai.unit.test.spike.request.SalaryRequest;
import ai.unit.test.spike.response.SalaryResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SalaryServiceTest {

    @Mock
    private WorkService workService;

    @InjectMocks
    private SalaryService salaryService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void calculate_FactoryWorkerWithOvertime_ShouldCalculateCorrectly() {
        SalaryRequest salaryRequest = new SalaryRequest();
        salaryRequest.setBaseSalaryRate(1000);
        salaryRequest.setEmployeeTYpe(EmployeeType.FACTORY_WORKER);
        salaryRequest.setOverTimeHours(10);
        salaryRequest.setSeniority(3);
        salaryRequest.setWorkDays(20);

        when(workService.get(1L)).thenReturn(salaryRequest);

        SalaryResponse salaryResponse = salaryService.calculate(1L);

        assertEquals(909, salaryResponse.getBaseSalary());
        assertEquals(182, salaryResponse.getOverTimeSalary());
        assertEquals(20, salaryResponse.getSenioritySalary());
        assertEquals(0, salaryResponse.getAttendanceSalary());
        assertEquals(0, salaryResponse.getSalesCommissionSalary());
        assertEquals(1111, salaryResponse.getTotalSalary());
    }

    @Test
    public void calculate_SalesWithoutOvertime_ShouldCalculateCorrectly() {
        SalaryRequest salaryRequest = new SalaryRequest();
        salaryRequest.setBaseSalaryRate(1000);
        salaryRequest.setEmployeeTYpe(EmployeeType.SALES);
        salaryRequest.setOverTimeHours(0);
        salaryRequest.setSalesRevenue(5000);
        salaryRequest.setSeniority(1);
        salaryRequest.setWorkDays(22);

        when(workService.get(2L)).thenReturn(salaryRequest);

        SalaryResponse salaryResponse = salaryService.calculate(2L);

        assertEquals(1000, salaryResponse.getBaseSalary());
        assertEquals(0, salaryResponse.getOverTimeSalary());
        assertEquals(10, salaryResponse.getSenioritySalary());
        assertEquals(100, salaryResponse.getAttendanceSalary());
        assertEquals(5000, salaryResponse.getSalesCommissionSalary());
        assertEquals(7010, salaryResponse.getTotalSalary());
    }

    @Test
    public void calculate_OfficeAssistantWithAttendance_ShouldCalculateCorrectly() {
        SalaryRequest salaryRequest = new SalaryRequest();
        salaryRequest.setBaseSalaryRate(1500);
        salaryRequest.setEmployeeTYpe(EmployeeType.OFFICE_ASSISTANT);
        salaryRequest.setOverTimeHours(0);
        salaryRequest.setSeniority(5);
        salaryRequest.setWorkDays(22);

        when(workService.get(3L)).thenReturn(salaryRequest);

        SalaryResponse salaryResponse = salaryService.calculate(3L);

        assertEquals(1500, salaryResponse.getBaseSalary());
        assertEquals(0, salaryResponse.getOverTimeSalary());
        assertEquals(30, salaryResponse.getSenioritySalary());
        assertEquals(100, salaryResponse.getAttendanceSalary());
        assertEquals(0, salaryResponse.getSalesCommissionSalary());
        assertEquals(1730, salaryResponse.getTotalSalary());
    }

    @Test
    public void calculate_QualityControlWithOvertimeAndAttendance_ShouldCalculateCorrectly() {
        SalaryRequest salaryRequest = new SalaryRequest();
        salaryRequest.setBaseSalaryRate(2000);
        salaryRequest.setEmployeeTYpe(EmployeeType.QUALITY_CONTROL);
        salaryRequest.setOverTimeHours(5);
        salaryRequest.setSeniority(7);
        salaryRequest.setWorkDays(22);

        when(workService.get(4L)).thenReturn(salaryRequest);

        SalaryResponse salaryResponse = salaryService.calculate(4L);

        assertEquals(2000, salaryResponse.getBaseSalary());
        assertEquals(188, salaryResponse.getOverTimeSalary());
        assertEquals(30, salaryResponse.getSenioritySalary());
        assertEquals(100, salaryResponse.getAttendanceSalary());
        assertEquals(0, salaryResponse.getSalesCommissionSalary());
        assertEquals(2418, salaryResponse.getTotalSalary());
    }
}
```

#### 评价：

```markdown
* [失败][严重]正确使用JUnit框架版本: 没有识别JUnit版本。
* [失败][次要]正确使用AssertJ框架：未识别到AssertJ框架。
* [成功][严重]正确地使用Mock框架。
* [成功][次要]生成单测可自动放在对应的文件目录: 点击采纳按钮后能自动放在对应的测试目录。
* [失败][严重]生成的单测可编译: 因为没有识别正确的框架故无法编译。
* [失败][严重]生成单测可执行: 无法编译则无法执行。
* [成功][严重]正确地使用断言：断言比较齐全。
* [失败][重要]生成的单测测试方法名可读性高: 测试方法命名有一定规律，感觉像是某种规范，也许是阿里内部测试代码的命名规范，还将就。
* [成功][次要]没有重复的测试：没有重复的测试。
* [失败][重要]测试覆盖率高: 测试失败无测试覆盖率。
```

### 第二次Spike
尝试修复JUnit、AssertJ等框架问题，修改提示词为：“请使用JUnit5、AssertJ和Mockito框架”，生成结果如下：

```java
package ai.unit.test.spike.service;

import ai.unit.test.spike.domain.EmployeeType;
import ai.unit.test.spike.request.SalaryRequest;
import ai.unit.test.spike.response.SalaryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SalaryServiceTest {

    @Mock
    private WorkService workService;

    @InjectMocks
    private SalaryService salaryService;

    @BeforeEach
    void setUp() {
        // Mockito自动初始化mock对象
    }

    @Test
    void calculate_FactoryWorkerWithOvertime_ShouldCalculateCorrectly() {
        SalaryRequest salaryRequest = new SalaryRequest();
        salaryRequest.setBaseSalaryRate(1000);
        salaryRequest.setEmployeeTYpe(EmployeeType.FACTORY_WORKER);
        salaryRequest.setOverTimeHours(10);
        salaryRequest.setSeniority(3);
        salaryRequest.setWorkDays(20);

        when(workService.get(1L)).thenReturn(salaryRequest);

        SalaryResponse salaryResponse = salaryService.calculate(1L);

        assertThat(salaryResponse.getBaseSalary()).isEqualTo(909);
        assertThat(salaryResponse.getOverTimeSalary()).isEqualTo(182);
        assertThat(salaryResponse.getSenioritySalary()).isEqualTo(20);
        assertThat(salaryResponse.getAttendanceSalary()).isEqualTo(0);
        assertThat(salaryResponse.getSalesCommissionSalary()).isEqualTo(0);
        assertThat(salaryResponse.getTotalSalary()).isEqualTo(1111);
    }

    @Test
    void calculate_SalesWithoutOvertime_ShouldCalculateCorrectly() {
        SalaryRequest salaryRequest = new SalaryRequest();
        salaryRequest.setBaseSalaryRate(1000);
        salaryRequest.setEmployeeTYpe(EmployeeType.SALES);
        salaryRequest.setOverTimeHours(0);
        salaryRequest.setSalesRevenue(5000);
        salaryRequest.setSeniority(1);
        salaryRequest.setWorkDays(22);

        when(workService.get(2L)).thenReturn(salaryRequest);

        SalaryResponse salaryResponse = salaryService.calculate(2L);

        assertThat(salaryResponse.getBaseSalary()).isEqualTo(1000);
        assertThat(salaryResponse.getOverTimeSalary()).isEqualTo(0);
        assertThat(salaryResponse.getSenioritySalary()).isEqualTo(10);
        assertThat(salaryResponse.getAttendanceSalary()).isEqualTo(100);
        assertThat(salaryResponse.getSalesCommissionSalary()).isEqualTo(5000);
        assertThat(salaryResponse.getTotalSalary()).isEqualTo(7010);
    }

    @Test
    void calculate_OfficeAssistantWithAttendance_ShouldCalculateCorrectly() {
        SalaryRequest salaryRequest = new SalaryRequest();
        salaryRequest.setBaseSalaryRate(1500);
        salaryRequest.setEmployeeTYpe(EmployeeType.OFFICE_ASSISTANT);
        salaryRequest.setOverTimeHours(0);
        salaryRequest.setSeniority(5);
        salaryRequest.setWorkDays(22);

        when(workService.get(3L)).thenReturn(salaryRequest);

        SalaryResponse salaryResponse = salaryService.calculate(3L);

        assertThat(salaryResponse.getBaseSalary()).isEqualTo(1500);
        assertThat(salaryResponse.getOverTimeSalary()).isEqualTo(0);
        assertThat(salaryResponse.getSenioritySalary()).isEqualTo(30);
        assertThat(salaryResponse.getAttendanceSalary()).isEqualTo(100);
        assertThat(salaryResponse.getSalesCommissionSalary()).isEqualTo(0);
        assertThat(salaryResponse.getTotalSalary()).isEqualTo(1730);
    }

    @Test
    void calculate_QualityControlWithOvertimeAndAttendance_ShouldCalculateCorrectly() {
        SalaryRequest salaryRequest = new SalaryRequest();
        salaryRequest.setBaseSalaryRate(2000);
        salaryRequest.setEmployeeTYpe(EmployeeType.QUALITY_CONTROL);
        salaryRequest.setOverTimeHours(5);
        salaryRequest.setSeniority(7);
        salaryRequest.setWorkDays(22);

        when(workService.get(4L)).thenReturn(salaryRequest);

        SalaryResponse salaryResponse = salaryService.calculate(4L);

        assertThat(salaryResponse.getBaseSalary()).isEqualTo(2000);
        assertThat(salaryResponse.getOverTimeSalary()).isEqualTo(188);
        assertThat(salaryResponse.getSenioritySalary()).isEqualTo(30);
        assertThat(salaryResponse.getAttendanceSalary()).isEqualTo(100);
        assertThat(salaryResponse.getSalesCommissionSalary()).isEqualTo(0);
        assertThat(salaryResponse.getTotalSalary()).isEqualTo(2418);
    }
}
```

#### 评价：

```markdown
* [成功][严重]正确使用JUnit框架版本: 明确告知JUnit5版本后正确。
* [成功][次要]正确使用AssertJ框架：明确告知断言使用AssertJ框架后正确，比百度Comate更强的是能推理出直接用assertThat方法。
* [失败][严重]正确地使用Mock框架：构建脚本中并没有mockito-junit-jupiter依赖，所以mock初始化失败，也导致无法编译。
* [成功][次要]生成单测可自动放在对应的文件目录: 点击采纳按钮后能自动放在对应的测试目录。
* [失败][严重]生成的单测可编译: 生成了不存在的类应用。
* [失败][严重]生成的单测可执行成功: 无法编译则无法执行成功。
* [成功][严重]正确地使用断言：断言比较齐全。
* [失败][重要]生成的单测测试方法名可读性高: 测试方法命名有一定规律，感觉像是某种规范，也许是阿里内部测试代码的命名规范，还将就。
* [成功][次要]没有重复的测试：没有重复的测试。
* [失败][重要]测试覆盖率高: 测试失败无测试覆盖率。
```

### 第三次Spike
尝试修复单测无法编译的问题，修改提示词为：“请使用JUnit5、AssertJ和Mockito框架并使用MockitoAnnotations.initMocks(this)初始化。”

生成结果如下：

```java
package ai.unit.test.spike.service;

import ai.unit.test.spike.domain.EmployeeType;
import ai.unit.test.spike.request.SalaryRequest;
import ai.unit.test.spike.response.SalaryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class SalaryServiceTest {

    @Mock
    private WorkService workService;

    @InjectMocks
    private SalaryService salaryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void calculate_FactoryWorkerWithOvertime_ShouldCalculateCorrectly() {
        SalaryRequest salaryRequest = new SalaryRequest();
        salaryRequest.setBaseSalaryRate(1000);
        salaryRequest.setEmployeeTYpe(EmployeeType.FACTORY_WORKER);
        salaryRequest.setOverTimeHours(10);
        salaryRequest.setSeniority(3);
        salaryRequest.setWorkDays(20);

        when(workService.get(1L)).thenReturn(salaryRequest);

        SalaryResponse salaryResponse = salaryService.calculate(1L);

        assertThat(salaryResponse.getBaseSalary()).isEqualTo(909);
        assertThat(salaryResponse.getOverTimeSalary()).isEqualTo(182);
        assertThat(salaryResponse.getSenioritySalary()).isEqualTo(20);
        assertThat(salaryResponse.getAttendanceSalary()).isEqualTo(0);
        assertThat(salaryResponse.getSalesCommissionSalary()).isEqualTo(0);
        assertThat(salaryResponse.getTotalSalary()).isEqualTo(1111);
    }

    @Test
    void calculate_SalesWithoutOvertime_ShouldCalculateCorrectly() {
        SalaryRequest salaryRequest = new SalaryRequest();
        salaryRequest.setBaseSalaryRate(1000);
        salaryRequest.setEmployeeTYpe(EmployeeType.SALES);
        salaryRequest.setOverTimeHours(0);
        salaryRequest.setSalesRevenue(5000);
        salaryRequest.setSeniority(1);
        salaryRequest.setWorkDays(22);

        when(workService.get(2L)).thenReturn(salaryRequest);

        SalaryResponse salaryResponse = salaryService.calculate(2L);

        assertThat(salaryResponse.getBaseSalary()).isEqualTo(1000);
        assertThat(salaryResponse.getOverTimeSalary()).isEqualTo(0);
        assertThat(salaryResponse.getSenioritySalary()).isEqualTo(10);
        assertThat(salaryResponse.getAttendanceSalary()).isEqualTo(100);
        assertThat(salaryResponse.getSalesCommissionSalary()).isEqualTo(5000);
        assertThat(salaryResponse.getTotalSalary()).isEqualTo(7010);
    }

    @Test
    void calculate_OfficeAssistantWithAttendance_ShouldCalculateCorrectly() {
        SalaryRequest salaryRequest = new SalaryRequest();
        salaryRequest.setBaseSalaryRate(1500);
        salaryRequest.setEmployeeTYpe(EmployeeType.OFFICE_ASSISTANT);
        salaryRequest.setOverTimeHours(0);
        salaryRequest.setSeniority(5);
        salaryRequest.setWorkDays(22);

        when(workService.get(3L)).thenReturn(salaryRequest);

        SalaryResponse salaryResponse = salaryService.calculate(3L);

        assertThat(salaryResponse.getBaseSalary()).isEqualTo(1500);
        assertThat(salaryResponse.getOverTimeSalary()).isEqualTo(0);
        assertThat(salaryResponse.getSenioritySalary()).isEqualTo(30);
        assertThat(salaryResponse.getAttendanceSalary()).isEqualTo(100);
        assertThat(salaryResponse.getSalesCommissionSalary()).isEqualTo(0);
        assertThat(salaryResponse.getTotalSalary()).isEqualTo(1730);
    }

    @Test
    void calculate_QualityControlWithOvertimeAndAttendance_ShouldCalculateCorrectly() {
        SalaryRequest salaryRequest = new SalaryRequest();
        salaryRequest.setBaseSalaryRate(2000);
        salaryRequest.setEmployeeTYpe(EmployeeType.QUALITY_CONTROL);
        salaryRequest.setOverTimeHours(5);
        salaryRequest.setSeniority(7);
        salaryRequest.setWorkDays(22);

        when(workService.get(4L)).thenReturn(salaryRequest);

        SalaryResponse salaryResponse = salaryService.calculate(4L);

        assertThat(salaryResponse.getBaseSalary()).isEqualTo(2000);
        assertThat(salaryResponse.getOverTimeSalary()).isEqualTo(188);
        assertThat(salaryResponse.getSenioritySalary()).isEqualTo(30);
        assertThat(salaryResponse.getAttendanceSalary()).isEqualTo(100);
        assertThat(salaryResponse.getSalesCommissionSalary()).isEqualTo(0);
        assertThat(salaryResponse.getTotalSalary()).isEqualTo(2418);
    }
}
```

#### 评价：

```markdown
* [成功][严重]正确使用JUnit框架版本: 明确告知JUnit5版本后正确。
* [成功][次要]正确使用AssertJ框架：明确告知断言使用AssertJ框架后正确，比百度Comate更强的是能推理出直接用assertThat方法。
* [成功][严重]正确地使用Mock框架：明确告知使用MockitoAnnotations.initMocks(this)初始化后成功。
* [成功][次要]生成单测可自动放在对应的文件目录: 点击采纳按钮后能自动放在对应的测试目录。
* [成功][严重]生成的单测可编译: 修复依赖问题后可编译。
* [失败][严重]生成的单测可执行成功: 生成的4个单测全部执行失败。
* [成功][严重]正确地使用断言：断言比较齐全。
* [失败][重要]生成的单测测试方法名可读性高: 测试方法命名有一定规律，感觉像是某种规范，也许是阿里内部测试代码的命名规范，还将就。
* [成功][次要]没有重复的测试：没有重复的测试。
* [失败][重要]测试覆盖率高: 测试失败无测试覆盖率。
```

### 第四次Spike
尝试修复生成的单测不可执行成功的问题，思路是提前告之AI测试名称、测试数据和断言结果，一次仅生成一个单测方法，提示词如下：

```markdown
请按以下要求生成单元测试：
1. 请使用JUnit5、AssertJ和Mockito框架并使用MockitoAnnotations.initMocks(this)初始化。
2. 请生成一个测试方法，该方法的名称为should_calculate_salary_for_office_assistant_with_base_salary_and_seniority_salary_and_attendance_salary，并且给该测试方法加上注释如下：测试意图：文员助理仅含基本考勤工资+工龄工资+全勤奖。
3. 该测试方法使用以下测试数据，userId为801，salaryRequest中的数据分别是，baseSalaryRate为4400，workDays为22，overTimeHours为0，employeeTYpe为OFFICE_ASSISTANT，seniority为3，salesRevenue为0。 
4. 请使用以下断言结果：baseSalary为4400，overTimeSalary为0，senioritySalary为16，attendanceSalary为100，salesCommissionSalary为0。
```

生成结果如下：

```java
package ai.unit.test.spike.service;

import ai.unit.test.spike.domain.EmployeeType;
import ai.unit.test.spike.request.SalaryRequest;
import ai.unit.test.spike.response.SalaryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

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
}
```

#### 评价：

只要Prompt提示语正确，生成的单测就正确执行了，与百度Comate效果相当。

```markdown
* [成功][严重]正确使用JUnit框架版本: 明确告知JUnit5版本后正确。
* [成功][次要]正确使用AssertJ框架：明确告知断言使用AssertJ框架后正确，比百度Comate更强的是能推理出直接用assertThat方法。
* [成功][严重]正确地使用Mock框架：明确告知使用MockitoAnnotations.initMocks(this)初始化后成功。
* [成功][次要]生成单测可自动放在对应的文件目录: 点击采纳按钮后能自动放在对应的测试目录。
* [成功][严重]生成的单测可编译: 修复依赖问题后可编译。
* [成功][严重]生成的单测可执行成功: 执行成功，Bingo！
* [成功][严重]正确地使用断言：断言比较齐全。
* [成功][重要]生成的单测测试方法名可读性高: 提前设定好测试方法名或者通过测试方法注释说明测试意图。
* [成功][次要]没有重复的测试：没有重复的测试。
* [失败][重要]测试覆盖率高: 不能一次性达成100%的测试覆盖率，即便是把所有测试案例提前输入进去大概率也会遇到内容截断的问题。
```

### 第五次Spike
尝试去除import和类头部分的代码，仅生成测试方法以实现增量单测生成。提示词如下：

```markdown
请按以下要求生成单元测试：
1. 请使用JUnit5、AssertJ和Mockito框架并使用MockitoAnnotations.initMocks(this)初始化。
2. 请生成一个测试方法，该方法的名称为should_calculate_salary_for_office_assistant_with_partial_base_salary_and_full_seniority_salary_and_without_attendance_salary，并且给该测试方法加上注释如下：测试意图：文员助理薪资=部分基本考勤工资+顶格工龄工资。
3. 该测试方法使用以下测试数据，userId为802，Mock对象salaryRequest中的数据分别是，baseSalaryRate为4400，workDays为20，overTimeHours为0，employeeTYpe为OFFICE_ASSISTANT，seniority为11，salesRevenue为0。
4. 断言结果：baseSalary为4000，overTimeSalary为0，senioritySalary为30，attendanceSalary为0，salesCommissionSalary为0。
5. 生成后仅保留测试方法的内容。
```

生成的结果如下：

```java
    /**
 * 测试意图：文员助理薪资=部分基本考勤工资+顶格工龄工资。
 */
@Test
    void should_calculate_salary_for_office_assistant_with_partial_base_salary_and_full_seniority_salary_and_without_attendance_salary() {
            // 测试数据
            SalaryRequest salaryRequest = new SalaryRequest();
            salaryRequest.setBaseSalaryRate(4400);
            salaryRequest.setEmployeeType(EmployeeType.OFFICE_ASSISTANT);
            salaryRequest.setOverTimeHours(0);
            salaryRequest.setSeniority(11);
            salaryRequest.setSalesRevenue(0);
            salaryRequest.setWorkDays(20);

            // 模拟行为
            when(workService.get(802L)).thenReturn(salaryRequest);

            // 执行测试
            SalaryResponse salaryResponse = salaryService.calculate(802L);

            // 断言结果
            assertThat(salaryResponse.getBaseSalary()).isEqualTo(4000); // 4400 / 22 * 20
            assertThat(salaryResponse.getOverTimeSalary()).isEqualTo(0);
            assertThat(salaryResponse.getSenioritySalary()).isEqualTo(30); // (11 - 1) * 3 + 10
            assertThat(salaryResponse.getAttendanceSalary()).isEqualTo(0);
            assertThat(salaryResponse.getSalesCommissionSalary()).isEqualTo(0);
            }
```

#### 评价：

```markdown
* [成功][严重]正确使用JUnit框架版本: 明确告知JUnit5版本后正确。
* [成功][次要]正确使用AssertJ框架：明确告知断言使用AssertJ框架后正确，比百度Comate更强的是能推理出直接用assertThat方法。
* [成功][严重]正确地使用Mock框架：明确告知使用MockitoAnnotations.initMocks(this)初始化后成功。
* [成功][次要]生成单测可自动放在对应的文件目录: 点击采纳按钮后能自动放在对应的测试目录。
* [成功][严重]生成的单测可编译: 修复依赖问题后可编译。
* [成功][严重]生成的单测可执行成功: 能够增量生成单测，Bingo！
* [成功][严重]正确地使用断言：断言比较齐全。
* [成功][重要]生成的单测测试方法名可读性高: 提前设定好测试方法名或者通过测试方法注释说明测试意图。
* [成功][次要]没有重复的测试：没有重复的测试。
* [成功][重要]测试覆盖率高: 以此下去，测试覆盖率能够达到100%。
```

### 第六次Spike
补全所有测试案例，达成测试覆盖率100%，提示词如下：

```markdown
请按以下要求生成单元测试：
1. 请使用JUnit5、AssertJ和Mockito框架并使用MockitoAnnotations.initMocks(this)初始化。
2. 请生成一个测试方法，该方法的名称为should_calculate_salary_for_factory_worker_with_base_salary_and_overtime_salary_and_seniority_salary_and_attendance_salary，并且给该测试方法加上注释：测试意图：车间薪资=基本考勤工资+加班工资+工龄工资+全勤奖。
3. 该测试方法使用以下测试数据，userId为803，Mock对象salaryRequest中的数据分别是，baseSalaryRate为3520，workDays为22，overTimeHours为13，employeeTYpe为FACTORY_WORKER，seniority为1，salesRevenue为0。 
4. 断言结果：baseSalary为3520，overTimeSalary为390，senioritySalary为10，attendanceSalary为100，salesCommissionSalary为0。
5. 生成后仅保留测试方法的内容。
```

生成的结果如下：

```java
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
```

```markdown
请按以下要求生成单元测试：
1. 请使用JUnit5、AssertJ和Mockito框架并使用MockitoAnnotations.initMocks(this)初始化。
2. 请生成一个测试方法，该方法的名称为should_calculate_salary_for_factory_worker_with_partial_base_salary_and_overtime_salary_and_seniority_salary_and_without_attendance_salary，并且给该测试方法加上注释：测试意图：车间薪资=部分考勤工资+加班工资+工龄工资。
3. 该测试方法使用以下测试数据，userId为804，Mock对象salaryRequest中的数据分别是，baseSalaryRate为3520，workDays为19，overTimeHours为6，employeeTYpe为FACTORY_WORKER，seniority为7，salesRevenue为0。
4. 断言结果：baseSalary为3040，overTimeSalary为180，senioritySalary为28，attendanceSalary为0，salesCommissionSalary为0。
5. 生成后仅保留测试方法的内容。
```

#### 评价：

多轮对话后，通义灵码也没有像百度Comate那样出现丢失上下文的问题，非常棒。

生成的结果如下：

```java
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
```

```markdown
请按以下要求生成单元测试：
1. 请使用JUnit5、AssertJ和Mockito框架并使用MockitoAnnotations.initMocks(this)初始化。
2. 请生成一个测试方法，该方法的名称为should_calculate_salary_for_quality_control_with_base_salary_and_seniority_salary_and_attendance_salary，并且给该测试方法加上注释：测试意图：质检技术人员薪资=基本考勤工资+工龄工资+全勤奖。
3. 该测试方法使用以下测试数据，userId为805，Mock对象salaryRequest中的数据分别是，baseSalaryRate为5280，workDays为22，overTimeHours为0，employeeTYpe为QUALITY_CONTROL，seniority为5，salesRevenue为0。
4. 断言结果：baseSalary为5280，overTimeSalary为0，senioritySalary为22，attendanceSalary为100，salesCommissionSalary为0。
5. 生成后仅保留测试方法的内容。
```

生成的结果如下：

```java
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
```

```markdown
请按以下要求生成单元测试：
1. 请使用JUnit5、AssertJ和Mockito框架并使用MockitoAnnotations.initMocks(this)初始化。
2. 请生成一个测试方法，该方法的名称为should_calculate_salary_for_sales_with_base_salary_and_seniority_salary_and_attendance_salary_and_sales_commission_salary
   ，并且给该测试方法加上注释：测试意图：销售人员薪资=基本考勤工资+工龄工资+全勤奖+提成。
3. 该测试方法使用以下测试数据，userId为806，Mock对象salaryRequest中的数据分别是，baseSalaryRate为6160，workDays为22，overTimeHours为0，employeeTYpe为SALES，seniority为1，salesRevenue为7。
4. 断言结果：baseSalary为6160，overTimeSalary为0，senioritySalary为10，attendanceSalary为100，salesCommissionSalary为1000。
5. 生成后仅保留测试方法的内容。
```

生成结果如下：

```java
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
```

```markdown
请按以下要求生成单元测试：
1. 请使用JUnit5、AssertJ和Mockito框架并使用MockitoAnnotations.initMocks(this)初始化。
2. 请生成一个测试方法，该方法的名称为should_calculate_salary_for_sales_with_base_salary_and_seniority_salary_and_attendance_salary_and_sales_commission_salary_more，并且给该测试方法加上注释：测试意图：销售人员薪资=基本考勤工资+工龄工资+全勤奖+提成。
3. 该测试方法使用以下测试数据，userId为807，Mock对象salaryRequest中的数据分别是，baseSalaryRate为6160，workDays为22，overTimeHours为0，employeeTYpe为SALES，seniority为7，salesRevenue为38。
4. 断言结果：baseSalary为6160，overTimeSalary为0，senioritySalary为28，attendanceSalary为100，salesCommissionSalary为4000。
5. 生成后仅保留测试方法的内容。
```

生成结果如下：

```java
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

```

```markdown
请按以下要求生成单元测试：
1. 请使用JUnit5、AssertJ和Mockito框架并使用MockitoAnnotations.initMocks(this)初始化。
2. 请生成一个测试方法，该方法的名称为should_calculate_salary_for_sales_with_partial_base_salary_and_full_seniority_salary_and_without_attendance_salary_and_sales_commission_salary，并且给该测试方法加上注释：测试意图：销售人员薪资=部分考勤工资+工龄工资+提成。
3. 该测试方法使用以下测试数据，userId为808，Mock对象salaryRequest中的数据分别是，baseSalaryRate为6160，workDays为21，overTimeHours为0，employeeTYpe为SALES，seniority为12，salesRevenue为54。
4. 断言结果：baseSalary为5880，overTimeSalary为0，senioritySalary为30，attendanceSalary为0，salesCommissionSalary为5000。
5. 生成后仅保留测试方法的内容。
```

生成结果如下：

```java
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
```

#### 评价：

最终测试覆盖率达到了100%，过程非常的流畅，效果比百度Comate要好很多，主要是底层模型更加的稳定，不会出现上下文丢失、测试方法内重复mock依赖对象等等问题。

### 整体总结

相比通义千问、百度Comate和腾讯AI助手，通义千问效果最佳，不管是应用层的生成单测的用户旅程体验，还是底层模型的稳定性都表现非常的出色。
