package ai.unit.test.spike.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ai.unit.test.spike.response.SalaryResponse;
import ai.unit.test.spike.service.SalaryService;

@RestController
@RequestMapping("salaries")
public class SalaryController {

    private final SalaryService salaryService;

    @Autowired
    public SalaryController(final SalaryService salaryService) {
        this.salaryService = salaryService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<SalaryResponse> getSalary(@PathVariable Long userId) {
        SalaryResponse salaryResponse = salaryService.calculate(userId);
        return ResponseEntity.ok(salaryResponse);
    }
}
