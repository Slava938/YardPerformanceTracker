package com.example.yardperformancetracker.service;
import com.example.yardperformancetracker.entity.Employee;
import com.example.yardperformancetracker.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository repo;

    public List<Employee> findAll() {
        return repo.findAll();
    }

    public Employee findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    public Employee create(Employee e) {
        calculateAndSetKpi(e);
        e.setFeedbackMessage(e.generateFeedback());
        e.setFeedbackDate(LocalDate.now());
        return repo.save(e);
    }

    public Employee update(Long id, Employee data) {
        Employee e = findById(id);
        e.setName(data.getName());
        e.setRoleType(data.getRoleType());
        e.setPerformanceScore(data.getPerformanceScore());
        e.setQualityScore(data.getQualityScore());
        e.setBehaviorScore(data.getBehaviorScore());
        e.setProblemSolvingTasks(data.getProblemSolvingTasks());
        calculateAndSetKpi(e);
        e.setFeedbackMessage(e.generateFeedback());
        e.setFeedbackDate(LocalDate.now());
        return repo.save(e);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    // --- KPI Calculation ---
    private void calculateAndSetKpi(Employee e) {
        double kpi = e.getPerformanceScore() * 0.6
                + e.getQualityScore() * 0.3
                + e.getBehaviorScore() * 0.1
                + e.getProblemSolvingTasks() * 2.0;
        e.setKpi(Math.round(kpi * 100.0) / 100.0);
    }
}