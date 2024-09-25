### Tencent AI Coding Assistant

### 第一次Spike
直接选择SalaryService的calculate方法并右键使用腾讯云AI代码助手进行生成单测，为了防止干扰删除以前用百度Comate生成的SalaryServiceTest类。

生成如下：

```markdown

#### 单元测试计划
1. 测试准备：
    * 创建 SalaryRequest 和 SalaryResponse 的实例。
    * 设置 SalaryRequest 的属性，包括 employeeTYpe, baseSalaryRate, overTimeHours, seniority, workDays, salesRevenue。
    * 调用 calculate 方法并获取 SalaryResponse 实例。
2. 测试用例：
    * 测试普通员工的基本工资计算。
    * 测试工厂工人的加班工资计算。
    * 测试不同工龄的资深工资计算。
    * 测试全勤工资的计算。
    * 测试销售人员的销售提成工资计算。
3. 断言：
    * 对每个测试用例的结果进行断言，确保 SalaryResponse 的各个属性值符合预期。
```

```java
import ai.unit.test.spike.domain.EmployeeType;
import ai.unit.test.spike.request.SalaryRequest;
import ai.unit.test.spike.response.SalaryResponse;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SalaryServiceTest {

    @Test
    void testCalculateBaseSalary() {
        SalaryRequest request = new SalaryRequest();
        request.setBaseSalaryRate(5000);
        request.setWorkDays(22);
        SalaryResponse response = new SalaryService().calculate(null); // 假设workService.get(userId)返回request
        assertEquals(5000, response.getBaseSalary());
    }

    @Test
    void testCalculateOverTimeSalaryForFactoryWorker() {
        SalaryRequest request = new SalaryRequest();
        request.setEmployeeTYpe(EmployeeTYpe.FACTORY_WORKER);
        request.setBaseSalaryRate(5000);
        request.setOverTimeHours(10);
        SalaryResponse response = new SalaryService().calculate(null); // 假设workService.get(userId)返回request
        assertEquals(413, response.getOverTimeSalary());
    }

    @Test
    void testCalculateSenioritySalary() {
        SalaryRequest request = new SalaryRequest();
        request.setSeniority(5);
        SalaryResponse response = new SalaryService().calculate(null); // 假设workService.get(userId)返回request
        assertEquals(25, response.getSenioritySalary());
    }

    @Test
    void testCalculateAttendanceSalary() {
        SalaryRequest request = new SalaryRequest();
        request.setWorkDays(22);
        SalaryResponse response = new SalaryService().calculate(null); // 假设workService.get(userId)返回request
        assertEquals(100, response.getAttendanceSalary());
    }

    @Test
    void testCalculateSalesCommissionSalary() {
        SalaryRequest request = new SalaryRequest();
        request.setEmployeeTYpe(EmployeeTYpe.SALES);
        request.setSalesRevenue(15000);
        SalaryResponse response = new SalaryService().calculate(null); // 假设workService.get(userId)返回request
        assertEquals(15000, response.getSalesCommissionSalary());
    }
}

```

### 评价：

生成单测的用户旅程和体验不如百度Comate。

```markdown
* [成功][严重]正确使用JUnit框架版本: 自动识别了JUnit5框架的版本。
* [失败][次要]正确使用AssertJ框架：未识别到AssertJ框架。
* [失败][严重]正确地使用Mock框架：没有Mock掉Service的依赖，没有正确使用@Mock和@InjectMocks注解。
* [失败][次要]生成单测可自动放在对应的文件目录: 并没有识别Java工程的目录结构。
* [失败][严重]生成的单测可编译: 因为没有正确识别单测文件目录的问题，导致没有package，故无法找到SalaryService类无法编译。
* [失败][严重]生成单测可执行: 没有正确的Mock无法编译且无法执行。
* [失败][严重]正确地使用断言：没有完全验证方法的输出是否正确。
* [失败][重要]生成的单测测试方法名可读性高: 测试方法命名并不能反应测试意图。
* [失败][次要]没有重复的测试：没有重复的测试。
* [失败][重要]测试覆盖率高: 测试失败无测试覆盖率。
```

