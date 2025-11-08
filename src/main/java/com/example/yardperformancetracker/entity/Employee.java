package com.example.yardperformancetracker.entity;
import com.example.yardperformancetracker.entity.enums.ProcessType;
import com.example.yardperformancetracker.entity.enums.RoleType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String position;

    @ElementCollection(fetch = FetchType.EAGER, targetClass = ProcessType.class)
    @CollectionTable(name = "employee_process_types", joinColumns = @JoinColumn(name = "employee_id")
    )
    @Column(name = "process_type")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Set<ProcessType> processTypes = new HashSet<>();

    public Set<ProcessType> getProcessTypes() {
        if (processTypes == null) {
            processTypes = new HashSet<>();
        }
        return processTypes;
    }



    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskRecord>taskRecords = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "shift_id")
    private Shift shift;
    private int totalDocked;
    private int totalUndocked;
    private int totalTrailers;
    private int problemSolvingTasks;
    private int expectedTrailersPerShift;
    private double performanceScore;
    private double qualityScore;
    private double behaviorScore;
    private double kpi;
    private int safetyScore;
    private String feedbackMessage;
    private LocalDate feedbackDate;


    public String generateFeedback() {
        if (this.kpi >= 90) {
            return "🌟 Excellent performance! You consistently exceed expectations and demonstrate strong leadership potential.";
        } else if (this.kpi >= 80) {
            return "💪 Great job! Your performance is above average. Keep maintaining this level of quality and focus.";
        } else if (this.kpi >= 70) {
            return "👍 Good work! There’s room for improvement, but overall your contribution is valuable.";
        } else if (this.kpi >= 60) {
            return "⚙️ Average performance. Focus on improving quality and consistency to reach higher standards.";
        } else if (this.kpi >= 50) {
            return "⚠️ Below expectations. You should pay attention to your accuracy, quality, and communication with the team.";
        } else {
            return "🚨 Low performance detected. Manager attention required — consider coaching or retraining.";
        }
    }
}