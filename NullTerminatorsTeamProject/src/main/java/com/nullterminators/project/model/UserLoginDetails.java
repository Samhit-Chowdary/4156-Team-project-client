package com.nullterminators.project.model;


import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
public class UserLoginDetails implements Serializable {
    @Id
    @SequenceGenerator(name = "userIdSeq", sequenceName = "user_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userIdSeq")
    private Integer id;

    @Column(unique = true)
    private Integer employeeId;

    @Column(unique = true)
    private String username;

    private String password;

    private String role;
}
