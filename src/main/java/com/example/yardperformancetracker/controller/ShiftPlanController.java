package com.example.yardperformancetracker.controller;
import com.example.yardperformancetracker.dto.ShiftPlanDTO;
import com.example.yardperformancetracker.entity.ShiftPlan;
import com.example.yardperformancetracker.mapper.ShiftPlanMapper;
import com.example.yardperformancetracker.service.ShiftPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/shift-plans")
@RequiredArgsConstructor
public class ShiftPlanController {

    private final ShiftPlanService service;
    private final ShiftPlanMapper mapper;


    @GetMapping
    public List<ShiftPlanDTO> getAll() {
        return service.getAllPlans().stream()
                .map(plan -> mapper.toDTO(plan))
                .collect(Collectors.toList());
    }


    @GetMapping("/today")
    public List<ShiftPlanDTO> getTodayPlans() {
        return service.getTodayPlans().stream()
                .map(plan -> mapper.toDTO(plan)) // ✅ виправлено
                .collect(Collectors.toList());
    }


    @PostMapping
    public ShiftPlan create(@RequestBody ShiftPlan plan) {
        return service.create(plan);
    }


    @PutMapping("/{id}")
    public ShiftPlan update(@PathVariable Long id, @RequestBody ShiftPlan plan) {
        return service.update(id, plan);
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}