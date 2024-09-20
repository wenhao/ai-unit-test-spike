# ai-unit-test-spike
ai-unit-test-spike

### 需求

**计算每月薪资**

员工薪资结构：月薪资=基本工资+加班工资+工龄工资+销售提成，最终工资取整发放。

* 基本工资：
  * 文员助理：4400元
  * 车间普工：3520元
  * 质检技术人员：5280元
  * 销售顾问：6160元
* 加班工资：
  * 加班工资适用于车间员工，其他职工加班不计薪资。
  * 加班月工资=基本工资/22天/8小时150%加班系数X月加班小时数。
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
  * [严重]正确地使用Mock框架
  * [次要]生成的单测可自动放在对应的文件目录
  * [严重]生成的单测可编译
  * [严重]生成的单测可执行成功
  * [严重]正确地使用断言
  * [重要]生成的单测测试方法名可读性高
  * [次要]没有重复的测试
  * [重要]测试覆盖率高
  * [次要]重复生成结果差异小
  * [严重]能增量生成单元测试

### 主要的业务代码

主要为了模拟多分支逻辑的情况，所有业务逻辑都在一个方法内完成，请忽略代码坏味道问题。

```java
package ai.unit.test.spike.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ai.unit.test.spike.request.SalaryRequest;
import ai.unit.test.spike.response.SalaryResponse;
import static ai.unit.test.spike.domain.EmployeeType.FACTORY_WORKER;
import static ai.unit.test.spike.domain.EmployeeType.SALES;

@Service
public class SalaryService {
  private final WorkService workService;

  @Autowired
  public SalaryService(final WorkService workService) {
    this.workService = workService;
  }

  public SalaryResponse calculate(Long userId) {
    SalaryRequest salaryRequest = workService.get(userId);
    SalaryResponse salaryResponse = new SalaryResponse();
    salaryResponse.setBaseSalary(Double.valueOf(salaryRequest.getBaseSalaryRate() / 22 * salaryRequest.getWorkDays()).intValue());
    if (salaryRequest.getEmployeeTYpe().equals(FACTORY_WORKER)) {
      double preHourSalary = salaryRequest.getBaseSalaryRate() / 22 / 8 * 1.5;
      salaryResponse.setOverTimeSalary(Double.valueOf((preHourSalary * salaryRequest.getOverTimeHours())).intValue());
    }
    salaryResponse.setSenioritySalary(Math.min((salaryRequest.getSeniority() - 1) * 3 + 10, 30));
    if (salaryRequest.getWorkDays() == 22) {
      salaryResponse.setAttendanceSalary(100);
    }
    if (salaryRequest.getEmployeeTYpe().equals(SALES)) {
      salaryResponse.setSalesCommissionSalary(Double.valueOf((Math.round(salaryRequest.getSalesRevenue() / 10) * 1000)).intValue());
    }
    return salaryResponse;
  }
}
```

### Spike

* [百度Comate Spike](./docs/baidu-comate.md)
* [腾讯AI助手 Spike](./docs/tencent-ai.md)
* [通义灵码 Spike](./docs/tongyi-lingma.md)
* [ClaudeAI_Spike](./docs/claude-ai.md)

### AI辅助单元测试引起的变革

* [AI辅助单元测试引起的变革](./docs/ai-unit-test-evolution.md)
