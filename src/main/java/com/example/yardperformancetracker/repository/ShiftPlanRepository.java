package com.example.yardperformancetracker.repository;
import com.example.yardperformancetracker.entity.ShiftPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ShiftPlanRepository extends JpaRepository<ShiftPlan, Long> {
    List<ShiftPlan> findByDate(LocalDate date);
}
