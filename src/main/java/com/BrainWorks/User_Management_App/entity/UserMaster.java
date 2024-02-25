package com.BrainWorks.User_Management_App.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
@Data
@Entity
@Table(name = "User_Master")
public class UserMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String fullName;
    private Long mobile;
    private String email;
    private String gender;
    private LocalDate dob;
    private Long ssn;
    private String password;
    private String accStatus;
    private String createdBy;
    private String updatedBy;
    @CreationTimestamp
    private LocalDate createdDate;
    @UpdateTimestamp
    private LocalDate updatedDate;
}
