### Claude AI

### 第一次Spike
将`SalaryService`文件发送给ClaudeAI，并提示生成`calculate`方法的单测。发送的提示词是：“请为这段代码生成单元测试；使用JUnit5框架”。

ClaudeAI收到提示后，开始生成单测。生成的单测如下：
```java

package ai.unit.test.spike.service;

import ai.unit.test.spike.request.SalaryRequest;
import ai.unit.test.spike.response.SalaryResponse;
import ai.unit.test.spike.domain.EmployeeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SalaryServiceTest {

    @Mock
    private WorkService workService;

    private SalaryService salaryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        salaryService = new SalaryService(workService);
    }

    @Test
    void testCalculateForFactoryWorker() {
        // Arrange
        Long userId = 1L;
        SalaryRequest request = new SalaryRequest();
        request.setEmployeeType(EmployeeType.FACTORY_WORKER);
        request.setBaseSalaryRate(22000);
        request.setWorkDays(22);
        request.setOverTimeHours(10);
        request.setSeniority(5);

        when(workService.get(userId)).thenReturn(request);

        // Act
        SalaryResponse response = salaryService.calculate(userId);

        // Assert
        assertEquals(22000, response.getBaseSalary());
        assertEquals(1875, response.getOverTimeSalary());
        assertEquals(22, response.getSenioritySalary());
        assertEquals(100, response.getAttendanceSalary());
        assertEquals(0, response.getSalesCommissionSalary());
    }

    @Test
    void testCalculateForSalesPerson() {
        // Arrange
        Long userId = 2L;
        SalaryRequest request = new SalaryRequest();
        request.setEmployeeType(EmployeeType.SALES);
        request.setBaseSalaryRate(20000);
        request.setWorkDays(20);
        request.setSeniority(2);
        request.setSalesRevenue(100000);

        when(workService.get(userId)).thenReturn(request);

        // Act
        SalaryResponse response = salaryService.calculate(userId);

        // Assert
        assertEquals(18181, response.getBaseSalary());
        assertEquals(0, response.getOverTimeSalary());
        assertEquals(13, response.getSenioritySalary());
        assertEquals(0, response.getAttendanceSalary());
        assertEquals(10000, response.getSalesCommissionSalary());
    }

    @Test
    void testCalculateWithMaxSenioritySalary() {
        // Arrange
        Long userId = 3L;
        SalaryRequest request = new SalaryRequest();
        request.setEmployeeType(EmployeeType.FACTORY_WORKER);
        request.setBaseSalaryRate(25000);
        request.setWorkDays(22);
        request.setSeniority(15);

        when(workService.get(userId)).thenReturn(request);

        // Act
        SalaryResponse response = salaryService.calculate(userId);

        // Assert
        assertEquals(25000, response.getBaseSalary());
        assertEquals(30, response.getSenioritySalary()); // Max seniority salary
    }

    @Test
    void testCalculateWithoutAttendanceBonus() {
        // Arrange
        Long userId = 4L;
        SalaryRequest request = new SalaryRequest();
        request.setEmployeeType(EmployeeType.FACTORY_WORKER);
        request.setBaseSalaryRate(20000);
        request.setWorkDays(21);
        request.setSeniority(1);

        when(workService.get(userId)).thenReturn(request);

        // Act
        SalaryResponse response = salaryService.calculate(userId);

        // Assert
        assertEquals(19090, response.getBaseSalary());
        assertEquals(10, response.getSenioritySalary());
        assertEquals(0, response.getAttendanceSalary()); // No attendance bonus
    }
}

```

### 第一次Spike的评价：

```markdown
* [成功][严重]正确使用JUnit框架版本: 自动识别了JUnit5框架的版本。
* [成功][次要]正确使用AssertJ框架：未识别到AssertJ框架。
* [成功][严重]正确地使用Mock框架。
* [不涉及][次要]生成单测可自动放在对应的文件目录: 点击采纳按钮后能自动放在对应的测试目录。
* [成功][严重]生成的单测可编译: 没有语法错误。
* [失败][严重]生成单测可执行: 但是生成的4个单测只有2个成功，2个失败。
* [成功][严重]正确地使用断言：验证了方法的全部输出。
* [成功][重要]生成的单测测试方法名可读性高: 测试方法命名反应了测试意图，但是结构上可以改善。
* [成功][次要]没有重复的测试：没有重复的测试。
* [失败][重要]测试覆盖率高: 测试失败无测试覆盖率。
```

