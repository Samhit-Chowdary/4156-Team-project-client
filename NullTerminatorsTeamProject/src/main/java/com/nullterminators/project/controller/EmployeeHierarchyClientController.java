package com.nullterminators.project.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Client wrapper APIs for EmployeeHierarchyController.
 */
@RestController
@RequestMapping("/client/hierarchy")
public class EmployeeHierarchyClientController {

    private final RestTemplate restTemplate;
    private static final String BASE_URL = "http://localhost:8080/api/employee-hierarchy";
    @Value("${service.auth.username}")
    private String USERNAME;
    @Value("${service.auth.password}")
    private String PASSWORD;

    public EmployeeHierarchyClientController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Utility to create headers with hardcoded Basic Authentication.
     */
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String auth = USERNAME + ":" + PASSWORD;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        headers.set("Authorization", "Basic " + encodedAuth);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    @GetMapping("/subordinates/{employeeId}")
    public ResponseEntity<?> getSubordinates(@PathVariable Long employeeId) {
        try {
            HttpHeaders headers = createHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);
            return restTemplate.exchange(BASE_URL + "/subordinates/" + employeeId, HttpMethod.GET, entity, List.class);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to fetch subordinates: " + e.getMessage());
        }
    }

    @GetMapping("/supervisor/{employeeId}")
    public ResponseEntity<?> getSupervisor(@PathVariable Long employeeId) {
        try {
            HttpHeaders headers = createHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);
            return restTemplate.exchange(BASE_URL + "/supervisor/" + employeeId, HttpMethod.GET, entity, Long.class);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to fetch supervisor: " + e.getMessage());
        }
    }

    @GetMapping("/tree/{employeeId}")
    public ResponseEntity<?> getSubtree(@PathVariable Long employeeId) {
        try {
            HttpHeaders headers = createHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);
            return restTemplate.exchange(BASE_URL + "/tree/" + employeeId, HttpMethod.GET, entity, Object.class);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to fetch subtree: " + e.getMessage());
        }
    }

    @PostMapping("/addEdge/{supervisorId}/{employeeId}")
    public ResponseEntity<?> addEdge(@PathVariable Long supervisorId, @PathVariable Long employeeId) {
        try {
            HttpHeaders headers = createHeaders();
            HttpEntity<String> entity = new HttpEntity<>(null, headers);
            return restTemplate.exchange(BASE_URL + "/addEdge/" + supervisorId + "/" + employeeId, HttpMethod.POST, entity, String.class);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to add edge: " + e.getMessage());
        }
    }

    @DeleteMapping("/removeEdge/{employeeId}")
    public ResponseEntity<?> removeEdge(@PathVariable Long employeeId) {
        try {
            HttpHeaders headers = createHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);
            return restTemplate.exchange(BASE_URL + "/removeEdge/" + employeeId, HttpMethod.DELETE, entity, String.class);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to remove edge: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{employeeId}")
    public ResponseEntity<?> deleteEmployeeAndReassign(@PathVariable Long employeeId) {
        try {
            HttpHeaders headers = createHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // Fetch supervisor
            ResponseEntity<Object> supervisorResponse = restTemplate.exchange(
                    BASE_URL + "/supervisor/" + employeeId, HttpMethod.GET, entity, Object.class);

            Long supervisorId = null;
            if (supervisorResponse.getBody() instanceof LinkedHashMap) {
                supervisorId = Long.valueOf(((LinkedHashMap) supervisorResponse.getBody()).get("id").toString());
            } else if (supervisorResponse.getBody() instanceof Long) {
                supervisorId = (Long) supervisorResponse.getBody();
            }

            // Fetch subordinates
            ResponseEntity<List> subordinatesResponse = restTemplate.exchange(
                    BASE_URL + "/subordinates/" + employeeId, HttpMethod.GET, entity, List.class);

            List<Long> subordinates = new ArrayList<>();
            if (subordinatesResponse.getBody() != null) {
                for (Object item : subordinatesResponse.getBody()) {
                    if (item instanceof LinkedHashMap) {
                        subordinates.add(Long.valueOf(((LinkedHashMap) item).get("id").toString()));
                    } else if (item instanceof Long) {
                        subordinates.add((Long) item);
                    }
                }
            }

            // Handle edge cases
            if (supervisorResponse.getStatusCode().is4xxClientError() && subordinates != null && !subordinates.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Cannot delete employee with ID " + employeeId + " as they have subordinates but no supervisor.");
            }

            // Reassign subordinates to the supervisor
            if (supervisorId != null) {
                for (Long subordinateId : subordinates) {
                    HttpEntity<String> reassignEntity = new HttpEntity<>(headers);
                    restTemplate.exchange(
                            BASE_URL + "/addEdge/" + supervisorId + "/" + subordinateId, HttpMethod.POST, reassignEntity, String.class);
                }
            }

            // Remove employee
            restTemplate.exchange(BASE_URL + "/removeEdge/" + employeeId, HttpMethod.DELETE, entity, String.class);
            return ResponseEntity.ok("Employee deleted and subordinates reassigned successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to delete employee: " + e.getMessage());
        }
    }
}