### 第二次Spike
尝试使用JUnit5和AssertJ生成单测，放弃一键生成单测的功能直接手动输入提示词：“请为这段代码生成单元测试；使用JUnit5、AssertJ和Mockito框架，AssertJ框架请使用如assertThat方法”，生成结果如下：

```java
import ai.unit.test.spike.domain.EmployeeType;
import ai.unit.test.spike.request.SalaryRequest;
import ai.unit.test.spike.response.SalaryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class SalaryServiceTest {

    private final WorkService workService = Mockito.mock(WorkService.class);
    private final SalaryService salaryService = new SalaryService();

    @BeforeEach
    void setUp() {
        // 设置模拟的 SalaryRequest 对象
        SalaryRequest request = new SalaryRequest();
        request.setBaseSalaryRate(5000);
        request.setOverTimeHours(10);
        request.setSeniority(5);
        request.setWorkDays(22);
        request.setSalesRevenue(15000);
        request.setEmployeeTYpe(EmployeeType.SALES);

        when(workService.get(1L)).thenReturn(request);
    }

    @Test
    void 测试普通员工基本工资计算() {
        SalaryResponse response = salaryService.calculate(1L);

        assertThat(response.getBaseSalary()).isEqualTo(5000);
    }

    @Test
    void 测试工厂工人加班工资计算() {
        SalaryRequest request = new SalaryRequest();
        request.setBaseSalaryRate(5000);
        request.setOverTimeHours(10);
        request.setEmployeeTYpe(EmployeeType.FACTORY_WORKER);

        when(work.jservice.get(2L)).thenReturn(request);

        SalaryResponse response = salaryService.calculate(2L);

        assertThat(response.getOverTimeSalary()).isEqualTo(413);
    }

    @Test
    void 测试员工工龄工资计算() {
        SalaryResponse response = salaryService.calculate(1L);

        assertThat(response.getSenioritySalary()).isEqualTo(22);
    }

    @Test
    void 测试员工全勤工资计算() {
        SalaryResponse response = salaryService.calculate(1L);

        assertThat(response.getAttendanceSalary()).isEqualTo(100);
    }

    @Test
    void 测试销售人员销售提成工资计算() {
        SalaryResponse response = salaryService.calculate(1L);

        assertThat(response.getSalesCommissionSalary()).isEqualTo(15000);
    }

    @Test
    void 测试总工资计算() {
        SalaryResponse response = salaryService.calculate(1L);

        assertThat(response.getTotalSalary()).isEqualTo(21733);
    }
}
```

### 评价：

```markdown
* [成功][严重]正确使用JUnit框架版本: 自动识别了JUnit5框架的版本。
* [成功][次要]正确使用AssertJ框架：虽然提示词里面提到了AssertJ框架，但实际代码还是没用到。
* [失败][严重]正确地使用Mock框架：在第3个测试方法中出现了变量不存在的情况。
* [失败][次要]生成单测可自动放在对应的文件目录: 并没有识别Java工程的目录结构。
* [失败][严重]生成的单测可编译: SalaryService构建的时候没有传参数导致编译失败，有没有声明的变量存在。
* [失败][严重]生成的单测可执行成功: 无法编译就无法执行。
* [失败][严重]正确地使用断言：没有完全验证方法的输出是否正确。
* [失败][重要]生成的单测测试方法名可读性高: 使用中文来命名测试方法，不太推荐，但也算有点可读性。
* [失败][次要]没有重复的测试：没有重复的测试。
* [失败][重要]测试覆盖率高: 测试失败无测试覆盖率。
```

### 第三次Spike
尝试修复Mock问题，修改提示词为：“请使用Mockito框架的@Mock和@InjectMocks并使用MockitoAnnotations.initMocks(this)初始化。”，生成结果如下：

