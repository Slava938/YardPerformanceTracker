package com.example.yardperformancetracker.controller;
import com.example.yardperformancetracker.entity.Employee;
import com.example.yardperformancetracker.entity.TaskRecord;
import com.example.yardperformancetracker.repository.EmployeeRepository;
import com.example.yardperformancetracker.repository.TaskRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/self")
@RequiredArgsConstructor
public class EmployeeSelfController {

    private final EmployeeRepository employeeRepo;
    private final TaskRecordRepository taskRecordRepo;

    // 🔹 Основна сторінка працівника
    @GetMapping("/{id}")
    public Map<String, Object> getSelfDashboard(@PathVariable Long id) {
        Employee e = employeeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("name", e.getName());
        result.put("role", e.getRoleType() != null ? e.getRoleType().name() : "N/A");
        result.put("shift", e.getShift() != null ? e.getShift().getShiftName() : "N/A");
        result.put("kpi", e.getKpi());
        result.put("feedback", e.getFeedbackMessage());
        result.put("feedbackDate", e.getFeedbackDate());
        result.put("processTypes", e.getProcessTypes());
        result.put("problemSolvingTasks", e.getProblemSolvingTasks());

        return result;
    }


    @GetMapping("/{id}/tasks")
    public List<Map<String, Object>> getEmployeeTasks(@PathVariable Long id) {
        Employee e = employeeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        List<TaskRecord> tasks = taskRecordRepo.findByEmployee(e);
        if (tasks.isEmpty()) {
            return List.of(Map.of("message", "No tasks found for this employee"));
        }

        List<Map<String, Object>> list = new ArrayList<>();
        for (TaskRecord t : tasks) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("taskId", t.getId());
            m.put("trailerId", t.getTrailerId());
            m.put("dockDoor", t.getDockDoor());
            m.put("processType", t.getProcessType() != null ? t.getProcessType().name() : "N/A");
            m.put("safetyScore", t.getSafetyScore());
            m.put("qualityScore", t.getQualityScore());
            m.put("problemReported", t.isProblemReported());
            m.put("startTime", t.getStartTime());
            m.put("endTime", t.getEndTime());
            list.add(m);
        }
        return list;
    }


    @GetMapping("/{id}/summary")
    public Map<String, Object> getEmployeeSummary(@PathVariable Long id) {
        Employee e = employeeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        return Map.of(
                "employee", e.getName(),
                "role", e.getRoleType() != null ? e.getRoleType().name() : "N/A",
                "shift", e.getShift() != null ? e.getShift().getShiftName() : "N/A",
                "totalTasks", taskRecordRepo.findByEmployee(e).size(),
                "kpi", e.getKpi(),
                "feedback", e.getFeedbackMessage()
        );
    }
}
