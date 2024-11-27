package com.nullterminators.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class PatientRecords {
    @Id
    @SequenceGenerator(name = "patientRecordIdSeq", sequenceName = "patient_record_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "patientRecordIdSeq")
    private Integer id;

    @NotNull
    private Integer doctorId;

    @NotNull
    private Integer patientId;

    @NotNull
    private LocalDate date;

    @NotNull
    private String prescription;

    @NotNull
    private String notes;
}
