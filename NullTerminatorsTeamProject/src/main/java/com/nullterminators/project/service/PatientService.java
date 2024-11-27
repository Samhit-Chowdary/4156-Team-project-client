package com.nullterminators.project.service;

import com.nullterminators.project.model.Patient;
import com.nullterminators.project.model.PatientRecords;
import com.nullterminators.project.repository.PatientRecordsRepository;
import com.nullterminators.project.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import java.time.LocalDate;
import java.util.List;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientRecordsRepository patientRecordsRepository;

    public Pair<String, String> createPatient(Patient patient) {
        try {
            patient.setId(null);
            patientRepository.save(patient);
            return Pair.of(patient.getId().toString(), "");
        }
        catch (DataIntegrityViolationException | TransactionSystemException e) {
            System.out.println(e);
            return Pair.of("", "phone number already exists or fields are empty");
        }
    }

    public boolean updatePatient(Patient patient) {
        try {
            patientRepository.save(patient);
            return true;
        }
        catch (DataIntegrityViolationException | TransactionSystemException e) {
            System.out.println(e);
            return false;
        }
    }

    public Patient getPatientById(Integer id) {
        return patientRepository.findById(id).orElse(null);
    }

    public void deletePatientById(Integer id) {
        patientRepository.deleteById(id);
    }

    public Pair<String, String> createPatientRecords(PatientRecords record) {
        try {
            record.setId(null);
            record.setDate(LocalDate.now());
            patientRecordsRepository.save(record);
            return Pair.of(record.getId().toString(), "");
        }
        catch (DataIntegrityViolationException | TransactionSystemException e) {
            System.out.println(e);
            return Pair.of("", "fields are missing");
        }
    }

    public PatientRecords getPatientRecordsById(Integer id) {
        return patientRecordsRepository.findById(id).orElse(null);
    }

    public List<PatientRecords> getPatientRecordsByPatientId(Integer id) {
        return patientRecordsRepository.findAllByPatientId(id);
    }

    public List<PatientRecords> getPatientRecordsByDoctorId(Integer id) {
        return patientRecordsRepository.findAllByDoctorId(id);
    }

    public void deletePatientRecordsById(Integer id) {
        patientRecordsRepository.deleteById(id);
    }

    public boolean updatePatientRecords(PatientRecords record) {
        try {
            patientRecordsRepository.save(record);
            return true;
        }
        catch (DataIntegrityViolationException | TransactionSystemException e) {
            System.out.println(e);
            return false;
        }
    }
}
