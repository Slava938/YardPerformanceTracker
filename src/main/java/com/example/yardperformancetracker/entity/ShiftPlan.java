package com.example.yardperformancetracker.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor

@NoArgsConstructor

@Table(name ="shift_plans")
public class ShiftPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String planName;

    private LocalDate date;
    private String taskDescription;

    @ManyToOne
    @JoinColumn(name = "shift_id")
    private Shift shift;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
private  int plannedEmployees;
}