```java
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

class SalaryServiceTest {

    @Mock
    private WorkService workService;

    @InjectMocks
    private SalaryService salaryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        // 设置模拟的 SalaryRequest 对象
        SalaryRequest request = new SalaryRequest();
        request.setBaseSalaryRate(5000);
        request.setOverTimeHours(10);
        request.setSeniority(5);
        request.setWorkDays(22);
        request.setSalesRevenue(15000);
        request.setEmployeeTYpe(EmployeeType.SALES);

        when(workService.get(1L)).thenReturn(request);
    }

    @Test
    void 测试普通员工基本工资计算() {
        SalaryResponse response = salaryService.calculate(1L);

        assertThat(response.getBaseSalary()).isEqualTo(5000);
    }

    @Test
    void 测试工厂工人加班工资计算() {
        SalaryRequest request = new SalaryRequest();
        request.setBaseSalaryRate(5000);
        request.setOverTimeHours(10);
        request.setEmployeeTYpe(EmployeeType.FACTORY_WORKER);

        when(workService.get(2L)).thenReturn(request);

        SalaryResponse response = salaryService.calculate(2L);

        assertThat(response.getOverTimeSalary()).isEqualTo(413);
    }

    @Test
    void 测试员工工龄工资计算() {
        SalaryResponse response = salaryService.calculate(1L);

        assertThat(response.getSenioritySalary()).isEqualTo(22);
    }

    @Test
    void 测试员工全勤工资计算() {
        SalaryResponse response = salaryService.calculate(1L);

        assertThat(response.getAttendanceSalary()).isEqualTo(100);
    }

    @Test
    void 测试销售人员销售提成工资计算() {
        SalaryResponse response = salaryService.calculate(1L);

        assertThat(response.getSalesCommissionSalary()).isEqualTo(15000);
    }

    @Test
    void 测试总工资计算() {
        SalaryResponse response = salaryService.calculate(1L);

        assertThat(response.getTotalSalary()).isEqualTo(21733);
    }
}
```

### 评价：

```markdown
* [成功][严重]正确使用JUnit框架版本：自动识别了JUnit5框架的版本。
* [成功][次要]正确使用AssertJ框架：虽然提示词里面提到了AssertJ框架，但实际代码还是没用到。
* [成功][严重]正确地使用Mock框架：修改提示词后正确的使用Mock框架，因为没有使用mockito-junit-jupiter依赖，所以不能用@ExtendWith(MockitoExtension.class)，腾讯AI并没有读到这个上下文信息。
* [失败][次要]生成单测可自动放在对应的文件目录: 并没有识别Java工程的目录结构。
* [成功][严重]生成的单测可编译: 修改提示词后可编译。
* [失败][严重]生成的单测可执行成功: 生成的测试全部执行失败。
* [失败][严重]正确地使用断言：没有完全验证方法的输出是否正确。
* [失败][重要]生成的单测测试方法名可读性高: 使用中文来命名测试方法，不太推荐，但也算有点可读性。
* [失败][次要]没有重复的测试：没有重复的测试。
* [失败][重要]测试覆盖率高: 测试失败无测试覆盖率。
```

### 第四次Spike
再次尝试修复单测执行不成功的的问题，根据使用百度Comate的经验，每次仅生成一个单测方法并提供方法名、测试数据和断言输出，那就需要更多的提示词：

```markdown
请按以下要求生成单元测试：
1、请为这段代码生成单元测试；使用JUnit5、AssertJ和Mockito框架，AssertJ框架请使用如assertThat方法。
2、请使用Mockito框架的@Mock和@InjectMocks并使用MockitoAnnotations.initMocks(this)初始化。
3、请生成一个测试方法，该方法的名称为should_calculate_salary_for_office_assistant_with_base_salary_and_seniority_salary_and_attendance_salary
，并且给该测试方法加上注释如下：测试意图：文员助理仅含基本考勤工资+工龄工资+全勤奖。
4、该测试方法使用以下测试数据，userId为801，salaryRequest中的数据分别是，baseSalaryRate为4400，workDays为22，overTimeHours为0，employeeTYpe为OFFICE_ASSISTANT，seniority为3
，salesRevenue为0。
5、断言结果：baseSalary为4400，overTimeSalary为0，senioritySalary为16，attendanceSalary为100，salesCommissionSalary为0。 
```

生成结果如下：

