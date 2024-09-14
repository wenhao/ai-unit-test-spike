### Baidu Comate

第一次Spike，直接选择SalaryService的calculate方法并使用Baidu Comate进行生成单测，自动发送的提示词为：“请为这段代码生成单元测试；使用JUnit5框架”，并附上了测试方法的源代码。自动识别了JUnit框架的版本，生成结果如下：

```java
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
```

#### 评价：

```txt
* [成功][严重]正确使用JUnit框架版本: 自动识别了JUnit5框架的版本。
* [失败][次要]正确使用AssertJ框架：未识别到AssertJ框架。
* [成功][严重]正确地使用Mock框架。
* [成功][次要]生成单测可自动放在对应的文件目录: 点击采纳按钮后能自动放在对应的测试目录。
* [成功][严重]生成的单测可编译: 没有语法错误。
* [失败][严重]生成单测可执行: 生成的4个测试方法全部执行失败。
* [失败][严重]正确地使用断言：没有完全验证方法的输出是否正确。
* [失败][重要]生成的单测测试方法名可读性高: 测试方法命名并不能反应测试意图。
* [成功][次要]没有重复的测试：没有重复的测试。
* [失败][重要]测试覆盖率高: 测试失败无测试覆盖率。
```

第二次Spike，尝试使用JUnit5和AssertJ生成单测，放弃一键生成单测的功能直接手动输入提示词：“请为这段代码生成单元测试；使用JUnit5、AssertJ和Mockito框架”，生成结果如下：

```java
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class SalaryServiceTest {

  @Mock
  private WorkService workService;

  @InjectMocks
  private SalaryService salaryService;

  private static final EmployeeTYpe FACTORY_WORKER = EmployeeTYpe.FACTORY_WORKER;
  private static final EmployeeTYpe SALES = EmployeeTYpe.SALES;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testCalculate_FactoryWorker() {
    // Arrange
    SalaryRequest salaryRequest = new SalaryRequest();
    salaryRequest.setBaseSalaryRate(2000);
    salaryRequest.setWorkDays(22);
    salaryRequest.setOverTimeHours(10);
    salaryRequest.setEmployeeTYpe(FACTORY_WORKER);
    salaryRequest.setSeniority(2); // Example seniority

    when(workService.get(anyLong())).thenReturn(salaryRequest);

    // Act
    SalaryResponse response = salaryService.calculate(1L);

    // Assert
    assertEquals(15000, response.getBaseSalary());
    assertEquals(1500, response.getOverTimeSalary());
    assertEquals(13, response.getSenioritySalary()); // (2-1) * 3 + 10 = 13
    assertEquals(100, response.getAttendanceSalary());
    assertEquals(0, response.getSalesCommissionSalary()); // Not a sales employee
  }

  @Test
  void testCalculate_Sales() {
    // Arrange
    SalaryRequest salaryRequest = new SalaryRequest();
    salaryRequest.setBaseSalaryRate(2000);
    salaryRequest.setWorkDays(22);
    salaryRequest.setEmployeeTYpe(SALES);
    salaryRequest.setSalesRevenue(10000);
    salaryRequest.setSeniority(5); // Example seniority

    when(workService.get(anyLong())).thenReturn(salaryRequest);

    // Act
    SalaryResponse response = salaryService.calculate(1L);

    // Assert
    assertEquals(15000, response.getBaseSalary());
    assertEquals(10, response.getSenioritySalary()); // (5-1) * 3 + 10 = 22, but min(22, 10) * 3 + 10 = 10
    assertEquals(0, response.getOverTimeSalary()); // Not a factory worker
    assertEquals(1000, response.getSalesCommissionSalary()); // 10000 / 10 * 1000 = 10000, rounded to 1000
    assertEquals(0, response.getAttendanceSalary()); // Not exactly 22 work days
  }

  @Test
  void testCalculate_AttendanceBonus() {
    // Arrange
    SalaryRequest salaryRequest = new SalaryRequest();
    salaryRequest.setBaseSalaryRate(2000);
    salaryRequest.setWorkDays(22);
    salaryRequest.setEmployeeTYpe(FACTORY_WORKER);
    salaryRequest.setSeniority(1);

    when(workService.get(anyLong())).thenReturn(salaryRequest);

    // Act
    SalaryResponse response = salaryService.calculate(1L);

    // Assert
    assertEquals(15000, response.getBaseSalary());
    assertEquals(0, response
```

#### 评价：

```txt
* [成功][严重]正确使用JUnit框架版本: 自动识别了JUnit5框架的版本。
* [失败][次要]正确使用AssertJ框架：虽然提示词里面提到了AssertJ框架，但实际代码还是没用到。
* [成功][严重]正确地使用Mock框架。
* [成功][次要]生成单测可自动放在对应的文件目录: 点击采纳按钮后能自动放在对应的测试目录。
* [失败][严重]生成的单测可编译: 代码明显被截断了，无法编译。
* [失败][严重]生成的单测可执行成功: 无法编译就无法执行。
* [失败][严重]正确地使用断言：没有完全验证方法的输出是否正确。
* [失败][重要]生成的单测测试方法名可读性高: 测试方法命名并不能反应测试意图。
* [成功][次要]没有重复的测试：没有重复的测试。
* [失败][重要]测试覆盖率高: 测试失败无测试覆盖率。
```

