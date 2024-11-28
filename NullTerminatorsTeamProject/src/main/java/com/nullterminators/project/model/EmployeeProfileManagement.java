package com.nullterminators.project.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class EmployeeProfileManagement {
    @Id
    private int id;

    @Column(nullable = false)
    private String designation;
}
