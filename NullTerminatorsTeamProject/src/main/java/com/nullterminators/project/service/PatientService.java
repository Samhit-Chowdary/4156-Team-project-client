package com.nullterminators.project.service;

import com.nullterminators.project.model.Patient;
import com.nullterminators.project.repository.PatientRecordsRepository;
import com.nullterminators.project.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

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
}