```java
package ai.unit.test.spike.service;

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
        // 准备测试数据
        SalaryRequest salaryRequest = new SalaryRequest();
        salaryRequest.setBaseSalaryRate(4400);
        salaryRequest.setWorkDays(22);
        salaryRequest.setOverTimeHours(0);
        salaryRequest.setEmployeeTYpe(EmployeeType.OFFICE_ASSISTANT);
        salaryRequest.setSeniority(3);
        salaryRequest.setSalesRevenue(0);

        when(workService.get(801L)).thenReturn(salaryRequest);

        // 调用被测试方法
        SalaryResponse response = salaryService.calculate(801L);

        // 断言结果
        assertThat(response.getBaseSalary()).isEqualTo(4400);
        assertThat(response.getOverTimeSalary()).isEqualTo(0);
        assertThat(response.getSenioritySalary()).isEqualTo(16);
        assertThat(response.getAttendanceSalary()).isEqualTo(100);
        assertThat(response.getSalesCommissionSalary()).isEqualTo(0);
    }
}
```

### 评价：

```markdown
* [成功][严重]正确使用JUnit框架版本: 自动识别了JUnit5框架的版本。
* [成功][次要]正确使用AssertJ框架：虽然提示词里面提到了AssertJ框架，但实际代码还是没用到。
* [成功][严重]正确地使用Mock框架：修改提示词后正确的使用Mock框架。
* [失败][次要]生成单测可自动放在对应的文件目录: 并没有识别Java工程的目录结构。
* [失败][严重]生成的单测可编译：少引入了几个类的包，导致无法编译。
* [成功][严重]生成的单测可执行成功: 手动解决包引入缺失的问题后，单测可执行成功，但前提是要明确给与测试数据和断言输出结果。
* [成功][严重]正确地使用断言：提前告知AI需要的断言。
* [成功][重要]生成的单测测试方法名可读性高: 提前设定好测试方法名或者通过测试方法注释说明测试意图。
* [成功][次要]没有重复的测试：没有重复的测试。
* [失败][重要]测试覆盖率高: 不能一次性达成100%的测试覆盖率，即便是把所有测试案例提前输入进去大概率也会遇到内容截断的问题。
```

### 第五次Spike
再次尝试修复无法编译的问题，明确告知AI必须导入依赖的包，提示词如下：

```markdown
请按以下要求生成单元测试：
1、请为这段代码生成单元测试；使用JUnit5、AssertJ和Mockito框架，AssertJ框架请使用如assertThat方法。
2、请使用Mockito框架的@Mock和@InjectMocks并使用MockitoAnnotations.initMocks(this)初始化。
3、请生成一个测试方法，该方法的名称为should_calculate_salary_for_office_assistant_with_base_salary_and_seniority_salary_and_attendance_salary
，并且给该测试方法加上注释如下：测试意图：文员助理仅含基本考勤工资+工龄工资+全勤奖。
4、该测试方法使用以下测试数据，userId为801，salaryRequest中的数据分别是，baseSalaryRate为4400，workDays为22，overTimeHours为0，employeeTYpe为OFFICE_ASSISTANT，seniority为3
，salesRevenue为0。
5、断言结果：baseSalary为4400，overTimeSalary为0，senioritySalary为16，attendanceSalary为100，salesCommissionSalary为0。
6、请导入被测试类依赖的类的包。
```

生成结果如下：

```java
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
}
```

### 评价：

```markdown
* [成功][严重]正确使用JUnit框架版本: 自动识别了JUnit5框架的版本。
* [成功][次要]正确使用AssertJ框架：虽然提示词里面提到了AssertJ框架，但实际代码还是没用到。
* [成功][严重]正确地使用Mock框架：修改提示词后正确的使用Mock框架。
* [失败][次要]生成单测可自动放在对应的文件目录: 并没有识别Java工程的目录结构。
* [成功][严重]生成的单测可编译：明确告知AI需要导入依赖的包后可编译。
* [成功][严重]生成的单测可执行成功: 明确告知AI需要导入依赖的包后可执行成功。
* [成功][严重]正确地使用断言：提前告知AI需要的断言。
* [成功][重要]生成的单测测试方法名可读性高: 提前设定好测试方法名或者通过测试方法注释说明测试意图。
* [成功][次要]没有重复的测试：没有重复的测试。
* [成功][重要]测试覆盖率高: 手动达成测试覆盖100%。
```

