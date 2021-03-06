package com.example.universetechapidemoapp.controller;

import com.example.universetechapidemoapp.model.Employee;
import com.example.universetechapidemoapp.model.SalaryManager;
import com.example.universetechapidemoapp.repository.EmployeeRepository;
import com.example.universetechapidemoapp.repository.SalaryManagerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v2/employees")
public class EmployeeController {

    private final EmployeeRepository employeeRepository;
    private final SalaryManagerRepository salaryManagerRepository;

    @GetMapping
    public Iterable<Employee> findAll() {
        var employees = employeeRepository.findAll();
        log.info("Employee Controller - find all");
        return employeeRepository.findAll();
    }

    @GetMapping("/{id}")
    public Employee getById(@PathVariable Long id) {
        log.info("Employee Controller - get by id: {}", id);
        return employeeRepository.findById(id).get();
    }

    @PostMapping
    public Employee saveEmployee(@Valid @RequestBody Employee employee) {
        log.info("Employee Controller - save employee: {}", employee);
        var employeeSaved = employeeRepository.save(employee);
        var salaryManager = new SalaryManager();
        salaryManager.setId(employeeSaved.getId());
        salaryManager.setEmployee(employeeSaved);
        salaryManagerRepository.save(salaryManager);
        return employee;
    }

    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable Long id, @Valid @RequestBody Employee employee) {
        var employeeToUpdate = employeeRepository.findById(id).get();
        BeanUtils.copyProperties(employee, employeeToUpdate);
        log.info("Employee Controller - update employee: {}", employeeToUpdate);
        return employeeRepository.save(employeeToUpdate);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public void deleteEmployee(@NotNull @PathVariable Long id) {
        log.info("Employee Controller - delete employee: {}", id);
        employeeRepository.deleteById(id);
    }
}
