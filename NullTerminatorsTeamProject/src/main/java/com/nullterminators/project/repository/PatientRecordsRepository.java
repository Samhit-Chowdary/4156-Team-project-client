package com.nullterminators.project.repository;

import com.nullterminators.project.model.PatientRecords;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientRecordsRepository extends JpaRepository<PatientRecords, Integer> {
    List<PatientRecords> findAllByPatientId(Integer patientId);

    List<PatientRecords> findAllByDoctorId(Integer doctorId);
}
