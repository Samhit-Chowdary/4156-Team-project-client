package com.nullterminators.project.controller;

import com.nullterminators.project.model.Patient;
import com.nullterminators.project.model.PatientRecords;
import com.nullterminators.project.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class PatientController {

    @Autowired
    private PatientService patientService;

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<?> getPatientById(@PathVariable(value = "patientId") Integer patientId) {
        Patient patient = patientService.getPatientById(patientId);
        if (patient == null) {
            return new ResponseEntity<>(
                    Map.of("error", "patient not found"),
                    HttpStatus.NOT_FOUND
            );
        }
        return new ResponseEntity<>(
                Map.of("response", patientService.getPatientById(patientId)),
                HttpStatus.OK
        );
    }

    @PatchMapping("/patient/{patientId}/updatePhoneNumber")
    public ResponseEntity<?> updatePatientPhoneNumberById(@PathVariable(value = "patientId") Integer patientId, @RequestBody Map<String, String> body) {
        try {
            Patient patient = patientService.getPatientById(patientId);
            if (patient == null) {
                return new ResponseEntity<>(
                        Map.of("error", "patient not found"),
                        HttpStatus.NOT_FOUND
                );
            }

            String phoneNumber = body.get("phoneNumber");
            if (phoneNumber == null || phoneNumber.isEmpty()) {
                return new ResponseEntity<>(
                        Map.of("error", "phone number is empty"),
                        HttpStatus.BAD_REQUEST
                );
            }

            patient.setPhoneNumber(body.get("phoneNumber"));
            if(patientService.updatePatient(patient)) {
                return new ResponseEntity<>(
                        Map.of("response", "patient phone number is updated successfully."),
                        HttpStatus.OK
                );
            }

            return new ResponseEntity<>(
                    Map.of("error", "phone number already exists"),
                    HttpStatus.BAD_REQUEST
            );
        }
        catch (Exception e) {
            return handleException(e);
        }
    }

    @DeleteMapping("/patient/{patientId}")
    public ResponseEntity<?> deletePatientById(@PathVariable(value = "patientId") Integer patientId) {
        try {
            Patient patient = patientService.getPatientById(patientId);
            if (patient == null) {
                return new ResponseEntity<>(
                        Map.of("error", "patient not found"),
                        HttpStatus.NOT_FOUND
                );
            }

            patientService.deletePatientById(patientId);
            return new ResponseEntity<>(
                    Map.of("response", "patient is deleted successfully."),
                    HttpStatus.OK
            );
        }
        catch (Exception e) {
            return handleException(e);
        }
    }

    @PostMapping("/patient")
    public ResponseEntity<?> createPatient(@RequestBody Patient patient) {
        try {
            Pair<String, String> response = patientService.createPatient(patient);
            if (response.getSecond().isEmpty()) {
                return new ResponseEntity<>(
                        Map.of("response", "patient is created successfully.", "id", response.getFirst()),
                        HttpStatus.CREATED
                );
            }
            return new ResponseEntity<>(
                    Map.of("error", response.getSecond()),
                    HttpStatus.BAD_REQUEST
            );
        }
        catch (Exception e) {
            return handleException(e);
        }
    }

    @PostMapping("/patient/record")
    public ResponseEntity<?> createPatientRecords(@RequestBody PatientRecords record) {
        Patient patient = patientService.getPatientById(record.getPatientId());
        if (patient == null) {
            return new ResponseEntity<>(
                    Map.of("error", "patient not found"),
                    HttpStatus.NOT_FOUND
            );
        }

        //TODO: check for doctor id




    }

    private ResponseEntity<?> handleException(Exception e) {
        System.out.println(e.toString());
    return new ResponseEntity<>(
        Map.of("error", "An unknown error has occurred"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
