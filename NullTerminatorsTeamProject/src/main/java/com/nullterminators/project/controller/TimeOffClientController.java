package com.nullterminators.project.controller;

import com.nullterminators.project.service.TimeOffClientService;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing time-off requests for employees.
 */
@RestController
@RequestMapping("/client/timeoff")
public class TimeOffClientController {

    private final TimeOffClientService timeOffClientService;

    public TimeOffClientController(TimeOffClientService timeOffClientService) {
        this.timeOffClientService = timeOffClientService;
    }

    /**
     * GET /client/timeoff/{requestorEmployeeId}/{requestedEmployeeId} - Retrieves time-off records
     * for a specific employee based on their employee ID.
     *
     * @param requestorEmployeeId the ID of the employee making the request
     * @param requestedEmployeeId the ID of the employee whose time-off records are requested
     * @return ResponseEntity containing the list of time-offs or an error message
     */
    @GetMapping("/{requestorEmployeeId}/{requestedEmployeeId}")
    public ResponseEntity<?> getTimeOffByEmployeeId(@PathVariable Integer requestorEmployeeId,
                                                    @PathVariable Integer requestedEmployeeId) {
        try {
            List<Object> timeOffs = timeOffClientService.getTimeOffByEmployeeId(requestorEmployeeId, requestedEmployeeId);
            return ResponseEntity.ok(timeOffs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching time-offs: " + e.getMessage());
        }
    }

    /**
     * GET /client/timeoff/{requestorEmployeeId}/{requestedEmployeeId}/range - Retrieves time-off
     * records for a specific employee within a date range.
     *
     * @param requestorEmployeeId the ID of the employee making the request
     * @param requestedEmployeeId the ID of the employee whose time-off records are requested
     * @param startDate the start date of the range (in yyyy-MM-dd format)
     * @param endDate the end date of the range (in yyyy-MM-dd format)
     * @return ResponseEntity containing the list of time-offs within the range or an error message
     */
    @GetMapping("/{requestorEmployeeId}/{requestedEmployeeId}/range")
    public ResponseEntity<?> getTimeOffInRange(
            @PathVariable Integer requestorEmployeeId,
            @PathVariable Integer requestedEmployeeId,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate
    ) {
        try {
            List<Object> timeOffs = timeOffClientService.getTimeOffInRange(requestorEmployeeId, requestedEmployeeId, startDate, endDate);
            return ResponseEntity.ok(timeOffs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching time-offs in range: " + e.getMessage());
        }
    }

    /**
     * POST /client/timeoff/create - Creates a new time-off request for an employee.
     *
     * @param timeOff a map containing the time-off details
     * @return ResponseEntity containing the created time-off details or an error message
     */
    @PostMapping("/create")
    public ResponseEntity<?> createTimeOffRequest(@RequestBody Map<String, Object> timeOff) {
        try {
            Object createdTimeOff = timeOffClientService.createTimeOffRequest(timeOff);
            return ResponseEntity.ok(createdTimeOff);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating time-off request: " + e.getMessage());
        }
    }

    /**
     * PUT /client/timeoff/{requestorEmployeeId}/{requestedEmployeeId}/{timeOffId}/update-status -
     * Updates the status of a specific time-off request.
     *
     * @param requestorEmployeeId the ID of the employee making the request
     * @param requestedEmployeeId the ID of the employee whose time-off request is being updated
     * @param timeOffId the ID of the time-off request
     * @param action the action to be performed (approve/reject/cancel)
     * @return ResponseEntity mentioning whether the status update was successful or not
     */
    @PutMapping("/{requestorEmployeeId}/{requestedEmployeeId}/{timeOffId}/update-status")
    public ResponseEntity<?> updateTimeOffStatus(
            @PathVariable Integer requestorEmployeeId,
            @PathVariable Integer requestedEmployeeId,
            @PathVariable Integer timeOffId,
            @RequestParam("action") String action
    ) {
        try {
            boolean updated = timeOffClientService.updateTimeOffStatus(requestorEmployeeId, requestedEmployeeId, timeOffId, action);
            return updated
                    ? ResponseEntity.ok("Status updated successfully")
                    : ResponseEntity.badRequest().body("Failed to update status");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating status: " + e.getMessage());
        }
    }
}