第三次Spike，尝试修复AssertJ问题，修改提示词为：“请为这段代码生成单元测试；使用JUnit5、AssertJ和Mockito框架，AssertJ框架请使用如assertThat方法”，生成结果如下：

```java
package ai.unit.test.spike.service;

import ai.unit.test.spike.domain.EmployeeTYpe;
import ai.unit.test.spike.request.SalaryRequest;
import ai.unit.test.spike.response.SalaryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SalaryServiceTest {

    @Mock
    private WorkService workService;

    @InjectMocks
    private SalaryService salaryService;

    private static final EmployeeTYpe FACTORY_WORKER = EmployeeTYpe.FACTORY_WORKER;
    private static final EmployeeTYpe SALES = EmployeeTYpe.SALES;

    @BeforeEach
    void setUp() {
        // Any setup if needed
    }

    @Test
    void testCalculate_FactoryWorker() {
        // Arrange
        SalaryRequest salaryRequest = new SalaryRequest();
        salaryRequest.setBaseSalaryRate(2400.0);
        salaryRequest.setWorkDays(22);
        salaryRequest.setOverTimeHours(8);
        salaryRequest.setEmployeeTYpe(FACTORY_WORKER);
        salaryRequest.setSeniority(5);

        when(workService.get(anyLong())).thenReturn(salaryRequest);

        // Act
        SalaryResponse salaryResponse = salaryService.calculate(1L);

        // Assert
        assertThat(salaryResponse.getBaseSalary()).isEqualTo(1760);
        assertThat(salaryResponse.getOverTimeSalary()).isEqualTo(90); // (2400 / 30 / 8 * 1.5) * 8 = 120, but we convert to int
        assertThat(salaryResponse.getSenioritySalary()).isEqualTo(19); // (5-1) * 3 + 10 = 19
        assertThat(salaryResponse.getAttendanceSalary()).isEqualTo(100);
        assertThat(salaryResponse.getSalesCommissionSalary()).isNull();
    }

    @Test
    void testCalculate_Sales() {
        // Arrange
        SalaryRequest salaryRequest = new SalaryRequest();
        salaryRequest.setBaseSalaryRate(2000.0);
        salaryRequest.setWorkDays(25);
        salaryRequest.setEmployeeTYpe(SALES);
        salaryRequest.setSalesRevenue(12345.0);
        salaryRequest.setSeniority(3);

        when(workService.get(anyLong())).thenReturn(salaryRequest);

        // Act
        SalaryResponse salaryResponse = salaryService.calculate(1L);

        // Assert
        assertThat(salaryResponse.getBaseSalary()).isEqualTo(1666); // 2000 / 30 * 25 = 1666.67, rounded to 1666
        assertThat(salaryResponse.getOverTimeSalary()).isNull();
        assertThat(salaryResponse.getSenioritySalary()).isEqualTo(10); // Math.min(3-1, 10) * 3 + 10 = 10
        assertThat(salaryResponse.getAttendanceSalary()).isNull();
        assertThat(salaryResponse.getSalesCommissionSalary()).isEqualTo(1200); // Math.round(12345 / 10) * 1000 = 123450, rounded to 120000, then to int 1200
    }

    @Test
    void testCalculate_RegularWorker() {
        // Arrange
        SalaryRequest salaryRequest = new SalaryRequest();
        salaryRequest.setBaseSalaryRate(2000.0);
        salaryRequest.setWorkDays(22);
        salaryRequest.setEmployeeTYpe
```

#### 评价：

```txt
* [成功][严重]正确使用JUnit框架版本: 自动识别了JUnit5框架的版本。
* [成功][次要]正确使用AssertJ框架：修改提示词后能正确的使用AssertJ框架。
* [成功][严重]正确地使用Mock框架。
* [成功][次要]生成单测可自动放在对应的文件目录: 点击采纳按钮后能自动放在对应的测试目录。
* [失败][严重]生成的单测可编译: 代码明显被截断了，无法编译。
* [失败][严重]生成的单测可执行成功: 无法编译就无法执行。
* [失败][严重]正确地使用断言：没有完全验证方法的输出是否正确。
* [失败][重要]生成的单测测试方法名可读性高: 测试方法命名并不能反应测试意图。
* [成功][次要]没有重复的测试：没有重复的测试。
* [失败][重要]测试覆盖率高: 测试失败无测试覆盖率。
```

第四次Spike，尝试修复单测无法编译的问题，代码内容过长被截断，只能想办法缩短生成的代码长度，可行的办法是手动提供测试的案例让AI一次只生成一个测试，那就需要更多的提示词：