### 第二次Spike
将`SalaryService`文件发送给ClaudeAI，并提示生成`calculate`方法的单测。发送的提示词是：“请为这段代码生成单元测试；使用JUnit5框架；请保证Assertion的正确性；测试名字使用‘given给定条件_when做了什么操作_then期望什么结果’这样的结构的英文表达”。

ClaudeAI收到提示后，开始生成单测。生成的单测如下：

```java
package ai.unit.test.spike.service;

import ai.unit.test.spike.request.SalaryRequest;
import ai.unit.test.spike.response.SalaryResponse;
import ai.unit.test.spike.domain.EmployeeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SalaryServiceTest {

    @Mock
    private WorkService workService;

    private SalaryService salaryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        salaryService = new SalaryService(workService);
    }

    @Test
    void given_factoryWorkerWithStandardConditions_when_calculateSalary_then_returnCorrectSalary() {
        // Given
        Long userId = 1L;
        SalaryRequest request = new SalaryRequest();
        request.setEmployeeType(EmployeeType.FACTORY_WORKER);
        request.setBaseSalaryRate(22000);
        request.setWorkDays(22);
        request.setOverTimeHours(10);
        request.setSeniority(5);

        when(workService.get(userId)).thenReturn(request);

        // When
        SalaryResponse response = salaryService.calculate(userId);

        // Then
        assertEquals(22000, response.getBaseSalary());
        assertEquals(1875, response.getOverTimeSalary());
        assertEquals(22, response.getSenioritySalary());
        assertEquals(100, response.getAttendanceSalary());
        assertEquals(0, response.getSalesCommissionSalary());
    }

    @Test
    void given_salesPersonWithHighRevenue_when_calculateSalary_then_returnCorrectSalary() {
        // Given
        Long userId = 2L;
        SalaryRequest request = new SalaryRequest();
        request.setEmployeeType(EmployeeType.SALES);
        request.setBaseSalaryRate(20000);
        request.setWorkDays(20);
        request.setSeniority(2);
        request.setSalesRevenue(200000);

        when(workService.get(userId)).thenReturn(request);

        // When
        SalaryResponse response = salaryService.calculate(userId);

        // Then
        assertEquals(18181, response.getBaseSalary());
        assertEquals(0, response.getOverTimeSalary());
        assertEquals(13, response.getSenioritySalary());
        assertEquals(0, response.getAttendanceSalary());
        assertEquals(20000, response.getSalesCommissionSalary());
    }

    @Test
    void given_employeeWithMaxSeniority_when_calculateSalary_then_returnMaxSenioritySalary() {
        // Given
        Long userId = 3L;
        SalaryRequest request = new SalaryRequest();
        request.setEmployeeType(EmployeeType.FACTORY_WORKER);
        request.setBaseSalaryRate(25000);
        request.setWorkDays(22);
        request.setSeniority(15);

        when(workService.get(userId)).thenReturn(request);

        // When
        SalaryResponse response = salaryService.calculate(userId);

        // Then
        assertEquals(25000, response.getBaseSalary());
        assertEquals(30, response.getSenioritySalary());
    }

    @Test
    void given_employeeWithPartialAttendance_when_calculateSalary_then_returnNoAttendanceBonus() {
        // Given
        Long userId = 4L;
        SalaryRequest request = new SalaryRequest();
        request.setEmployeeType(EmployeeType.FACTORY_WORKER);
        request.setBaseSalaryRate(24000);
        request.setWorkDays(21);
        request.setSeniority(3);

        when(workService.get(userId)).thenReturn(request);

        // When
        SalaryResponse response = salaryService.calculate(userId);

        // Then
        assertEquals(22909, response.getBaseSalary());
        assertEquals(16, response.getSenioritySalary());
        assertEquals(0, response.getAttendanceSalary());
    }
}
```