### 阶段性总结

```markdown
1. 跟百度Comate相同的是必须明确的告知AI生成单测需要依赖的框架，JUnit、AssertJ、Mockito等；测试名称、测试输入和预期输出；一次仅生成一个单测方法。。
2. 跟百度Comate不同的是，腾讯AI犯的低级错误更多一点包括：少导包、诡异的变量、没有package、无法识别框架依赖、如果对话过程中选中了其他代码就变成了给这段代码的对话等。
```

### 第六次Spike
多次重复第五次Spike的过程，并尝试每次仅生成测试方法，把该方法的测试已覆盖率提高到100%。提示词如下：

```markdown
请按以下要求生成单元测试：
1、请为这段代码生成单元测试；使用JUnit5、AssertJ和Mockito框架，AssertJ框架请使用如assertThat方法。
2、请使用Mockito框架的@Mock和@InjectMocks并使用MockitoAnnotations.initMocks(this)初始化。
3、请生成一个测试方法，该方法的名称为should_calculate_salary_for_office_assistant_with_base_salary_and_seniority_salary_and_attendance_salary
，并且给该测试方法加上注释如下：测试意图：文员助理仅含基本考勤工资+工龄工资+全勤奖。
4、该测试方法使用以下测试数据，userId为802，Mock对象salaryRequest中的数据分别是，baseSalaryRate为4400，workDays为20，overTimeHours为0，employeeTYpe为OFFICE_ASSISTANT，seniority为11，salesRevenue为0。
5、断言结果：baseSalary为4000，overTimeSalary为0，senioritySalary为30，attendanceSalary为0，salesCommissionSalary为0。
6、请导入被测试类依赖的类的包。
7、生成后删除除测试方法以外所有的内容，包括import、类成员、setUp部分。
```

生成的结果如下：

```java
import ai.unit.test.spike.domain.EmployeeType;
import ai.unit.test.spike.request.SalaryRequest;
import ai.unit.test.spike.response.SalaryResponse;
import ai.unit.test.spike.service.SalaryService;
import ai.unit.test.spike.service.WorkService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class SalaryServiceTest {

    @Test
    public void should_calculate_salary_for_office_assistant_with_base_salary_and_seniority_salary_and_attendance_salary() {
        // 初始化Mock对象
        MockitoAnnotations.initMocks(this);

        // 创建Mock对象
        @Mock
        private WorkService workService;

        @InjectMocks
        private SalaryService salaryService;

        // 测试数据
        Long userId = 802L;
        SalaryRequest salaryRequest = new SalaryRequest();
        salaryRequest.setBaseSalaryRate(4400);
        salaryRequest.setWorkDays(20);
        salaryRequest.setOverTimeHours(0);
        salaryRequest.setEmployeeTYpe(EmployeeType.OFFICE_ASSISTANT);
        salaryRequest.setSeniority(11);
        salaryRequest.setSalesRevenue(0);

        // 模拟workService.get方法返回salaryRequest
        when(workService.get(userId)).thenReturn(salaryRequest);

        // 调用calculate方法
        SalaryResponse salaryResponse = salaryService.calculate(userId);

        // 断言结果
        assertThat(salaryResponse.getBaseSalary()).isEqualTo(4000);
        assertThat(salaryResponse.getOverTimeSalary()).isEqualTo(0);
        assertThat(salaryResponse.getSenioritySalary()).isEqualTo(30);
        assertThat(salaryResponse.getAttendanceSalary()).isEqualTo(0);
        assertThat(salaryResponse.getSalesCommissionSalary()).isEqualTo(0);
    }
}
```

### 评价：

生成的代码还是包含了import和setUp了，并且代码再次无法编译了，尝试了多次腾讯AI都无法达到预期的结果，放弃仅生成单个测试方法的思路。也发现腾讯AI会把已生成的SalaryServiceTest作为上下文，导致错误相互影响变得胡言乱语。