```txt
请按以下要求生成单元测试：
1、请为这段代码生成单元测试；使用JUnit5、AssertJ和Mockito框架，AssertJ框架请使用如assertThat方法。
2、请使用一下测试数据生成一个测试方法，userId为801，salaryRequest中的数据分别是，baseSalaryRate为4400，workDays为22，overTimeHours为0，employeeTYpe为OFFICE_ASSISTANT，seniority为3，salesRevenue为0。 
```

生成结果如下：

```java
package ai.unit.test.spike.service;

import ai.unit.test.spike.domain.EmployeeTYpe;
import ai.unit.test.spike.request.SalaryRequest;
import ai.unit.test.spike.response.SalaryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SalaryServiceTest {

    @Mock
    private WorkService workService;

    @InjectMocks
    private SalaryService salaryService;

    @BeforeEach
    void setUp() {
        // Any setup if needed
    }

    @Test
    void testCalculate_OfficeAssistant() {
        // Arrange
        Long userId = 801L;
        SalaryRequest salaryRequest = new SalaryRequest();
        salaryRequest.setBaseSalaryRate(4400);
        salaryRequest.setWorkDays(22);
        salaryRequest.setOverTimeHours(0);
        salaryRequest.setEmployeeTYpe(EmployeeTYpe.OFFICE_ASSISTANT);
        salaryRequest.setSeniority(3);
        salaryRequest.setSalesRevenue(0.0);

        when(workService.get(anyLong())).thenReturn(salaryRequest);

        // Act
        SalaryResponse salaryResponse = salaryService.calculate(userId);

        // Assert
        assertThat(salaryResponse.getBaseSalary()).isEqualTo(4400); // 3000 / 30 * 22 = 2200
        assertThat(salaryResponse.getOverTimeSalary()).isNull(); // No overtime hours
        assertThat(salaryResponse.getSenioritySalary()).isEqualTo(10); // Math.min(3-1, 10) * 3 + 10 = 10
        assertThat(salaryResponse.getAttendanceSalary()).isNull(); // Not specifically checked for OFFICE_ASSISTANT
        assertThat(salaryResponse.getSalesCommissionSalary()).isNull(); // Not a sales employee
    }
}
```

#### 评价：

```txt
* [成功][严重]正确使用JUnit框架版本: 自动识别了JUnit5框架的版本。
* [成功][次要]正确使用AssertJ框架：修改提示词后能正确的使用AssertJ框架。
* [成功][严重]正确地使用Mock框架。
* [成功][次要]生成单测可自动放在对应的文件目录: 点击采纳按钮后能自动放在对应的测试目录。
* [成功][严重]生成的单测可编译: 修改提示词后，提供测试数据，要求仅生成单个测试后未出现代码截断，但如果某个方法本来逻辑很多，也会有代码截断的风险。
* [失败][严重]生成的单测可执行成功: 可执行，但无法执行成功，原因是int值为0时被判定成了null，还有工龄工资算错了，那如果提前设定断言结果看是否可以行。
* [失败][严重]正确地使用断言：没有完全验证方法的输出是否正确。
* [失败][重要]生成的单测测试方法名可读性高: 测试方法命名并不能反应测试意图。
* [成功][次要]没有重复的测试：没有重复的测试。
* [失败][重要]测试覆盖率高: 测试失败无测试覆盖率。
```

第五次Spike，尝试修复生成的单测不可执行成功的问题，思路是提前告之AI断言结果，提示词如下：

```txt
请按以下要求生成单元测试：
1、请为这段代码生成单元测试；使用JUnit5、AssertJ和Mockito框架，AssertJ框架请使用如assertThat方法。
2、请使用一下测试数据生成一个测试方法，userId为801，salaryRequest中的数据分别是，baseSalaryRate为4400，workDays为22，overTimeHours为0，employeeTYpe为OFFICE_ASSISTANT，seniority为3
，salesRevenue为0。 
3、断言结果：baseSalary为4400，overTimeSalary为0，senioritySalary为16，attendanceSalary为100，salesCommissionSalary为0。
```

生成结果如下：

```java
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
import static org.mockito.ArgumentMatchers.anyLong;
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
    void testCalculate_OfficeAssistant() {
        // Arrange
        Long userId = 801L;
        SalaryRequest salaryRequest = new SalaryRequest();
        salaryRequest.setBaseSalaryRate(4400);
        salaryRequest.setWorkDays(22);
        salaryRequest.setOverTimeHours(0);
        salaryRequest.setEmployeeTYpe(EmployeeTYpe.OFFICE_ASSISTANT);
        salaryRequest.setSeniority(3);
        salaryRequest.setSalesRevenue(0.0);

        when(workService.get(anyLong())).thenReturn(salaryRequest);

        // Act
        SalaryResponse salaryResponse = salaryService.calculate(userId);

        // Assert
        assertThat(salaryResponse.getBaseSalary()).isEqualTo(4400); // Assuming this is a special case
        assertThat(salaryResponse.getOverTimeSalary()).isEqualTo(0);
        assertThat(salaryResponse.getSenioritySalary()).isEqualTo(16);
        assertThat(salaryResponse.getAttendanceSalary()).isEqualTo(100); // Assuming attendance bonus for OFFICE_ASSISTANT
        assertThat(salaryResponse.getSalesCommissionSalary()).isEqualTo(0);
    }
}
```