### 第二次Spike的评价：

```markdown
* [成功][严重]正确使用JUnit框架版本: 自动识别了JUnit5框架的版本。
* [成功][次要]正确使用AssertJ框架：未识别到AssertJ框架。
* [成功][严重]正确地使用Mock框架。
* [不涉及][次要]生成单测可自动放在对应的文件目录: 点击采纳按钮后能自动放在对应的测试目录。
* [成功][严重]生成的单测可编译: 没有语法错误。
* [失败][严重]生成单测可执行: 但是生成的4个单测只有2个成功，2个失败。
* [成功][严重]正确地使用断言：验证了方法的全部输出。
* [成功][重要]生成的单测测试方法名可读性高: 测试方法命名反应了测试意图，但是结构上可以改善。
* [成功][次要]没有重复的测试：没有重复的测试。
* [失败][重要]测试覆盖率高: 测试失败无测试覆盖率。
```

> 两次Spike后，发现Claude的代码生成的正确性很高，第二次Spike结果显示命名结构清晰且能表达测试意图，但是核心的问题是其不能正确的计算现有业务代码的正确输入输出，也就是案例不正确，接下来我们将案例加入到提示词中，测试是否能够通过文字案例描述来生成正确可运行的单测代码。

### 第三次Spike
将`SalaryService`文件发送给ClaudeAI，并提示生成`calculate`方法的单测。发送的提示词是：
```markdown
请按以下要求生成这个类的单元测试类：
1. 请使用JUnit5、AssertJ和Mockito框架并使用MockitoAnnotations.initMocks(this)初始化。
2. 请生成一个测试方法，该方法的名称为should_calculate_salary_for_sales_with_partial_base_salary_and_full_seniority_salary_and_without_attendance_salary_and_sales_commission_salary，并且给该测试方法加上注释：测试意图：销售人员薪资=部分考勤工资+工龄工资+提成。
3. 该测试方法使用以下测试数据，userId为808，Mock对象salaryRequest中的数据分别是，baseSalaryRate为6160，workDays为21，overTimeHours为0，employeeTYpe为SALES，seniority为12，salesRevenue为54。
4. 断言结果：baseSalary为5880，overTimeSalary为0，senioritySalary为30，attendanceSalary为0，salesCommissionSalary为5000。
5. 生成后仅保留测试方法的内容。
```

ClaudeAI收到提示后，开始生成单测。生成的单测如下：
```java
@Test
@DisplayName("测试意图：销售人员薪资=部分考勤工资+工龄工资+提成")
void should_calculate_salary_for_sales_with_partial_base_salary_and_full_seniority_salary_and_without_attendance_salary_and_sales_commission_salary() {
    // Given
    Long userId = 808L;
    SalaryRequest salaryRequest = new SalaryRequest();
    salaryRequest.setBaseSalaryRate(6160);
    salaryRequest.setWorkDays(21);
    salaryRequest.setOverTimeHours(0);
    salaryRequest.setEmployeeType(SALES);
    salaryRequest.setSeniority(12);
    salaryRequest.setSalesRevenue(54);

    when(workService.get(userId)).thenReturn(salaryRequest);

    // When
    SalaryResponse result = salaryService.calculate(userId);

    // Then
    assertThat(result.getBaseSalary()).isEqualTo(5880);
    assertThat(result.getOverTimeSalary()).isEqualTo(0);
    assertThat(result.getSenioritySalary()).isEqualTo(30);
    assertThat(result.getAttendanceSalary()).isEqualTo(0);
    assertThat(result.getSalesCommissionSalary()).isEqualTo(5000);
}
```

### 第三次Spike的评价

