package com.example.yardperformancetracker.entity;
import com.example.yardperformancetracker.entity.enums.ProcessType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String taskName;
    private String status;
    private String trailerId;
    private String driverId;
    private String dockDoor;
    private double safetyScore;
    private double qualityScore;
    private boolean problemReported;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @ManyToOne
    @JoinColumn(name = "shift_id")
    private Shift shift;


    @Enumerated(EnumType.STRING)
    private ProcessType processType;

    @ManyToOne
    private Employee employee;
}


