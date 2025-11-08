package com.example.yardperformancetracker.service;

import com.example.yardperformancetracker.entity.ShiftPlan;
import com.example.yardperformancetracker.repository.ShiftPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShiftPlanService {

    private final ShiftPlanRepository repo;


    public List<ShiftPlan> getAllPlans() {
        return repo.findAll();
    }


    public List<ShiftPlan> getTodayPlans() {
        return repo.findByDate(LocalDate.now());
    }


    public ShiftPlan create(ShiftPlan plan) {
        return repo.save(plan);
    }


    public ShiftPlan update(Long id, ShiftPlan data) {
        ShiftPlan p = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("ShiftPlan not found with id: " + id));

        p.setDate(data.getDate());
        p.setShift(data.getShift());
        p.setPlannedEmployees(data.getPlannedEmployees());
        return repo.save(p);
    }


    public void delete(Long id) {
        repo.deleteById(id);
    }
}