package com.example.yardperformancetracker.service;
import com.example.yardperformancetracker.entity.Employee;
import com.example.yardperformancetracker.entity.Shift;
import com.example.yardperformancetracker.entity.enums.ProcessType;
import com.example.yardperformancetracker.entity.enums.ShiftType;
import com.example.yardperformancetracker.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;
@Service
@RequiredArgsConstructor
public class DashboardService {

    private final EmployeeRepository employeeRepo;

    /* =======================
          DASHBOARD SUMMARY
       ======================= */
    public Map<String, Object> getSummary() {
        List<Employee> employees = employeeRepo.findAll();
        if (employees.isEmpty()) {
            return Map.of("message", "No employees found in the system.");
        }

        double averageKpi = employees.stream()
                .mapToDouble(Employee::getKpi)
                .average()
                .orElse(0.0);

        Employee topPerformer = employees.stream()
                .max(Comparator.comparingDouble(Employee::getKpi))
                .orElse(null);

        Employee lowestPerformer = employees.stream()
                .min(Comparator.comparingDouble(Employee::getKpi))
                .orElse(null);

        Map<String, Double> processAverages = new HashMap<>();
        for (ProcessType type : ProcessType.values()) {
            double avg = employees.stream()
                    .filter(e -> e.getProcessTypes() != null && e.getProcessTypes().contains(type))
                    .mapToDouble(Employee::getKpi)
                    .average()
                    .orElse(0.0);
            processAverages.put(type.name(), round1(avg));
        }

        List<String> alerts = new ArrayList<>();
        for (Employee e : employees) {
            if (e.getKpi() < 50) {
                alerts.add("[ALERT] Low KPI detected for " + e.getName()
                        + " (" + round1(e.getKpi()) + "%)");
            }
        }

        return Map.of(
                "totalEmployees", employees.size(),
                "averageKPI", round1(averageKpi),
                "averageByProcess", processAverages,
                "topPerformer", topPerformer != null ? topPerformer.getName() : "N/A",
                "lowestPerformer", lowestPerformer != null ? lowestPerformer.getName() : "N/A",
                "alerts", alerts
        );
    }

    /* =======================
        PROCESS STATS BY TYPE
       ======================= */
    public Map<String, Object> getProcessStats(String processTypeName) {
        List<Employee> employees = employeeRepo.findAll();
        if (employees.isEmpty()) {
            return Map.of("message", "No employees found in the system.");
        }

        ProcessType processType = ProcessType.valueOf(processTypeName.toUpperCase());
        List<Employee> filtered = new ArrayList<>();
        for (Employee e : employees) {
            if (e.getProcessTypes() != null && e.getProcessTypes().contains(processType)) {
                filtered.add(e);
            }
        }

        if (filtered.isEmpty()) {
            return Map.of("message", "No employees found for process type: " + processTypeName);
        }

        double avgKpi = filtered.stream().mapToDouble(Employee::getKpi).average().orElse(0.0);
        Employee top = filtered.stream().max(Comparator.comparingDouble(Employee::getKpi)).orElse(null);
        Employee low = filtered.stream().min(Comparator.comparingDouble(Employee::getKpi)).orElse(null);


        List<Map<String, Object>> members = new ArrayList<>();
        for (Employee e : filtered) {
            Map<String, Object> m = new HashMap<>();
            m.put("name", e.getName());
            m.put("position", e.getPosition());
            m.put("kpi", round1(e.getKpi()));
            m.put("feedback", e.getFeedbackMessage());
            members.add(m);
        }

        return Map.of(
                "processType", processType.name(),
                "employeeCount", filtered.size(),
                "averageKPI", round1(avgKpi),
                "topPerformer", top != null ? top.getName() : "N/A",
                "lowestPerformer", low != null ? low.getName() : "N/A",
                "members", members
        );
    }

    /* =======================
          SHIFT STATS (ALL)
       ======================= */
    public Map<String, Object> getShiftStats() {
        List<Employee> employees = employeeRepo.findAll();
        if (employees.isEmpty()) {
            return Map.of("message", "No employees found in the system.");
        }

        Map<String, Double> shiftAverages = new HashMap<>();
        Map<String, Long> shiftCounts = new HashMap<>();

        for (ShiftType st : ShiftType.values()) {
            double avg = employees.stream()
                    .filter(e -> e.getShift() != null && e.getShift().getType() == st)
                    .mapToDouble(Employee::getKpi)
                    .average()
                    .orElse(0.0);
            long count = employees.stream()
                    .filter(e -> e.getShift() != null && e.getShift().getType() == st)
                    .count();

            shiftAverages.put(st.name(), round1(avg));
            shiftCounts.put(st.name(), count);
        }

        return Map.of(
                "shiftAverages", shiftAverages,
                "shiftCounts", shiftCounts
        );
    }

    /* ===============================
       SHIFT STATS (BY CONCRETE SHIFT)
       =============================== */
    public Map<String, Object> getShiftStats(String shiftTypeName) {
        List<Employee> employees = employeeRepo.findAll();
        if (employees.isEmpty()) {
            return Map.of("message", "No employees found in the system.");
        }

        ShiftType shiftType = ShiftType.valueOf(shiftTypeName.toUpperCase());
        List<Employee> filtered = new ArrayList<>();
        for (Employee e : employees) {
            Shift s = e.getShift();
            if (s != null && s.getType() == shiftType) {
                filtered.add(e);
            }
        }

        if (filtered.isEmpty()) {
            return Map.of("message", "No employees found for shift: " + shiftType.name());
        }

        double avgKpi = filtered.stream().mapToDouble(Employee::getKpi).average().orElse(0.0);
        Employee top = filtered.stream().max(Comparator.comparingDouble(Employee::getKpi)).orElse(null);
        Employee low = filtered.stream().min(Comparator.comparingDouble(Employee::getKpi)).orElse(null);

        List<Map<String, Object>> members = new ArrayList<>();
        for (Employee e : filtered) {
            Map<String, Object> m = new HashMap<>();
            m.put("name", e.getName());
            m.put("position", e.getPosition());
            m.put("kpi", round1(e.getKpi()));
            m.put("feedback", e.getFeedbackMessage());
            members.add(m);
        }

        return Map.of(
                "shiftType", shiftType.name(),
                "employeeCount", filtered.size(),
                "averageKPI", round1(avgKpi),
                "topPerformer", top != null ? top.getName() : "N/A",
                "lowestPerformer", low != null ? low.getName() : "N/A",
                "members", members
        );
    }

    /* ===== Helpers ===== */
    private static double round1(double v) {
        return Math.round(v * 10.0) / 10.0;
    }
}