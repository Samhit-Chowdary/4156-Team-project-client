package com.nullterminators.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class PayrollRequests {
    @Id
    @SequenceGenerator(name = "payrollRequestsIdSeq", sequenceName = "payroll_requests_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payrollRequestsIdSeq")
    private Integer id;

    @NotNull
    private Integer empId;

    @NotNull
    private String reason;

    @NotNull
    private String mReason;

    @NotNull
    private LocalDate createdDate;

    @NotNull
    private int approved;
}
