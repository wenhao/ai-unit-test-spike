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
* [失败][重要]生成的单测测试方法论可读性高: 测试方法命名并不能反应测试意图。
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
* [失败][重要]生成的单测测试方法论可读性高: 测试方法命名并不能反应测试意图。
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
* [失败][重要]生成的单测测试方法论可读性高: 测试方法命名并不能反应测试意图。
* [成功][次要]没有重复的测试：没有重复的测试。
* [失败][重要]测试覆盖率高: 测试失败无测试覆盖率。
```

第四次Spike，尝试修复单测无法编译的问题，代码内容过程被截断，只能想办法缩短生成的代码长度，可行的办法是手动提供测试的案例让AI一次只生成一个测试，那就需要更多的提示词：

```txt
请按以下要求生成单元测试：
1、请为这段代码生成单元测试；使用JUnit5、AssertJ和Mockito框架，AssertJ框架请使用如assertThat方法。
2、请使用一下测试数据生成一个测试方法，userId为801，salaryRequest中的数据分别是，baseSalaryRate为3000，workDays为22，overTimeHours为0，employeeTYpe为OFFICE_ASSISTANT，seniority为3
，salesRevenue为0。 
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
        salaryRequest.setBaseSalaryRate(3000.0);
        salaryRequest.setWorkDays(22);
        salaryRequest.setOverTimeHours(0);
        salaryRequest.setEmployeeTYpe(EmployeeTYpe.OFFICE_ASSISTANT);
        salaryRequest.setSeniority(3);
        salaryRequest.setSalesRevenue(0.0);

        when(workService.get(anyLong())).thenReturn(salaryRequest);

        // Act
        SalaryResponse salaryResponse = salaryService.calculate(userId);

        // Assert
        assertThat(salaryResponse.getBaseSalary()).isEqualTo(2200); // 3000 / 30 * 22 = 2200
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
* [失败][重要]生成的单测测试方法论可读性高: 测试方法命名并不能反应测试意图。
* [成功][次要]没有重复的测试：没有重复的测试。
* [失败][重要]测试覆盖率高: 测试失败无测试覆盖率。
```

第五次Spike，尝试西服生产的单测不可执行成功的问题，思路是提前告之AI断言结果，提示词如下：

```txt
请按以下要求生成单元测试：
1、请为这段代码生成单元测试；使用JUnit5、AssertJ和Mockito框架，AssertJ框架请使用如assertThat方法。
2、请使用一下测试数据生成一个测试方法，userId为801，salaryRequest中的数据分别是，baseSalaryRate为3000，workDays为22，overTimeHours为0，employeeTYpe为OFFICE_ASSISTANT，seniority为3
，salesRevenue为0。 
3、断言结果：baseSalary为3000，overTimeSalary为0，senioritySalary为16，attendanceSalary为100，salesCommissionSalary为0。
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
        salaryRequest.setBaseSalaryRate(3000.0);
        salaryRequest.setWorkDays(22);
        salaryRequest.setOverTimeHours(0);
        salaryRequest.setEmployeeTYpe(EmployeeTYpe.OFFICE_ASSISTANT);
        salaryRequest.setSeniority(3);
        salaryRequest.setSalesRevenue(0.0);

        when(workService.get(anyLong())).thenReturn(salaryRequest);

        // Act
        SalaryResponse salaryResponse = salaryService.calculate(userId);

        // Assert
        assertThat(salaryResponse.getBaseSalary()).isEqualTo(3000); // Assuming this is a special case
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
* [失败][重要]生成的单测测试方法论可读性高: 测试方法命名并不能反应测试意图。
* [成功][次要]没有重复的测试：没有重复的测试。
* [失败][重要]测试覆盖率高: 测试失败无测试覆盖率。
```

第六次Spike，尝试提高生成单测的方法的可读性，一种是测试的方法名能揭示测试意图，另外一种方式是在方法名上加注释说明测试意图，我们两种都要，因为复杂度类似，提示词如下：

```txt
请按以下要求生成单元测试：
1、请为这段代码生成单元测试；使用JUnit5、AssertJ和Mockito框架，AssertJ框架请使用如assertThat方法。
2、请生成一个测试方法，该方法的名称为should_calculate_salary_for_office_assistant_with_base_salary_and_seniority_salary_and_attendance_salary
，并且给该测试方法加上注释如下：测试意图：文员助理仅含基本考勤工资+工龄工资+全勤奖。
2、该测试方法使用以下测试数据，userId为801，salaryRequest中的数据分别是，baseSalaryRate为3000，workDays为22，overTimeHours为0，employeeTYpe为OFFICE_ASSISTANT，seniority为3
，salesRevenue为0。 
3、断言结果：baseSalary为3000，overTimeSalary为0，senioritySalary为16，attendanceSalary为100，salesCommissionSalary为0。
```

发现多轮对话后AI出现了上下文缺失的问题，所以猜测Baidu Comate会生成单测时会把方法的类及类的引用都提前输入给AI，具体的过程可以通过工具自动化完成，故在此我们不深究。

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
* [成功][重要]生成的单测测试方法论可读性高: 提前设定好测试方法名或者通过测试方法注释说明测试意图。
```

### 阶段性总结

1. 想要生成可执行、可读、可维护的单测，需要提前设计单测方法名称、测试输入和测试输出。
2. 需要明确的告知AI生成单测需要依赖的框架，JUnit、AssertJ、Mockito等。
3. 一次仅生成一个单测方法。
4. 需要探究和测试AI的极限，如何时输出内容截断、上下文丢失等问题。

以上问题的解决对AI生成单测有很重要的指导意义，但后续还需要结合场景比如增量单测如何处理，修改了业务代码的单测如何跟着更改等等。