```markdown
* [成功][严重]正确使用JUnit框架版本: 自动识别了JUnit5框架的版本。
* [成功][次要]正确使用AssertJ框架：虽然提示词里面提到了AssertJ框架，但实际代码还是没用到。
* [成功][严重]正确地使用Mock框架：修改提示词后正确的使用Mock框架。
* [失败][次要]生成单测可自动放在对应的文件目录: 并没有识别Java工程的目录结构。
* [失败][严重]生成的单测可编译：腾讯不理解什么叫仅生成测试方法导致生成的单测代码无法编译。
* [失败][严重]生成的单测可执行成功: 明确告知AI需要导入依赖的包后可执行成功。
* [成功][严重]正确地使用断言：提前告知AI需要的断言。
* [成功][重要]生成的单测测试方法名可读性高: 提前设定好测试方法名或者通过测试方法注释说明测试意图。
* [成功][次要]没有重复的测试：没有重复的测试。
* [成功][重要]测试覆盖率高: 手动达成测试覆盖100%。
```

### 第七次Spike
多次重复第五次Spike的过程，不要求仅生成测试方法。提示词如下：

```markdown
请按以下要求生成单元测试：
1、请为这段代码生成单元测试；使用JUnit5、AssertJ和Mockito框架，AssertJ框架请使用如assertThat方法。
2、请使用Mockito框架的@Mock和@InjectMocks并使用MockitoAnnotations.initMocks(this)初始化。
3、请生成一个测试方法，该方法的名称为should_calculate_salary_for_office_assistant_with_partial_base_salary_and_full_seniority_salary_and_without_attendance_salary
，并且给该测试方法加上注释如下：测试意图：文员助理仅含基本考勤工资+工龄工资+全勤奖。
4、该测试方法使用以下测试数据，userId为802，Mock对象salaryRequest中的数据分别是，baseSalaryRate为4400，workDays为20，overTimeHours为0，employeeTYpe为OFFICE_ASSISTANT，seniority为11，salesRevenue为0。
5、断言结果：baseSalary为4000，overTimeSalary为0，senioritySalary为30，attendanceSalary为0，salesCommissionSalary为0。
6、请导入被测试类依赖的类的包。
```

生成的结果如下：

```java
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
}
```

### 评价：

```markdown
* [成功][严重]正确使用JUnit框架版本: 自动识别了JUnit5框架的版本。
* [成功][次要]正确使用AssertJ框架：虽然提示词里面提到了AssertJ框架，但实际代码还是没用到。
* [成功][严重]正确地使用Mock框架：修改提示词后正确的使用Mock框架。
* [失败][次要]生成单测可自动放在对应的文件目录: 并没有识别Java工程的目录结构。
* [失败][严重]生成的单测可编译: 又出现导包缺失的情况，怀疑是腾讯AI没有识别正确地上下文，或者是多轮对话后上下文缺失导致胡言乱语。
* [失败][严重]生成的单测可执行成功: 无法编译就无法执行。
* [成功][严重]正确地使用断言：提前告知AI需要的断言。
* [成功][重要]生成的单测测试方法名可读性高: 提前设定好测试方法名或者通过测试方法注释说明测试意图。
* [成功][次要]没有重复的测试：没有重复的测试。
* [成功][重要]测试覆盖率高: 手动达成测试覆盖100%。
```

继续生产其他测试案例的单元测试：

```markdown
请按以下要求生成单元测试：
1、请为这段代码生成单元测试；使用JUnit5、AssertJ和Mockito框架，AssertJ框架请使用如assertThat方法。
2、请使用Mockito框架的@Mock和@InjectMocks并使用MockitoAnnotations.initMocks(this)初始化。
3、请生成一个测试方法，该方法的名称为should_calculate_salary_for_facory_worker_with_base_salary_and_overtime_salary_and_seniority_salary_and_attendance_salary
，并且给该测试方法加上注释：测试意图：车间薪资=基本考勤工资+加班工资+工龄工资+全勤奖。
4、该测试方法使用以下测试数据，userId为803，Mock对象salaryRequest中的数据分别是，baseSalaryRate为3520，workDays为22，overTimeHours为13，employeeTYpe为FACTORY_WORKER，seniority为1，salesRevenue为0。
5、断言结果：baseSalary为3520，overTimeSalary为390，senioritySalary为10，attendanceSalary为100，salesCommissionSalary为0。
6、请导入被测试类依赖的类的包。
```

