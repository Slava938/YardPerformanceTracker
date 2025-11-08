package com.example.yardperformancetracker.user;
import com.example.yardperformancetracker.entity.Employee;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    public  UserEntity(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

}