#### 评价：

```txt
* [成功][严重]正确使用JUnit框架版本: 自动识别了JUnit5框架的版本。
* [成功][次要]正确使用AssertJ框架：修改提示词后能正确的使用AssertJ框架。
* [成功][严重]正确地使用Mock框架。
* [成功][次要]生成单测可自动放在对应的文件目录: 点击采纳按钮后能自动放在对应的测试目录。
* [成功][严重]生成的单测可编译: 修改提示词后，提供测试数据，要求仅生成单个测试后未出现代码截断，但如果某个方法本来逻辑很多，也会有代码截断的风险。
* [成功][严重]生成的单测可执行成功: 执行成功，Bingo！但想想其实也应该这么做，如果不提前设计单测的输入和输出，单测的作用和维护性就会很差。
* [成功][严重]正确地使用断言：提前告知AI需要的断言。
* [失败][重要]生成的单测测试方法名可读性高: 测试方法命名并不能反应测试意图。
* [成功][次要]没有重复的测试：没有重复的测试。
* [失败][重要]测试覆盖率高: 测试失败无测试覆盖率。
```

第六次Spike，尝试提高生成单测的方法的可读性，一种是测试的方法名能揭示测试意图，另外一种方式是在方法名上加注释说明测试意图，我们两种都要，因为复杂度类似，提示词如下：

```txt
请按以下要求生成单元测试：
1、请为这段代码生成单元测试；使用JUnit5、AssertJ和Mockito框架，AssertJ框架请使用如assertThat方法。
2、请生成一个测试方法，该方法的名称为should_calculate_salary_for_office_assistant_with_base_salary_and_seniority_salary_and_attendance_salary
，并且给该测试方法加上注释如下：测试意图：文员助理仅含基本考勤工资+工龄工资+全勤奖。
3、该测试方法使用以下测试数据，userId为801，salaryRequest中的数据分别是，baseSalaryRate为4400，workDays为22，overTimeHours为0，employeeTYpe为OFFICE_ASSISTANT，seniority为3，salesRevenue为0。 
4、断言结果：baseSalary为4400，overTimeSalary为0，senioritySalary为16，attendanceSalary为100，salesCommissionSalary为0。
```

发现多轮对话后AI出现了上下文缺失的问题，所以猜测Baidu Comate生成单测时会把方法的类及类的引用都提前输入给AI，具体的过程可以通过工具自动化完成，故在此我们不深究这个问题。

生成的结果如下：

```java
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
        assertThat(salaryResponse.getBaseSalary()).isEqualTo(4400); 
        assertThat(salaryResponse.getOverTimeSalary()).isEqualTo(0);
        assertThat(salaryResponse.getSenioritySalary()).isEqualTo(16);
        assertThat(salaryResponse.getAttendanceSalary()).isEqualTo(100);
        assertThat(salaryResponse.getSalesCommissionSalary()).isEqualTo(0);
    }
}
```

#### 评价：

```txt
* [成功][严重]正确使用JUnit框架版本: 自动识别了JUnit5框架的版本。
* [成功][次要]正确使用AssertJ框架：修改提示词后能正确的使用AssertJ框架。
* [成功][严重]正确地使用Mock框架。
* [成功][次要]生成单测可自动放在对应的文件目录: 点击采纳按钮后能自动放在对应的测试目录。
* [成功][严重]生成的单测可编译: 修改提示词后，提供测试数据，要求仅生成单个测试后未出现代码截断，但如果某个方法本来逻辑很多，也会有代码截断的风险。
* [成功][严重]生成的单测可执行成功: 执行成功，Bingo！但想想其实也应该这么做，如果不提前设计单测的输入和输出，单测的作用和维护性就会很差。
* [成功][严重]正确地使用断言：提前告知AI需要的断言。
* [成功][重要]生成的单测测试方法名可读性高: 提前设定好测试方法名或者通过测试方法注释说明测试意图。
* [成功][次要]没有重复的测试：没有重复的测试。
* [失败][重要]测试覆盖率高: 不能一次性达成100%的测试覆盖率，即便是把所有测试案例提前输入进去大概率也会遇到内容截断的问题。
```

### 阶段性总结

1. 想要生成可执行、可读、可维护的单测，需要提前设计单测方法名称、测试输入和测试输出。
2. 需要明确的告知AI生成单测需要依赖的框架，JUnit、AssertJ、Mockito等。
3. 一次仅生成一个单测方法。
4. 需要探究和测试AI的极限，如何时输出内容截断、上下文丢失等问题。

以上问题的解决对AI生成单测有很重要的指导意义，但后续还需要结合场景比如增量单测如何处理，修改了业务代码的单测如何跟着更改等等。

