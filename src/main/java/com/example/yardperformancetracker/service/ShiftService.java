package com.example.yardperformancetracker.service;
import com.example.yardperformancetracker.entity.Employee;
import com.example.yardperformancetracker.entity.Shift;
import com.example.yardperformancetracker.repository.EmployeeRepository;
import com.example.yardperformancetracker.repository.ShiftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShiftService {

    private final ShiftRepository shiftRepo;
    private final EmployeeRepository employeeRepo;

    public double getAverageKpiForShift(Long shiftId) {
        Shift shift = shiftRepo.findById(shiftId)
                .orElseThrow(() -> new RuntimeException("Shift not found"));
        List<Employee> employees = employeeRepo.findByShift(shift);

        if (employees.isEmpty()) return 0.0;

        double avg = employees.stream()
                .mapToDouble(Employee::getKpi)
                .average()
                .orElse(0.0);
        return Math.round(avg * 100.0) / 100.0;
    }
}