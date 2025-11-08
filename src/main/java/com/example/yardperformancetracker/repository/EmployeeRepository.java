package com.example.yardperformancetracker.repository;


import com.example.yardperformancetracker.entity.Employee;
import com.example.yardperformancetracker.entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
@Query("SELECT DISTINCT e FROM Employee e LEFT JOIN FETCH e.processTypes")
List<Employee>findAllWithProcesses();
    List<Employee> findByShift(Shift shift);
    Optional<Employee> findByName(String name);
}