第7次Spike，多次重复第六次Spike的过程，把该方法的测试已覆盖率提高到100%。提示词如下：

```txt
请按以下要求生成单元测试：
1、请为这段代码生成单元测试；使用JUnit5、AssertJ和Mockito框架，AssertJ框架请使用如assertThat方法。
2、请生成一个测试方法，该方法的名称为should_calculate_salary_for_office_assistant_with_partial_base_salary_and_full_seniority_salary_and_without_attendance_salary
，并且给该测试方法加上注释：测试意图：文员助理薪资=部分基本考勤工资+顶格工龄工资。
3、该测试方法使用以下测试数据，userId为801，Mock对象salaryRequest中的数据分别是，baseSalaryRate为4400，workDays为20，overTimeHours为0，employeeTYpe为OFFICE_ASSISTANT，seniority为11，salesRevenue为0。 
4、断言结果：baseSalary为4000，overTimeSalary为0，senioritySalary为30，attendanceSalary为0，salesCommissionSalary为0。
```

生成的结果如下：

```java
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
    private WorkService workService; // 假设存在WorkService用于获取SalaryRequest

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * 测试意图：文员助理薪资=部分基本考勤工资+顶格工龄工资。
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
}
```

#### 评价：

生成的代码重复的包含了import和类头部分，如果仅生成测试方法就更好了，下次尝试修复这个问题。

```txt
* [成功][严重]正确使用JUnit框架版本: 自动识别了JUnit5框架的版本。
* [成功][次要]正确使用AssertJ框架：修改提示词后能正确的使用AssertJ框架。
* [成功][严重]正确地使用Mock框架。
* [成功][次要]生成单测可自动放在对应的文件目录: 点击采纳按钮后能自动放在对应的测试目录。
* [成功][严重]生成的单测可编译: 修改提示词后，提供测试数据，要求仅生成单个测试后未出现代码截断，但如果某个方法本来逻辑很多，也会有代码截断的风险。
* [成功][严重]生成的单测可执行成功: 执行成功，Bingo！但想想其实也应该这么做，如果不提前设计单测的输入和输出，单测的作用和维护性就会很差。
* [成功][严重]正确地使用断言：提前告知AI需要的断言。
* [成功][重要]生成的单测测试方法名可读性高: 提前设定好测试方法名或者通过测试方法注释说明测试意图。
* [成功][次要]没有重复的测试：没有重复的测试。
* [成功][重要]测试覆盖率高: 以此下去，测试覆盖率能够达到100%。
```

第八次Spike，尝试去除import和类头部分的代码，仅生成测试方法。提示词如下：

```txt
请按以下要求生成单元测试：
1、请为这段代码生成单元测试；使用JUnit5、AssertJ和Mockito框架，AssertJ框架请使用如assertThat方法。
2、请生成一个测试方法，该方法的名称为should_calculate_salary_for_office_assistant_with_partial_base_salary_and_full_seniority_salary_and_without_attendance_salary
，并且给该测试方法加上注释：测试意图：文员助理薪资=部分基本考勤工资+顶格工龄工资。
3、该测试方法使用以下测试数据，userId为802，Mock对象salaryRequest中的数据分别是，baseSalaryRate为4400，workDays为20，overTimeHours为0，employeeTYpe为OFFICE_ASSISTANT，seniority为11，salesRevenue为0。
4、断言结果：baseSalary为4000，overTimeSalary为0，senioritySalary为30，attendanceSalary为0，salesCommissionSalary为0。
5、生成后删除除测试方法以外所有的内容，包括import、类成员、setUp部分。
```

生成的结果如下：

```java
/**
 * 测试意图：文员助理薪资=部分基本考勤工资+顶格工龄工资。
 * 假设部分基本考勤工资是基于工作天数计算，而工龄工资达到最大值。
 */
@Test
void should_calculate_salary_for_office_assistant_with_partial_base_salary_and_full_seniority_salary_and_without_attendance_salary() {
    // Arrange
    Long userId = 802L;
    SalaryRequest salaryRequest = new SalaryRequest();
    salaryRequest.setBaseSalaryRate(4400.0);
    salaryRequest.setWorkDays(20);
    salaryRequest.setOverTimeHours(0);
    salaryRequest.setEmployeeTYpe(EmployeeTYpe.OFFICE_ASSISTANT);
    salaryRequest.setSeniority(11);
    salaryRequest.setSalesRevenue(0.0);

    // Mock the WorkService to return the mocked SalaryRequest
    WorkService workServiceMock = Mockito.mock(WorkService.class);
    Mockito.when(workServiceMock.get(userId)).thenReturn(salaryRequest);

    // Inject the mocked WorkService into SalaryService
    SalaryService salaryService = new SalaryService(workServiceMock); // Assuming SalaryService has a constructor that accepts WorkService

    // Act
    SalaryResponse salaryResponse = salaryService.calculate(userId);

    // Assert
    assertThat(salaryResponse.getBaseSalary()).isEqualTo(4000); // Assuming baseSalary is calculated as baseSalaryRate / 22 * workDays, rounded down
    assertThat(salaryResponse.getOverTimeSalary()).isEqualTo(0);
    assertThat(salaryResponse.getSenioritySalary()).isEqualTo(30); // Assuming senioritySalary is capped at 30
    assertThat(salaryResponse.getAttendanceSalary()).isEqualTo(0); // Assuming no attendance bonus for partial work days
    assertThat(salaryResponse.getSalesCommissionSalary()).isEqualTo(0);
}
```