* [成功][严重]正确使用JUnit框架版本: 自动识别了JUnit5框架的版本。
* [成功][次要]正确使用AssertJ框架：未识别到AssertJ框架。
* [成功][严重]正确地使用Mock框架。
* [不涉及][次要]生成单测可自动放在对应的文件目录: 点击采纳按钮后能自动放在对应的测试目录。
* [成功][严重]生成的单测可编译: 没有语法错误。
* [成功][严重]生成单测可执行: 生成的单测完全可以执行。
* [成功][严重]正确地使用断言：验证了方法的全部输出。
* [成功][重要]生成的单测测试方法名可读性高: 测试方法命名反应了测试意图，但是结构上可以改善。
* [成功][次要]没有重复的测试：没有重复的测试。
* [成功][重要]测试覆盖率高: 按照提示词的要求生成了对应的案例，可以扩展。

> 基于第三次Spike，发现根据之前spike的结论，详细的输入案例描述，能够生成正确的单测代码。
> 但是输入过程还是不够业务导向，需要尝试将提示词进行更加标准化、业务化的描述，不加入mock等实现要求。
> 第四次Spike就是要看看这种情况下，测试是否也可以生成同样的案例

### 第四次Spike
将`SalaryService`文件发送给ClaudeAI，并提示生成`calculate`方法的单测。发送的提示词是：
```markdown
请为这段代码生成单元测试
1. 使用JUnit5框架
2. 测试名字使用‘given给定条件_when做了什么操作_then期望什么结果’这样的结构的英文表达，根据输入输出来命名测试方法
3. 测试的测试数据是：{ userId: 808，baseSalaryRate: 6160，workDays: 21，overTimeHours: 0，employeeTYpe: SALES，seniority: 12，salesRevenue: 54 }
4. 期望的断言结果是：{ baseSalary: 5880，overTimeSalary: 0，senioritySalary: 30，attendanceSalary: 0，salesCommissionSalary: 5000 }
5. 生成后仅保留测试方法的内容。
```

ClaudeAI收到提示后，开始生成单测。生成的单测如下：
```java
@Test
void givenSalesEmployeeWithSpecificConditions_whenCalculateSalary_thenReturnExpectedSalaryComponents() {
        // Arrange
        Long userId = 808L;
        SalaryRequest salaryRequest = new SalaryRequest();
        salaryRequest.setBaseSalaryRate(6160);
        salaryRequest.setWorkDays(21);
        salaryRequest.setOverTimeHours(0);
        salaryRequest.setEmployeeType(SALES);
        salaryRequest.setSeniority(12);
        salaryRequest.setSalesRevenue(54);

        when(workService.get(userId)).thenReturn(salaryRequest);

        // Act
        SalaryResponse result = salaryService.calculate(userId);

        // Assert
        assertAll(
        () -> assertEquals(5880, result.getBaseSalary()),
        () -> assertEquals(0, result.getOverTimeSalary()),
        () -> assertEquals(30, result.getSenioritySalary()),
        () -> assertEquals(0, result.getAttendanceSalary()),
        () -> assertEquals(5000, result.getSalesCommissionSalary())
        );
}
```

### 第四次Spike的评价

* [成功][严重]正确使用JUnit框架版本: 自动识别了JUnit5框架的版本。
* [成功][次要]正确使用AssertJ框架：未识别到AssertJ框架。
* [成功][严重]正确地使用Mock框架。
* [不涉及][次要]生成单测可自动放在对应的文件目录: 点击采纳按钮后能自动放在对应的测试目录。
* [成功][严重]生成的单测可编译: 没有语法错误。
* [成功][严重]生成单测可执行: 生成的单测完全可以执行。
* [成功][严重]正确地使用断言：验证了方法的全部输出。
* [失败][重要]生成的单测测试方法名可读性高: 测试方法命名没有业务含义。
* [成功][次要]没有重复的测试：没有重复的测试。
* [成功][重要]测试覆盖率高: 按照提示词的要求生成了对应的案例，可以扩展。
* [新][成功][重要]仅依据结构化的测试数据和断言生成测试代码。

