package com.example.yardperformancetracker.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class ShiftPlanDTO {
    private Long id;
    private String planName;
    private LocalDate date;
    private String taskDescription;
    private String shiftName;
    private String shiftType;
    private List<String> employeeNames;
    private int plannedEmployees;
}