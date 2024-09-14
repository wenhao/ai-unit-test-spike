# ai-unit-test-spike
ai-unit-test-spike

### 需求

**计算每月薪资**

员工薪资结构：月薪资=基本工资+加班工资+工龄工资+销售提成，最终工资取整发放。

* 基本工资：
  * 文员助理：3000元
  * 车间普工：2400元
  * 质检技术人员：3600元
  * 销售顾问：4000元
* 加班工资：
  * 加班工资适用于车间员工，其他职工加班不计薪资。
  * 加班月工资=基本工资/22天/8小时*150%加班系数*月加班小时数。
* 工龄工资：每月10元，每年递增3元，最高每月30元。
* 全勤奖：全勤以22天算，每月100元。
* 销售提成：业绩每10万(不足10万安四舍五入)，提成1%。

计算以下员工当月的薪资：

* 文员-小王：员工号：801，工龄3年，当月全勤。
* 文员-小李：员工号：802，工龄11年，当月出勤20天。
* 车间-小刘：员工号：803，工龄1年，当月全勤，加班13小时。
* 车间-小赵：员工号：804，工龄7年，当月出勤19天，加班6小时。
* 质检-小周：员工号：805，工银5年，当月全勤。
* 销售-小孔：员工号：806，工龄1年，当月全勤，业务7万。
* 销售-小吴：员工号：807，工龄4年，当月全勤，业绩38万。
* 销售-小杨：员工号：808，工龄12年，当月出勤21天，业绩54万。

### 测试场景

把所有业务逻辑都写在service的一个方法里面，模拟很多业务分支的情况便于测试AI辅助单测的效果。

### 目标

* 借助AI如何更精准的生成单元测试
  * [严重]正确使用JUnit框架版本
  * [次要]正确使用AssertJ框架
  * [次要]生成的单测可自动放在对应的文件目录
  * [严重]生成的单测可编译 
  * [严重]生成的单测可执行成功
  * [重要]生成的单测测试方法论可读性高
  * [严重]正确地使用Mock框架
  * [严重]正确地使用断言
  * [次要]没有重复的测试
  * [重要]测试执行速度快
  * [重要]测试覆盖率高
  * [次要]重复生成结果差异小

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

* [成功][严重]正确使用JUnit框架版本: 自动识别了JUnit5框架的版本。
* [失败][次要]正确使用AssertJ框架：未识别到AssertJ框架。
* [成功][次要]生成单测可自动放在对应的文件目录: 点击采纳按钮后能自动放在对应的测试目录。
* [成功][严重]生成的单测可编译: 没有语法错误。
* [失败][严重]生成单测可执行: 生成的4个测试方法全部执行失败。
* [失败][重要]生成的单测测试方法论可读性高: 测试方法命名并不能反应测试意图。
* [成功][严重]正确地使用Mock框架。
* [失败][严重]正确地使用断言：没有完全验证方法的输出是否正确。
* [成功][次要]没有重复的测试：没有重复的测试。
* [成功][重要]测试执行速度快: 没有启动Spring服务。
* [失败][重要]测试覆盖率高: 测试失败无测试覆盖率。

