package com.example.yardperformancetracker.controller;
import com.example.yardperformancetracker.entity.Employee;
import com.example.yardperformancetracker.repository.EmployeeRepository;
import com.example.yardperformancetracker.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;



@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final EmployeeRepository employeeRepository;

    // ==================== BASIC DASHBOARD ====================

    @GetMapping("/summary")
    public Map<String, Object> getSummary() {
        return dashboardService.getSummary();
    }

    @GetMapping("/process/{type}")
    public Map<String, Object> getProcessStats(@PathVariable String type) {
        return dashboardService.getProcessStats(type);
    }

    @GetMapping("/shift/{shiftType}")
    public Map<String, Object> getShiftStats(@PathVariable String shiftType) {
        return dashboardService.getShiftStats(shiftType);
    }

    // ==================== EMPLOYEE SELF VIEW ====================

    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("/self")
    public ResponseEntity<?> getSelfDashboard(@RequestParam String username) {
        Employee employee = employeeRepository.findAll().stream()
                .filter(e -> e.getName().equalsIgnoreCase(username))
                .findFirst()
                .orElse(null);

        if (employee == null) {
            return ResponseEntity.status(404).body(Map.of("message", "Employee not found"));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("employeeName", employee.getName());
        response.put("position", employee.getPosition());
        response.put("shift", employee.getShift() != null ? employee.getShift().getShiftName(): "No shift");
        response.put("processTypes", employee.getProcessTypes());
        response.put("kpi", employee.getKpi());
        response.put("feedbackMessage", employee.getFeedbackMessage());
        response.put("feedbackDate", employee.getFeedbackDate());
        response.put("totalTasksCompleted", employee.getTaskRecords().size());

        return ResponseEntity.ok(response);
    }

    // ==================== MANAGER OVERVIEW ====================

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/manager/overview")
    public ResponseEntity<?> getManagerOverview() {
        List<Employee> employees = employeeRepository.findAll();

        if (employees.isEmpty()) {
            return ResponseEntity.ok(Map.of("message", "No employees found"));
        }

        double avgKpi = employees.stream()
                .mapToDouble(Employee::getKpi)
                .average()
                .orElse(0.0);

        List<String> alerts = employees.stream()
                .filter(e -> e.getKpi() < 60)
                .map(e -> "⚠️ " + e.getName() + " - KPI " + e.getKpi() + " (" + e.getFeedbackMessage() + ")")
                .collect(Collectors.toList());

        List<Map<String, Object>> employeeList = employees.stream()
                .map(e -> Map.of(
                        "name", e.getName(),
                        "shift", e.getShift() != null ? e.getShift().getShiftName() : "No shift",
                        "processTypes", e.getProcessTypes(),
                        "kpi", e.getKpi(),
                        "feedbackMessage", e.getFeedbackMessage()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(Map.of(
                "date", LocalDate.now(),
                "totalEmployees", employees.size(),
                "averageKPI", Math.round(avgKpi * 10.0) / 10.0,
                "alerts", alerts,
                "employees", employeeList
        ));
    }
}