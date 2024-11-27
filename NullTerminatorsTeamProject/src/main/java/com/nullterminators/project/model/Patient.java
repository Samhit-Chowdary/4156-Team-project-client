package com.nullterminators.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class Patient {
    @Id
    @SequenceGenerator(name = "patientIdSeq", sequenceName = "patient_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "patientIdSeq")
    private Integer id;

    @NotNull
    private String name;

    @Column(unique = true)
    private String phoneNumber;

    @NotNull
    private Integer age;
}