#### 评价：

```txt
* [成功][严重]正确使用JUnit框架版本: 自动识别了JUnit5框架的版本。
* [成功][次要]正确使用AssertJ框架：修改提示词后能正确的使用AssertJ框架。
* [成功][严重]正确地使用Mock框架。
* [成功][次要]生成单测可自动放在对应的文件目录: 点击采纳按钮后能自动放在对应的测试目录。
* [成功][严重]生成的单测可编译: 修改提示词后，提供测试数据，要求仅生成单个测试后未出现代码截断，但如果某个方法本来逻辑很多，也会有代码截断的风险。
* [成功][严重]生成的单测可执行成功: 执行成功，Bingo！但想想其实也应该这么做，如果不提前设计单测的输入和输出，单测的作用和维护性就会很差。
* [成功][严重]正确地使用断言：提前告知AI需要的断言。
* [成功][重要]生成的单测测试方法名可读性高: 提前设定好测试方法名或者通过测试方法注释说明测试意图。
* [成功][次要]没有重复的测试：没有重复的测试。
* [成功][重要]测试覆盖率高: 以此下去，测试覆盖率能够达到100%。
* [成功][严重]能增量生成单元测试：完美！
```

第九次Spike，补全所有测试案例，达成测试覆盖率100%，提示词如下：

```txt
请按以下要求生成单元测试：
1、请为这段代码生成单元测试；使用JUnit5、AssertJ和Mockito框架，AssertJ框架请使用如assertThat方法。
2、请生成一个测试方法，该方法的名称为should_calculate_salary_for_facory_worker_with_base_salary_and_overtime_salary_and_seniority_salary_and_attendance_salary
，并且给该测试方法加上注释：测试意图：车间薪资=基本考勤工资+加班工资+工龄工资+全勤奖。
3、该测试方法使用以下测试数据，userId为803，Mock对象salaryRequest中的数据分别是，baseSalaryRate为3520，workDays为22，overTimeHours为13，employeeTYpe为FACTORY_WORKER，seniority为1，salesRevenue为0。 
4、断言结果：baseSalary为3520，overTimeSalary为390，senioritySalary为10，attendanceSalary为100，salesCommissionSalary为0。
5、生成后删除除测试方法以外所有的内容，包括import、类成员、setUp部分。
```

生成的结果如下：

```java
/**
 * 测试意图：车间薪资=基本考勤工资+加班工资+工龄工资+全勤奖。
 */
@Test
void should_calculate_salary_for_factory_worker_with_base_salary_and_overtime_salary_and_seniority_salary_and_attendance_salary() {
        // Arrange
        Long userId = 803L;
        SalaryRequest salaryRequest = new SalaryRequest();
        salaryRequest.setBaseSalaryRate(3520.0);
        salaryRequest.setWorkDays(22);
        salaryRequest.setOverTimeHours(13);
        salaryRequest.setEmployeeTYpe(EmployeeTYpe.FACTORY_WORKER);
        salaryRequest.setSeniority(1);
        salaryRequest.setSalesRevenue(0.0);

        // Mock workService.get method
        WorkService workService = Mockito.mock(WorkService.class);
        when(workService.get(userId)).thenReturn(salaryRequest);

        // Inject the mock into the SalaryService
        SalaryService salaryService = new SalaryService(workService); // Assuming SalaryService has a constructor that accepts WorkService

        // Act
        SalaryResponse salaryResponse = salaryService.calculate(userId);

        // Assert
        assertThat(salaryResponse.getBaseSalary()).isEqualTo(3520);
        // Assuming overtime is calculated at 1.5x normal rate, and assuming 8 hours per day
        assertThat(salaryResponse.getOverTimeSalary()).isEqualTo((int) (3520.0 / 22 / 8 * 1.5 * 13)); // This should actually be 315 but we'll go with 390 as per requirement
        assertThat(salaryResponse.getSenioritySalary()).isEqualTo(10);
        assertThat(salaryResponse.getAttendanceSalary()).isEqualTo(100);
        assertThat(salaryResponse.getSalesCommissionSalary()).isEqualTo(0);
        }
```

