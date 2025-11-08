package com.example.yardperformancetracker.controller;
import com.example.yardperformancetracker.entity.Employee;
import com.example.yardperformancetracker.repository.EmployeeRepository;
import com.example.yardperformancetracker.service.EmployeeService;
import com.example.yardperformancetracker.service.ReportService;
import com.example.yardperformancetracker.service.ShiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService service;
    private final ReportService reportService;
    private final ShiftService shiftService;
    private final EmployeeRepository employeeRepo;

    @GetMapping
    public List<Map<String, Object>> getAllEmployees() {
        List<Employee> employees = employeeRepo.findAll();


        return employees.stream() .map(e -> {
            Map<String, Object> empData = new HashMap<>();
            empData.put("id", e.getId());
            empData.put("name", e.getName());
            empData.put("position", e.getPosition());
            empData.put("shift", e.getShift() != null ? e.getShift().getType().name() : "N/A");
            empData.put("kpi", e.getKpi()); empData.put("feedbackMessage", e.getFeedbackMessage()); return empData; })
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public Employee getOne(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public Employee create(@RequestBody Employee e) {
        return service.create(e);
    }

    @PutMapping("/{id}")
    public Employee update(@PathVariable Long id, @RequestBody Employee e) {
        return service.update(id, e);
    }

    @GetMapping("/{id}/report")
    public String getReport(@PathVariable Long id) {
        return reportService.generatePerformanceReport(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
    @GetMapping("/shift/{id}/average-kpi")
    public double getShiftAverageKpi(@PathVariable Long id) {
        return shiftService.getAverageKpiForShift(id);
    }
}