生成的结果如下：

```java
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
     * 测试意图：车间薪资=基本考勤工资+加班工资+工龄工资+全勤奖。
     */
    @Test
    public void should_calculate_salary_for_facory_worker_with_base_salary_and_overtime_salary_and_seniority_salary_and_attendance_salary() {
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
}
```

```markdown
请按以下要求生成单元测试：
1、请为这段代码生成单元测试；使用JUnit5、AssertJ和Mockito框架，AssertJ框架请使用如assertThat方法。
2、请使用Mockito框架的@Mock和@InjectMocks并使用MockitoAnnotations.initMocks(this)初始化。
3、请生成一个测试方法，该方法的名称为should_calculate_salary_for_factory_worker_with_partial_base_salary_and_overtime_salary_and_seniority_salary_and_without_attendance_salary
，并且给该测试方法加上注释：测试意图：车间薪资=部分考勤工资+加班工资+工龄工资。
4、该测试方法使用以下测试数据，userId为804，Mock对象salaryRequest中的数据分别是，baseSalaryRate为3520，workDays为19，overTimeHours为6，employeeTYpe为FACTORY_WORKER，seniority为7，salesRevenue为0。
5、断言结果：baseSalary为3040，overTimeSalary为180，senioritySalary为28，attendanceSalary为0，salesCommissionSalary为0。
6、请在测试类中导入被测试类依赖的类的包。
```

生成的结果如下：

```java
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
     * 测试意图：车间薪资=部分考勤工资+加班工资+工龄工资。
     */
    @Test
    public void should_calculate_salary_for_factory_worker_with_partial_base_salary_and_overtime_salary_and_seniority_salary_and_without_attendance_salary() {
        // 准备测试数据
        Long userId = 804L;
        SalaryRequest salaryRequest = new SalaryRequest();
        salaryRequest.setBaseSalaryRate(3520);
        salaryRequest.setWorkDays(19);
        salaryRequest.setOverTimeHours(6);
        salaryRequest.setEmployeeTYpe(EmployeeType.FACTORY_WORKER);
        salaryRequest.setSeniority(7);
        salaryRequest.setSalesRevenue(0);

        when(workService.get(userId)).thenReturn(salaryRequest);

        // 调用被测试方法
        SalaryResponse salaryResponse = salaryService.calculate(userId);

        // 断言结果
        assertThat(salaryResponse.getBaseSalary()).isEqualTo(3040);
        assertThat(salaryResponse.getOverTimeSalary()).isEqualTo(180);
        assertThat(salaryResponse.getSenioritySalary()).isEqualTo(28);
        assertThat(salaryResponse.getAttendanceSalary()).isEqualTo(0);
        assertThat(salaryResponse.getSalesCommissionSalary()).isEqualTo(0);
    }
}
```

### 评价：

还是无法正确的导包，放弃。

```markdown
请按以下要求生成单元测试：
1、请为这段代码生成单元测试；使用JUnit5、AssertJ和Mockito框架，AssertJ框架请使用如assertThat方法。
2、请使用Mockito框架的@Mock和@InjectMocks并使用MockitoAnnotations.initMocks(this)初始化。
3、请生成一个测试方法，该方法的名称为should_calculate_salary_for_quality_control_with_base_salary_and_seniority_salary_and_attendance_salary
，并且给该测试方法加上注释：测试意图：质检技术人员薪资=基本考勤工资+工龄工资+全勤奖。
4、该测试方法使用以下测试数据，userId为805，Mock对象salaryRequest中的数据分别是，baseSalaryRate为5280，workDays为22，overTimeHours为0，employeeTYpe为QUALITY_CONTROL，seniority为5，salesRevenue为0。
5、断言结果：baseSalary为5280，overTimeSalary为0，senioritySalary为22，attendanceSalary为100，salesCommissionSalary为0。
```

