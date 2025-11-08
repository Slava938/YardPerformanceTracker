package com.example.yardperformancetracker.service;
import com.example.yardperformancetracker.entity.Employee;
import com.example.yardperformancetracker.entity.TaskRecord;
import com.example.yardperformancetracker.repository.EmployeeRepository;
import com.example.yardperformancetracker.repository.TaskRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KpiCalculationService {

    private final EmployeeRepository employeeRepo;
    private final TaskRecordRepository taskRepo;


    public void recalculateAll() {
        List<Employee> employees = employeeRepo.findAll();
        employees.forEach(this::calculateEmployeeKpi);
    }


    public void calculateEmployeeKpi(Employee e) {
        List<TaskRecord> tasks = taskRepo.findByEmployee(e);
        if (tasks.isEmpty()) return;

        double totalScore = 0;
        for (TaskRecord task : tasks) {
            double baseScore = switch (task.getProcessType()) {
                case OUTDOOR -> 0.5;
                case INDOOR -> 0.4;
                case GATEHOUSE -> 0.3;
                case EXIT -> 0.3;
                default -> 0.2;
            };

            long duration = Duration.between(task.getStartTime(), task.getEndTime()).toMinutes();
            double efficiency = Math.max(0.5, Math.min(1.0, 60.0 / (duration + 1))); // нормалізація

            double taskKpi = (task.getQualityScore() * 0.5 + task.getSafetyScore() * 0.3)
                    * efficiency * (1 + baseScore);
            totalScore += taskKpi;
        }

        double avgKpi = totalScore / tasks.size();
        e.setKpi(Math.round(avgKpi * 10.0) / 10.0);
        e.setFeedbackMessage(e.generateFeedback());
        employeeRepo.save(e);
    }
}