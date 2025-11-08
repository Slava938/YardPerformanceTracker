package com.example.yardperformancetracker.repository;
import com.example.yardperformancetracker.entity.Shift;
import com.example.yardperformancetracker.entity.enums.ShiftType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ShiftRepository extends JpaRepository<Shift, Long> {
    Optional<Shift> findByType(ShiftType type);
}
