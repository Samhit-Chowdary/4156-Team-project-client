package com.nullterminators.project.service;


import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Service for managing time-off requests, validations, and authorization checks.
 */
@Service
public class TimeOffClientService {

    private static final String BASE_URL = "http://localhost:8080/timeoff";
    private final RestTemplate restTemplate;
    private final EmployeeHierarchyService hierarchyService;
    @Value("${service.auth.username}")
    private String username;
    @Value("${service.auth.password}")
    private String password;

    public TimeOffClientService(RestTemplate restTemplate, EmployeeHierarchyService hierarchyService) {
        this.restTemplate = restTemplate;
        this.hierarchyService = hierarchyService;
    }

    /**
     * Retrieves time-off records for an employee.
     *
     * @param requestorEmployeeId the ID of the employee making the request
     * @param requestedEmployeeId the ID of the employee whose records are requested
     * @return a list of time-off records
     */
    @Cacheable(value = "timeOffs", key = "#requestedEmployeeId")
    public List<Object> getTimeOffByEmployeeId(Integer requestorEmployeeId, Integer requestedEmployeeId) {
        Long superVisorEmployeeId = null;
        try {
            superVisorEmployeeId = hierarchyService.getSupervisor(requestorEmployeeId.longValue());
        } catch (Exception ignored) {

        }

        if (superVisorEmployeeId != null && !superVisorEmployeeId.equals(requestedEmployeeId.longValue()) && !requestedEmployeeId.equals(requestorEmployeeId)) {
            throw new IllegalArgumentException("Only the employee or their supervisor can view this information.");
        }

        HttpHeaders headers = createHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Object[]> response = restTemplate.exchange(
                BASE_URL + "/" + requestedEmployeeId,
                HttpMethod.GET,
                entity,
                Object[].class
        );

        return response.getBody() != null ? Arrays.asList(response.getBody()) : List.of();
    }

    /**
     * Retrieves time-off records for an employee within a date range.
     *
     * @param requestorEmployeeId the ID of the employee making the request
     * @param requestedEmployeeId the ID of the employee whose records are requested
     * @param startDate the start date of the range
     * @param endDate the end date of the range
     * @return a list of time-off records within the specified range
     */
    @Cacheable(value = "timeOffsInRange", key = "#requestedEmployeeId + '-' + #startDate + '-' + #endDate")
    public List<Object> getTimeOffInRange(Integer requestorEmployeeId, Integer requestedEmployeeId, String startDate, String endDate) {
        if (isValidDateFormat(startDate) || isValidDateFormat(endDate)) {
            throw new IllegalArgumentException("Invalid date format. Use 'yyyy-MM-dd'.");
        }

        Long superVisorEmployeeId = null;
        try {
            superVisorEmployeeId = hierarchyService.getSupervisor(requestorEmployeeId.longValue());
        } catch (Exception ignored) {

        }

        if (superVisorEmployeeId != null && !superVisorEmployeeId.equals(requestedEmployeeId.longValue()) && !requestedEmployeeId.equals(requestorEmployeeId)) {
            throw new IllegalArgumentException("Only the employee or their supervisor can view this information.");
        }

        HttpHeaders headers = createHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Object[]> response = restTemplate.exchange(
                BASE_URL + "/" + requestedEmployeeId + "/range?startDate=" + startDate + "&endDate=" + endDate,
                HttpMethod.GET,
                entity,
                Object[].class
        );

        return response.getBody() != null ? Arrays.asList(response.getBody()) : List.of();
    }


    /**
     * Creates a new time-off request after validation.
     *
     * @param timeOffRequest a map containing the time-off details
     * @return the created time-off object
     */
    public Object createTimeOffRequest(Map<String, Object> timeOffRequest) {
        try {
            Long employeeId = hierarchyService.getSupervisor(((Number) timeOffRequest.get("employeeId")).longValue());
            timeOffRequest.put("approverId", employeeId);
        } catch (Exception e) {
            timeOffRequest.put("approverId", null);
        }

        List<String> validLeaveTypes = Arrays.asList("CASUAL", "SICK", "HOLIDAY", "MATERNITY", "PATERNITY");
        if (!validLeaveTypes.contains(timeOffRequest.get("leaveType").toString())) {
            throw new IllegalArgumentException("Invalid leave type.");
        }

        LocalDate startDate = LocalDate.parse((String) timeOffRequest.get("startDate"));
        LocalDate endDate = LocalDate.parse((String) timeOffRequest.get("endDate"));
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before or the same as end date.");
        }

        if (startDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Start date cannot be in the past.");
        }

        long leaveDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;

        if ("CASUAL".equals(timeOffRequest.get("leaveType").toString()) && leaveDays > 5) {
            throw new IllegalArgumentException("Casual leave cannot exceed 5 days.");
        }

        HttpHeaders headers = createHeaders();

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(timeOffRequest, headers);

        ResponseEntity<Object> response =
                restTemplate.exchange(
                        BASE_URL + "/create",
                        HttpMethod.POST,
                        entity,
                        Object.class
                );

        return response.getBody();
    }


    /**
     * Updates the status of a time-off request.
     *
     * @param requestorEmployeeId the ID of the employee making the request
     * @param requestedEmployeeId the ID of the employee whose request is updated
     * @param timeOffId the ID of the time-off request
     * @param action the action to perform (approve/reject/cancel)
     * @return true if the status update is successful, false otherwise
     */
    public boolean updateTimeOffStatus(Integer requestorEmployeeId, Integer requestedEmployeeId, Integer timeOffId, String action) {
        Long superVisorEmployeeId = null;
        try {
            superVisorEmployeeId = hierarchyService.getSupervisor(requestorEmployeeId.longValue());
        } catch (Exception ignored) {

        }

        if (!action.equals("cancel") && superVisorEmployeeId != null && !superVisorEmployeeId.equals(requestedEmployeeId.longValue())) {
            throw new IllegalArgumentException("Only the employee's supervisor can approve/reject the timeoff request.");
        }

        HttpHeaders headers = createHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                BASE_URL + "/" + requestedEmployeeId + "/" + timeOffId + "/update-status?action=" + action,
                HttpMethod.PUT,
                entity,
                Void.class
        );

        return response.getStatusCode() == HttpStatus.OK;
    }

    /**
     * Creates HTTP headers for authentication and content type.
     *
     * @return HttpHeaders object with authentication and content type
     */
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String auth = username + ":" + password;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        headers.set("Authorization", "Basic " + encodedAuth);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    /**
     * Validates the format of a date string.
     *
     * @param date the date string to validate
     * @return true if the format is invalid, false otherwise
     */
    private boolean isValidDateFormat(String date) {
        try {
            LocalDate.parse(date);
            return false;
        } catch (DateTimeParseException e) {
            return true;
        }
    }
}