生成的结果如下：

```java
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
     * 测试意图：质检技术人员薪资=基本考勤工资+工龄工资+全勤奖。
     */
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
}
```

```markdown
请按以下要求生成单元测试：
1、请为这段代码生成单元测试；使用JUnit5、AssertJ和Mockito框架，AssertJ框架请使用如assertThat方法。
2、请使用Mockito框架的@Mock和@InjectMocks并使用MockitoAnnotations.initMocks(this)初始化。
3、请生成一个测试方法，该方法的名称为should_calculate_salary_for_sales_with_base_salary_and_seniority_salary_and_attendance_salary_and_sales_commission_salary
，并且给该测试方法加上注释：测试意图：销售人员薪资=基本考勤工资+工龄工资+全勤奖+提成。
4、该测试方法使用以下测试数据，userId为806，Mock对象salaryRequest中的数据分别是，baseSalaryRate为6160，workDays为22，overTimeHours为0，employeeTYpe为SALES，seniority为1，salesRevenue为7。
5、断言结果：baseSalary为6160，overTimeSalary为0，senioritySalary为10，attendanceSalary为100，salesCommissionSalary为1000。
```

生成结果如下：

```java
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
}
```

```markdown
请按以下要求生成单元测试：
1、请为这段代码生成单元测试；使用JUnit5、AssertJ和Mockito框架，AssertJ框架请使用如assertThat方法。
2、请使用Mockito框架的@Mock和@InjectMocks并使用MockitoAnnotations.initMocks(this)初始化。
3、请生成一个测试方法，该方法的名称为should_calculate_salary_for_sales_with_base_salary_and_seniority_salary_and_attendance_salary_and_sales_commission_salary_more
，并且给该测试方法加上注释：测试意图：销售人员薪资=基本考勤工资+工龄工资+全勤奖+提成。
4、该测试方法使用以下测试数据，userId为807，Mock对象salaryRequest中的数据分别是，baseSalaryRate为6160，workDays为22，overTimeHours为0，employeeTYpe为SALES，seniority为7，salesRevenue为38。
5、断言结果：baseSalary为6160，overTimeSalary为0，senioritySalary为28，attendanceSalary为100，salesCommissionSalary为4000。
```

生成结果如下：

```java
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
}
```

```markdown
请按以下要求生成单元测试：
1、请为这段代码生成单元测试；使用JUnit5、AssertJ和Mockito框架，AssertJ框架请使用如assertThat方法。
2、请使用Mockito框架的@Mock和@InjectMocks并使用MockitoAnnotations.initMocks(this)初始化。
3、请请生成一个测试方法，该方法的名称为should_calculate_salary_for_sales_with_partial_base_salary_and_full_seniority_salary_and_without_attendance_salary_and_sales_commission_salary
，并且给该测试方法加上注释：测试意图：销售人员薪资=部分考勤工资+工龄工资+提成。
4、该测试方法使用以下测试数据，userId为808，Mock对象salaryRequest中的数据分别是，baseSalaryRate为6160，workDays为21，overTimeHours为0，employeeTYpe为SALES，seniority为12，salesRevenue为54。
5、断言结果：baseSalary为5880，overTimeSalary为0，senioritySalary为30，attendanceSalary为0，salesCommissionSalary为5000。
```

生成结果如下：

```java
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
```

### 评价：

最终测试覆盖率达到了100%，过程并不像百度Comate那样的的流畅，生成的代码几乎都需要人工参与修复，如果能在优化下对测试上下文的理解及生成测试的用户旅程，应该会更好。

### 整体总结

1. 腾讯AI助手的单测生成推理能力实践下来比百度Comate要差一些，具体表现是：无法编译较多、上下文理解不够、新增测试类与增量添加测试方法旅程不合理等等，其中无法编译的问题比较难以接受。
2. 除以上外生成单元测试的结果与百度Comate相差无几。
