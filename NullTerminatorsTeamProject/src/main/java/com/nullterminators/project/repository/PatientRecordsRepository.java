package com.nullterminators.project.repository;

import com.nullterminators.project.model.PatientRecords;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRecordsRepository extends JpaRepository<PatientRecords, Integer> {}
