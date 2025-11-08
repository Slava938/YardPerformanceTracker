package com.example.yardperformancetracker.service;
import com.example.yardperformancetracker.entity.Employee;
import com.example.yardperformancetracker.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final EmployeeRepository employeeRepository;

    public String generatePerformanceReport(Long employeeId) {
        Employee e = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        String performanceCategory;

        if (e.getKpi() >= 85)
            performanceCategory = "Excellent performance. Keep it up!";
        else if (e.getKpi() >= 60)
            performanceCategory = "Average performance. Some improvements needed.";
        else
            performanceCategory = "Low performance. Please review training or task assignment.";

        e.setFeedbackMessage(performanceCategory);
        e.setFeedbackDate(LocalDate.now());
        employeeRepository.save(e);

        return String.format("Employee: %s | KPI: %.2f | Status: %s",
                e.getName(), e.getKpi(), performanceCategory);
    }
}