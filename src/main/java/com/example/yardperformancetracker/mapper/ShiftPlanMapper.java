package com.example.yardperformancetracker.mapper;
import com.example.yardperformancetracker.dto.ShiftPlanDTO;
import com.example.yardperformancetracker.entity.Employee;
import com.example.yardperformancetracker.entity.ShiftPlan;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ShiftPlanMapper {

    public ShiftPlanDTO toDTO(ShiftPlan plan) {
        if (plan == null) return null;

        String shiftName = plan.getShift() != null ? plan.getShift().getShiftName() : "N/A";
        String shiftType = plan.getShift() != null && plan.getShift().getType() != null
                ? plan.getShift().getType().name() : "N/A";

        List<String> employeeNames = (plan.getShift() != null && plan.getShift().getEmployees() != null)
                ? plan.getShift().getEmployees().stream()
                .map(Employee::getName)
                .collect(Collectors.toList())
                : List.of();

        return new ShiftPlanDTO(
                plan.getId(),
                plan.getPlanName(),
                plan.getDate(),
                plan.getTaskDescription(),
                shiftName,
                shiftType,
                employeeNames,
                plan.getPlannedEmployees()
        );
    }
}