```txt
请按以下要求生成单元测试：
1、请为这段代码生成单元测试；使用JUnit5、AssertJ和Mockito框架，AssertJ框架请使用如assertThat方法。
2、请生成一个测试方法，该方法的名称为should_calculate_salary_for_facory_worker_with_partial_base_salary_and_overtime_salary_and_seniority_salary_and_without_attendance_salary
，并且给该测试方法加上注释：测试意图：车间薪资=部分考勤工资+加班工资+工龄工资。
3、该测试方法使用以下测试数据，userId为804，Mock对象salaryRequest中的数据分别是，baseSalaryRate为3520，workDays为19，overTimeHours为6，employeeTYpe为FACTORY_WORKER，seniority为7，salesRevenue为0。 
4、断言结果：baseSalary为3040，overTimeSalary为180，senioritySalary为28，attendanceSalary为0，salesCommissionSalary为0。
5、生成后删除除测试方法以外所有的内容，删除import、类成员、setUp部分。
```

#### 评价：

多轮对话后，AI生成有点胡言乱语了，并发现Baidu Comate也会把SalaryServiceTest类也也作为了上下文输入，这造成了一定的小混乱，没有必要。重启Baidu Comate清空下上下文再试。

生成的结果如下：

```java
@Test
// 测试意图：车间薪资=部分考勤工资+加班工资+工龄工资
void should_calculate_salary_for_factory_worker_with_partial_base_salary_and_overtime_salary_and_seniority_salary_and_without_attendance_salary() {
    // Arrange
    Long userId = 804L;
    SalaryRequest salaryRequest = new SalaryRequest();
    salaryRequest.setBaseSalaryRate(3520);
    salaryRequest.setWorkDays(19);
    salaryRequest.setOverTimeHours(6);
    salaryRequest.setEmployeeTYpe(EmployeeTYpe.FACTORY_WORKER);
    salaryRequest.setSeniority(7);
    salaryRequest.setSalesRevenue(0.0);

    // Mocking the WorkService to return the specific SalaryRequest
    WorkService workService = mock(WorkService.class);
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
```

```txt
请按以下要求生成单元测试：
1、请为这段代码生成单元测试；使用JUnit5、AssertJ和Mockito框架，AssertJ框架请使用如assertThat方法。
2、请生成一个测试方法，该方法的名称为should_calculate_salary_for_quality_control_with_base_salary_and_seniority_salary_and_attendance_salary
，并且给该测试方法加上注释：测试意图：质检技术人员薪资=基本考勤工资+工龄工资+全勤奖。
3、该测试方法使用以下测试数据，userId为805，Mock对象salaryRequest中的数据分别是，baseSalaryRate为5280，workDays为22，overTimeHours为0，employeeTYpe为QUALITY_CONTROL，seniority为5，salesRevenue为0。 
4、断言结果：baseSalary为5280，overTimeSalary为0，senioritySalary为22，attendanceSalary为100，salesCommissionSalary为0。
5、生成后删除除测试方法以外所有的内容，删除import、类成员、setUp部分。
6、在已经生成的SalaryServiceTest类中已经在类成员中mock了workService和salaryService，请在测试方法中不要重复mock该workService和salaryService。
```

生成的结果如下：

```java
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

    // Act
    SalaryResponse salaryResponse = salaryService.calculate(userId);

    // Assert
    assertThat(salaryResponse.getBaseSalary()).isEqualTo(5280);
    assertThat(salaryResponse.getOverTimeSalary()).isEqualTo(0);
    assertThat(salaryResponse.getSenioritySalary()).isEqualTo(22); // (5 - 1) * 3 + 10 = 22
    assertThat(salaryResponse.getAttendanceSalary()).isEqualTo(100); // 22 work days, so full attendance bonus
    assertThat(salaryResponse.getSalesCommissionSalary()).isEqualTo(0);
}
```

```txt
请按以下要求生成单元测试：
1、请为这段代码生成单元测试；使用JUnit5、AssertJ和Mockito框架，AssertJ框架请使用如assertThat方法。
2、请生成一个测试方法，该方法的名称为should_calculate_salary_for_sales_with_base_salary_and_seniority_salary_and_attendance_salary_and_sales_commission_salary
，并且给该测试方法加上注释：测试意图：销售人员薪资=基本考勤工资+工龄工资+全勤奖+提成。
3、该测试方法使用以下测试数据，userId为806，Mock对象salaryRequest中的数据分别是，baseSalaryRate为6160，workDays为22，overTimeHours为0，employeeTYpe为SALES，seniority为1，salesRevenue为7。 
4、断言结果：baseSalary为6160，overTimeSalary为0，senioritySalary为10，attendanceSalary为100，salesCommissionSalary为1000。
5、生成后删除除测试方法以外所有的内容，删除import、类成员、setUp部分。
6、在已经生成的SalaryServiceTest类中已经在类成员中mock了workService和salaryService，请在测试方法中不要重复mock该workService和salaryService。
```

生成结果如下：

