package com.example.yardperformancetracker.user;
import com.example.yardperformancetracker.entity.Employee;
import com.example.yardperformancetracker.entity.Shift;
import com.example.yardperformancetracker.entity.ShiftPlan;
import com.example.yardperformancetracker.entity.TaskRecord;
import com.example.yardperformancetracker.entity.enums.ProcessType;
import com.example.yardperformancetracker.entity.enums.RoleType;
import com.example.yardperformancetracker.entity.enums.ShiftType;
import com.example.yardperformancetracker.repository.EmployeeRepository;
import com.example.yardperformancetracker.repository.ShiftPlanRepository;
import com.example.yardperformancetracker.repository.ShiftRepository;
import com.example.yardperformancetracker.repository.TaskRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DemoDataLoader implements CommandLineRunner {

    private final UserRepository userRepo;
    private final EmployeeRepository employeeRepo;
    private final ShiftRepository shiftRepo;
    private final TaskRecordRepository taskRecordRepo;
    private final PasswordEncoder encoder;
    private final ShiftPlanRepository shiftPlanRepo;

    @Override
    public void run(String... args) {

        // === 1️⃣ LOGIN USERS ===
        if (userRepo.count() == 0) {
            UserEntity manager = new UserEntity("manager", encoder.encode("12345"), Role.ROLE_MANAGER);
            UserEntity employee = new UserEntity("employee", encoder.encode("12345"), Role.ROLE_EMPLOYEE);
            userRepo.saveAll(List.of(manager, employee));
            System.out.println("✅ Demo users created");
        }

        // === 2️⃣ SHIFTS WITH REAL HOURS ===
        if (shiftRepo.count() == 0) {
            Shift morning = new Shift(null, LocalDate.now(), "Morning Shift",
                    LocalDateTime.of(LocalDate.now(), java.time.LocalTime.of(6, 30)),
                    LocalDateTime.of(LocalDate.now(), java.time.LocalTime.of(14, 30)),
                    ShiftType.MORNING);

            Shift twilight = new Shift(null, LocalDate.now(), "Twilight Shift",
                    LocalDateTime.of(LocalDate.now(), java.time.LocalTime.of(11, 30)),
                    LocalDateTime.of(LocalDate.now(), java.time.LocalTime.of(19, 30)),
                    ShiftType.TWILIGHT);

            Shift late = new Shift(null, LocalDate.now(), "Late Shift",
                    LocalDateTime.of(LocalDate.now(), java.time.LocalTime.of(14, 30)),
                    LocalDateTime.of(LocalDate.now(), java.time.LocalTime.of(22, 30)),
                    ShiftType.LATE);

            Shift down = new Shift(null, LocalDate.now(), "Down Shift",
                    LocalDateTime.of(LocalDate.now(), java.time.LocalTime.of(19, 30)),
                    LocalDateTime.of(LocalDate.now().plusDays(1), java.time.LocalTime.of(3, 30)),
                    ShiftType.DOWN);

            Shift night = new Shift(null, LocalDate.now(), "Night Shift",
                    LocalDateTime.of(LocalDate.now(), java.time.LocalTime.of(22, 30)),
                    LocalDateTime.of(LocalDate.now().plusDays(1), java.time.LocalTime.of(6, 30)),
                    ShiftType.NIGHT);

            shiftRepo.saveAll(List.of(morning, twilight, late, down, night));
            System.out.println("✅ Shifts created with real timing");


            ShiftPlan plan1 = new ShiftPlan();
            plan1.setPlanName("Morning Plan");
            plan1.setDate(LocalDate.now());
            plan1.setTaskDescription("Unload inbound trailers");
            plan1.setShift(morning );
            plan1.setPlannedEmployees(5);
            shiftPlanRepo.save(plan1);

            ShiftPlan plan2 = new ShiftPlan();
            plan2.setPlanName("Night Plan");
            plan2.setDate(LocalDate.now());
            plan2.setTaskDescription("Load outbound trailers");
            plan2.setShift(night);
            plan2.setPlannedEmployees(3);
            shiftPlanRepo.save(plan2);
        }



        // === 3️⃣ EMPLOYEES ===
        if (employeeRepo.count() == 0) {
            Shift morning = shiftRepo.findByType(ShiftType.MORNING).orElse(null);
            Shift twilight = shiftRepo.findByType(ShiftType.TWILIGHT).orElse(null);
            Shift late = shiftRepo.findByType(ShiftType.LATE).orElse(null);
            Shift down = shiftRepo.findByType(ShiftType.DOWN).orElse(null);
            Shift night = shiftRepo.findByType(ShiftType.NIGHT).orElse(null);

            Employee john = Employee.builder()
                    .name("John")
                    .position("Yard Marshal")
                    .roleType(RoleType.YARD_MARSHAL)
                    .shift(morning)
                    .performanceScore(82)
                    .qualityScore(90)
                    .behaviorScore(78)
                    .problemSolvingTasks(5)
                    .processTypes(Set.of(ProcessType.OUTDOOR))
                    .build();

            Employee anna = Employee.builder()
                    .name("Anna")
                    .position("Gatehouse")
                    .roleType(RoleType.GATEHOUSE_AGENT)
                    .shift(twilight)
                    .performanceScore(80)
                    .qualityScore(85)
                    .behaviorScore(88)
                    .problemSolvingTasks(3)
                    .processTypes(Set.of(ProcessType.GATEHOUSE))
                    .build();

            Employee mike = Employee.builder()
                    .name("Mike")
                    .position("Exit Controller")
                    .roleType(RoleType.EXIT_CONTROLLER)
                    .shift(late)
                    .performanceScore(65)
                    .qualityScore(70)
                    .behaviorScore(72)
                    .problemSolvingTasks(1)
                    .processTypes(Set.of(ProcessType.EXIT))
                    .build();

            Employee sarah = Employee.builder()
                    .name("Sarah")
                    .position("Clerk")
                    .roleType(RoleType.CLERK)
                    .shift(down)
                    .performanceScore(55)
                    .qualityScore(65)
                    .behaviorScore(60)
                    .problemSolvingTasks(0)
                    .processTypes(Set.of(ProcessType.INDOOR))
                    .build();

            Employee alex = Employee.builder()
                    .name("Alex")
                    .position("Yard Marshal")
                    .roleType(RoleType.YARD_MARSHAL)
                    .shift(night)
                    .performanceScore(95)
                    .qualityScore(93)
                    .behaviorScore(92)
                    .problemSolvingTasks(8)
                    .processTypes(Set.of(ProcessType.OUTDOOR))
                    .build();

            employeeRepo.saveAll(List.of(john, anna, mike, sarah, alex));
            System.out.println("✅ Employees created");
        }

        // === 4️⃣ TASK RECORDS ===
        if (taskRecordRepo.count() == 0) {
            Employee john = employeeRepo.findByName("John").orElseThrow();

            TaskRecord t1 = TaskRecord.builder()
                    .trailerId("TR-207")
                    .driverId("DRV-112")
                    .dockDoor("207")
                    .processType(ProcessType.OUTDOOR)
                    .safetyScore(95)
                    .qualityScore(0)
                    .problemReported(false)
                    .startTime(LocalDateTime.now().minusMinutes(25))
                    .endTime(LocalDateTime.now())
                    .employee(john)
                    .build();

            TaskRecord t2 = TaskRecord.builder()
                    .trailerId("TR-208")
                    .driverId("DRV-113")
                    .dockDoor("208")
                    .processType(ProcessType.INDOOR)
                    .safetyScore(0)
                    .qualityScore(88)
                    .problemReported(false)
                    .startTime(LocalDateTime.now())
                    .endTime(LocalDateTime.now().plusMinutes(15))
                    .employee(john)
                    .build();

            taskRecordRepo.saveAll(List.of(t1, t2));
            System.out.println("✅ Task records created for John");
        }

        // === 5️⃣ KPI CALCULATION & FEEDBACK SYSTEM ===
        employeeRepo.findAll().forEach(this::calculateKpi);
    }

    // === KPI FORMULA ===
    private void calculateKpi(Employee e) {
        double kpi = e.getPerformanceScore() * 0.5
                + e.getQualityScore() * 0.3
                + e.getBehaviorScore() * 0.2
                + e.getProblemSolvingTasks() * 2.0;

        kpi = Math.round(kpi * 10.0) / 10.0;
        e.setKpi(kpi);

        // === Feedback messages ===
        if (kpi >= 90) {
            e.setFeedbackMessage("🌟 Excellent performance! Keep up the great work!");
        } else if (kpi >= 75) {
            e.setFeedbackMessage("💪 Good job! Stay consistent and maintain your level!");
        } else if (kpi >= 50) {
            e.setFeedbackMessage("⚙️ Average performance. Try to improve your consistency.");
        } else {
            e.setFeedbackMessage("🚨 Manager Alert: Low KPI detected! Review employee performance!");
        }

        e.setFeedbackDate(LocalDate.now());
        employeeRepo.save(e);
    }
}