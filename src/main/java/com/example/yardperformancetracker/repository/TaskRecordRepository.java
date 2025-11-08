package com.example.yardperformancetracker.repository;
import com.example.yardperformancetracker.entity.Employee;
import com.example.yardperformancetracker.entity.TaskRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRecordRepository extends JpaRepository<TaskRecord, Long> {
    List<TaskRecord> findByEmployee(Employee employee);
}
