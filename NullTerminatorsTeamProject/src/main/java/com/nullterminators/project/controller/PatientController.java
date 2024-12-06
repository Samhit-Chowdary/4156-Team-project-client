package com.nullterminators.project.controller;

import com.nullterminators.project.model.Patient;
import com.nullterminators.project.model.PatientRecords;
import com.nullterminators.project.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping("/patient")
    public ResponseEntity<?> getAllPatients() {
        List<Patient> resp = patientService.getPatients();
        return new ResponseEntity<>(
                Map.of("response", resp),
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

    @GetMapping("/patient/{patientId}/records")
    public ResponseEntity<?> getPatientRecordsByPatientId(@PathVariable(value = "patientId") Integer patientId) {
        try {
            Patient patient = patientService.getPatientById(patientId);
            if (patient == null) {
                return new ResponseEntity<>(
                        Map.of("error", "patient not found"),
                        HttpStatus.NOT_FOUND
                );
            }
            return new ResponseEntity<>(
                    Map.of("response", patientService.getPatientRecordsByPatientId(patientId)),
                    HttpStatus.OK
            );
        }
        catch (Exception e) {
            return handleException(e);
        }
    }

    @GetMapping("/patient/records/getByDoctorId/{doctorId}")
    public ResponseEntity<?> getPatientRecordsByDoctorId(@PathVariable(value = "doctorId") Integer doctorId) {
        try {
            return new ResponseEntity<>(
                    Map.of("response", patientService.getPatientRecordsByDoctorId(doctorId)),
                    HttpStatus.OK
            );
        }
        catch (Exception e) {
            return handleException(e);
        }
    }

    @GetMapping("/patient/records/getAll")
    public ResponseEntity<?> getAllPatientRecords() {
        try {
            return new ResponseEntity<>(
                    Map.of("response", patientService.getPatientRecords()),
                    HttpStatus.OK
            );
        }
        catch (Exception e) {
            return handleException(e);
        }
    }

    @GetMapping("/patient/record/{Id}")
    public ResponseEntity<?> getPatientRecordsById(@PathVariable(value = "Id") Integer Id) {
        try {
            PatientRecords record = patientService.getPatientRecordsById(Id);
            if (record == null) {
                return new ResponseEntity<>(
                        Map.of("error", "patient record not found"),
                        HttpStatus.NOT_FOUND
                );
            }
            return new ResponseEntity<>(
                    Map.of("response", record),
                    HttpStatus.OK
            );
        }
        catch (Exception e) {
            return handleException(e);
        }
    }

    @DeleteMapping("/patient/record/{Id}")
    public ResponseEntity<?> deletePatientRecordsById(@PathVariable(value = "Id") Integer Id) {
        try {
            PatientRecords record = patientService.getPatientRecordsById(Id);
            if (record == null) {
                return new ResponseEntity<>(
                        Map.of("error", "patient record not found"),
                        HttpStatus.NOT_FOUND
                );
            }
            patientService.deletePatientRecordsById(Id);
            return new ResponseEntity<>(
                    Map.of("response", "patient record is deleted successfully."),
                    HttpStatus.OK
            );
        }
        catch (Exception e) {
            return handleException(e);
        }
    }

    @PatchMapping("/patient/record/{Id}/updatePrescription")
    public ResponseEntity<?> updatePatientRecordsPrescription(@PathVariable(value = "Id") Integer Id, @RequestBody Map<String, String> body) {
        try {
            PatientRecords record = patientService.getPatientRecordsById(Id);
            if (record == null) {
                return new ResponseEntity<>(
                        Map.of("error", "patient record not found"),
                        HttpStatus.NOT_FOUND
                );
            }
            record.setPrescription(body.get("prescription"));
            if (patientService.updatePatientRecords(record)) {
                return new ResponseEntity<>(
                        Map.of("response", "patient record is updated successfully."),
                        HttpStatus.OK
                );
            }
            return new ResponseEntity<>(
                    Map.of("response", "patient record update failed."),
                    HttpStatus.BAD_REQUEST
            );
        }
        catch (Exception e) {
            return handleException(e);
        }
    }

    @PatchMapping("/patient/record/{Id}/updateNotes")
    public ResponseEntity<?> updatePatientRecordsNote(@PathVariable(value = "Id") Integer Id, @RequestBody Map<String, String> body) {
        try {
            PatientRecords record = patientService.getPatientRecordsById(Id);
            if (record == null) {
                return new ResponseEntity<>(
                        Map.of("error", "patient record not found"),
                        HttpStatus.NOT_FOUND
                );
            }

            record.setNotes(body.get("notes"));
            if(patientService.updatePatientRecords(record)) {
                return new ResponseEntity<>(
                        Map.of("response", "patient record is updated successfully."),
                        HttpStatus.OK
                );
            }
            return new ResponseEntity<>(
                    Map.of("response", "patient record updated failed."),
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

        try {
            Pair<String, String> response = patientService.createPatientRecords(record);
            if (response.getSecond().isEmpty()) {
                return new ResponseEntity<>(
                        Map.of("response", "patient record is created successfully.", "id", response.getFirst()),
                        HttpStatus.CREATED
                );
            }
            return new ResponseEntity<>(
                    Map.of("error", response.getSecond()),
                    HttpStatus.BAD_REQUEST
            );
        } catch (Exception e) {
            return handleException(e);
        }

    }

    private ResponseEntity<?> handleException(Exception e) {
        System.out.println(e.toString());
    return new ResponseEntity<>(
        Map.of("error", "An unknown error has occurred"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