> 第四次Spike结果显示，输入时候不需要对mock进行过多的要求，只需要对输入输出进行约束，即可产出可运行的达成测试预期的测试代码。
> 但是问题还是有，包括名字并不能自动根据测试数据和断言来合理命名，这一块还是需要用户给出业务场景，来扩展。
> 而且ClaudeAI相比其他模型较为厉害的点在于，目前其实还没有把单测的文件给到它，它的上下文只有SalaryService这个文件，就能生成出，仅需import一些静态方法的能执行的测试代码
>
> 第五次Spike想要验证的假设是：提示词中加入粗略的案例标题，结合之前有的测试数据和断言，能够生成可执行的测试代码，且测试方法名称符合案例名。
> 另外的一个优化点：在生成的代码中，看到是用3A形式来排布的，也可以明确加入到提示词中，让输出更稳定

### 第五次Spike

将`SalaryService`文件发送给ClaudeAI，并提示生成`calculate`方法的单测。发送的提示词是：
```markdown
请为这段代码生成单元测试
1. 使用JUnit5框架
2. 本次测试的案例业务名字是：“测试销售人员薪资计算”，请你根据测试数据和断言来命名测试方法，用DisplayName注解把业务名加上
3. 测试方法名使用‘given给定条件_when做了什么操作_then期望什么结果’结构的英文
4. 测试的测试数据是：{ userId: 808，baseSalaryRate: 6160，workDays: 21，overTimeHours: 0，employeeTYpe: SALES，seniority: 12，salesRevenue: 54 }
5. 期望的断言结果是：{ baseSalary: 5880，overTimeSalary: 0，senioritySalary: 30，attendanceSalary: 0，salesCommissionSalary: 5000 }
6. 生成后仅保留测试方法的内容。
```
ClaudeAI收到提示后，开始生成单测。生成的单测如下：

```java
@Test
@DisplayName("测试销售人员薪资计算")
void givenSalesEmployeeData_whenCalculateSalary_thenReturnExpectedSalary() {
    // Arrange
    Long userId = 808L;
    SalaryRequest mockRequest = new SalaryRequest();
    mockRequest.setBaseSalaryRate(6160);
    mockRequest.setWorkDays(21);
    mockRequest.setOverTimeHours(0);
    mockRequest.setEmployeeType(SALES);
    mockRequest.setSeniority(12);
    mockRequest.setSalesRevenue(54);

    when(workService.get(userId)).thenReturn(mockRequest);

    SalaryService salaryService = new SalaryService(workService);

    // Act
    SalaryResponse result = salaryService.calculate(userId);

    // Assert
    assertEquals(5880, result.getBaseSalary());
    assertEquals(0, result.getOverTimeSalary());
    assertEquals(30, result.getSenioritySalary());
    assertEquals(0, result.getAttendanceSalary());
    assertEquals(5000, result.getSalesCommissionSalary());
}
```

### 第五次Spike的评价

* [成功][严重]正确使用JUnit框架版本: 自动识别了JUnit5框架的版本。
* [成功][次要]正确使用AssertJ框架：未识别到AssertJ框架。
* [成功][严重]正确地使用Mock框架。
* [不涉及][次要]生成单测可自动放在对应的文件目录: 点击采纳按钮后能自动放在对应的测试目录。
* [成功][严重]生成的单测可编译: 没有语法错误。
* [成功][严重]生成单测可执行: 生成的单测完全可以执行。
* [成功][严重]正确地使用断言：验证了方法的全部输出。
* [成功][重要]生成的单测测试方法名可读性高: 根据简单的名字描述，自动生成given_when_then的方法名。
* [成功][次要]没有重复的测试：没有重复的测试。
* [成功][重要]测试覆盖率高: 按照提示词的要求生成了对应的案例，可以扩展。
* [新][成功][重要]仅依据结构化的测试数据和断言生成测试代码。

> 至此，基本可以认为以单个方法为例，大模型有能力在输入了几个关键的“案例信息”后，可以生成可执行的测试代码，并且测试方法名符合业务场景。
>   
> 需要用户输入的关键案例信息可以理解为有3个：
>   1. 输入数据：测试数据
>   2. 断言结果：期望的输出
>   3. 业务场景：测试案例的名字
> 
> 而用户并不需要关注测试的框架，mock的细节等。【仅基于当前仓库的场景】