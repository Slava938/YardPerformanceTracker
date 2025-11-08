package com.example.yardperformancetracker.entity;
import com.example.yardperformancetracker.entity.enums.ShiftType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "shifts")
public class Shift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private String shiftName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private ShiftType type;

    @ManyToOne
    private Manager manager;

    @OneToMany(mappedBy = "shift")
    private List<Employee> employees;

    @OneToMany(mappedBy = "shift")
    private List<TaskRecord> taskRecords;

    @OneToMany(mappedBy = "shift")
    private List<ShiftPlan> shiftPlans;


    public Shift(Long id, LocalDate date, String shiftName,
                 LocalDateTime startTime, LocalDateTime endTime, ShiftType type) {
        this.id = id;
        this.date = date;
        this.shiftName = shiftName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.type = type;
    }
}