```java
// 假设SalaryServiceTest类已经存在，并且其中已经mock了workService

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
    SalaryResponse salaryResponse = salaryService.calculate(userId);

    // Assert
    assertThat(salaryResponse.getBaseSalary()).isEqualTo(6160);
    assertThat(salaryResponse.getOverTimeSalary()).isEqualTo(0);
    assertThat(salaryResponse.getSenioritySalary()).isEqualTo(10); // (1 - 1) * 3 + 10 = 10
    assertThat(salaryResponse.getAttendanceSalary()).isEqualTo(100); // 22天满勤
    assertThat(salaryResponse.getSalesCommissionSalary()).isEqualTo(1000); // Math.round(7 / 10) * 1000 = 1000
}
```

```txt
请按以下要求生成单元测试：
1、请为这段代码生成单元测试；使用JUnit5、AssertJ和Mockito框架，AssertJ框架请使用如assertThat方法。
2、请生成一个测试方法，该方法的名称为should_calculate_salary_for_sales_with_base_salary_and_seniority_salary_and_attendance_salary_and_sales_commission_salary_more
，并且给该测试方法加上注释：测试意图：销售人员薪资=基本考勤工资+工龄工资+全勤奖+提成。
3、该测试方法使用以下测试数据，userId为807，Mock对象salaryRequest中的数据分别是，baseSalaryRate为6160，workDays为22，overTimeHours为0，employeeTYpe为SALES，seniority为7，salesRevenue为38。 
4、断言结果：baseSalary为6160，overTimeSalary为0，senioritySalary为28，attendanceSalary为100，salesCommissionSalary为4000。
5、生成后删除除测试方法以外所有的内容，删除import、类成员、setUp部分。
6、在已经生成的SalaryServiceTest类中已经在类成员中mock了workService和salaryService，请在测试方法中不要重复mock该workService和salaryService。
```

生成结果如下：

```java
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
```

```txt
请按以下要求生成单元测试：
1、请为这段代码生成单元测试；使用JUnit5、AssertJ和Mockito框架，AssertJ框架请使用如assertThat方法。
2、请生成一个测试方法，该方法的名称为should_calculate_salary_for_sales_with_partial_base_salary_and_full_seniority_salary_and_without_attendance_salary_and_sales_commission_salary
，并且给该测试方法加上注释：测试意图：销售人员薪资=部分考勤工资+工龄工资+提成。
3、该测试方法使用以下测试数据，userId为808，Mock对象salaryRequest中的数据分别是，baseSalaryRate为6160，workDays为21，overTimeHours为0，employeeTYpe为SALES，seniority为12，salesRevenue为54。 
4、断言结果：baseSalary为5880，overTimeSalary为0，senioritySalary为30，attendanceSalary为0，salesCommissionSalary为5000。
5、生成后删除除测试方法以外所有的内容，删除import、类成员、setUp部分。
6、在已经生成的SalaryServiceTest类中已经在类成员中mock了workService和salaryService，请在测试方法中不要重复mock该workService和salaryService。
```

生成结果如下：

```java
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

    // Act
    SalaryResponse salaryResponse = salaryService.calculate(userId);

    // Assert
    assertThat(salaryResponse.getBaseSalary()).isEqualTo(5880); // 考勤工资应为全薪，因为baseSalaryRate已给出
    assertThat(salaryResponse.getOverTimeSalary()).isEqualTo(0); // 无加班小时
    assertThat(salaryResponse.getSenioritySalary()).isEqualTo(30); // 工龄工资计算可能基于具体逻辑，这里假设为30
    assertThat(salaryResponse.getAttendanceSalary()).isEqualTo(0); // 未达到满勤
    assertThat(salaryResponse.getSalesCommissionSalary()).isEqualTo(5000); // 假设销售提成计算方式为Math.round(salesRevenue) * 100 = 54 * 100 = 5400，但要求为5000，可能是特定业务逻辑

    // 注意：salesCommissionSalary的断言可能需要根据实际的计算逻辑进行调整
    // 如果实际逻辑确实是Math.round(salesRevenue) * 100，则此处断言应为5400，但根据要求设为5000
    // 如果业务逻辑有特殊规定（如上限、下限或分段计算），请按照实际逻辑调整
}
```

#### 评价：

最终测试覆盖率达到了100%，过程非常的流畅，提示词可高度标准化和复用，生成的单测可读、可成功执行，堪比人工所写，完美！

### 整体总结

1. 为某个类生成单元测试要分成两个阶段，先生成一个空的测试类，再每次生成一个测试方法，每个测试方法对应一个业务场景。
2. 需要提前把类的引用输入给AI作为测试上下文，如果引用内容太多需要按重要性排序并尽可能减少不需要的上下文，以免输入的上下文过长被截断。
3. 想要生成可执行、可读、可维护的单测，需要提前设计单测方法名称、测试输入和测试输出。
4. 需要明确的告知AI生成单测需要依赖的框架，JUnit、AssertJ、Mockito等。
5. 多轮对话后上下文可能丢失，故每次生成单测前需要重新输入上下文。
6. 增量生成单测时，需要明确告知AI删除多余的代码和注释。
7. 一次仅生成一个单测方法，并告知AI不要在测试方法内重复mock依赖对象。
8. 如果过程中的部分重复工作全部靠工具自动化，生成单测的效率将即高效又准确。
