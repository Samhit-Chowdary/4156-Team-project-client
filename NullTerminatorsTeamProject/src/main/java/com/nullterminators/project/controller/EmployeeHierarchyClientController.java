package com.nullterminators.project.controller;

import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/** Client wrapper APIs for EmployeeHierarchyController. */
@RestController
@RequestMapping("/client/hierarchy")
public class EmployeeHierarchyClientController {

  private final RestTemplate restTemplate;
  private static final String BASE_URL = "http://localhost:8080/api/employee-hierarchy";

  @Value("${service.auth.username}")
  private String username;

  @Value("${service.auth.password}")
  private String password;

  public EmployeeHierarchyClientController(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  /**
   * Creates and returns HTTP headers with basic authentication. The 'Authorization' header is set
   * using a Base64 encoded username and password. The 'Content-Type' is set to 'application/json'.
   *
   * @return HttpHeaders object containing the configured headers.
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
   * Finds all subordinates of the employee with the given id.
   *
   * @param employeeId the id of the employee whose subordinates are to be found
   * @return a list of subordinates of the given employee
   */
  @GetMapping("/subordinates/{employeeId}")
  public ResponseEntity<?> getSubordinates(@PathVariable Long employeeId) {
    try {
      HttpHeaders headers = createHeaders();
      HttpEntity<String> entity = new HttpEntity<>(headers);
      return restTemplate.exchange(
          BASE_URL + "/subordinates/" + employeeId, HttpMethod.GET, entity, List.class);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body("Failed to fetch subordinates: " + e.getMessage());
    }
  }

  /**
   * Finds the supervisor of the employee with the given id.
   *
   * @param employeeId the id of the employee whose supervisor is to be found
   * @return the id of the supervisor of the given employee
   */
  @GetMapping("/supervisor/{employeeId}")
  public ResponseEntity<?> getSupervisor(@PathVariable Long employeeId) {
    try {
      HttpHeaders headers = createHeaders();
      HttpEntity<String> entity = new HttpEntity<>(headers);
      return restTemplate.exchange(
          BASE_URL + "/supervisor/" + employeeId, HttpMethod.GET, entity, Long.class);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body("Failed to fetch supervisor: " + e.getMessage());
    }
  }

  /**
   * Gets the subtree of an employee with the given id.
   *
   * @param employeeId the id of the employee
   * @return the subtree of the given employee
   */
  @GetMapping("/tree/{employeeId}")
  public ResponseEntity<?> getSubtree(@PathVariable Long employeeId) {
    try {
      HttpHeaders headers = createHeaders();
      HttpEntity<String> entity = new HttpEntity<>(headers);
      return restTemplate.exchange(
          BASE_URL + "/tree/" + employeeId, HttpMethod.GET, entity, Object.class);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body("Failed to fetch subtree: " + e.getMessage());
    }
  }

  /**
   * Adds a supervisor-employee edge.
   *
   * @param supervisorId the ID of the supervisor
   * @param employeeId the ID of the employee
   * @return a response entity with status
   */
  @PostMapping("/addEdge/{supervisorId}/{employeeId}")
  public ResponseEntity<?> addEdge(@PathVariable Long supervisorId, @PathVariable Long employeeId) {
    try {
      HttpHeaders headers = createHeaders();
      HttpEntity<String> entity = new HttpEntity<>(null, headers);
      return restTemplate.exchange(
          BASE_URL + "/addEdge/" + supervisorId + "/" + employeeId,
          HttpMethod.POST,
          entity,
          String.class);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body("Failed to add edge: " + e.getMessage());
    }
  }

  /**
   * Removes a supervisor-employee edge.
   *
   * @param employeeId the ID of the employee whose supervisor edge is to be removed
   * @return a response entity with status
   */
  @DeleteMapping("/removeEdge/{employeeId}")
  public ResponseEntity<?> removeEdge(@PathVariable Long employeeId) {
    try {
      HttpHeaders headers = createHeaders();
      HttpEntity<String> entity = new HttpEntity<>(headers);
      return restTemplate.exchange(
          BASE_URL + "/removeEdge/" + employeeId, HttpMethod.DELETE, entity, String.class);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body("Failed to remove edge: " + e.getMessage());
    }
  }

  /**
   * Deletes an employee with the given ID and reassigns their subordinates to the employee's
   * supervisor.
   *
   * <p>This method performs the following operations: 1. Fetches the supervisor of the employee. 2.
   * Fetches the subordinates of the employee. 3. If the employee has subordinates but no
   * supervisor, returns a BAD_REQUEST response. 4. If the employee has a supervisor, reassigns all
   * subordinates to the supervisor. 5. Removes the supervisor-employee edge. 6. Returns an OK
   * response upon successful deletion and reassignment.
   *
   * @param employeeId the ID of the employee to be deleted
   * @return a response entity indicating the status of the operation
   */
  @DeleteMapping("/delete/{employeeId}")
  public ResponseEntity<?> deleteEmployeeAndReassign(@PathVariable Long employeeId) {

    try {
      HttpHeaders headers = createHeaders();
      HttpEntity<String> entity = new HttpEntity<>(headers);

      ResponseEntity<Object> supervisorResponse =
          restTemplate.exchange(
              BASE_URL + "/supervisor/" + employeeId, HttpMethod.GET, entity, Object.class);

      ResponseEntity<List> subordinatesResponse =
          restTemplate.exchange(
              BASE_URL + "/subordinates/" + employeeId, HttpMethod.GET, entity, List.class);

      if (supervisorResponse.getStatusCode().is4xxClientError()
          && subordinatesResponse.getBody() != null
          && !subordinatesResponse.getBody().isEmpty()) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(
                "Cannot delete employee with ID "
                    + employeeId
                    + " as they have subordinates but no supervisor.");
      }

      Long supervisorId = ((Integer) supervisorResponse.getBody()).longValue();

      List<Long> subordinates = new ArrayList<>();
      if (subordinatesResponse.getBody() != null) {
        for (Object item : subordinatesResponse.getBody()) {
          if (item instanceof LinkedHashMap) {
            subordinates.add(Long.valueOf(((LinkedHashMap) item).get("toEmployeeId").toString()));
          } else if (item instanceof Long) {
            subordinates.add((Long) item);
          }
        }
      }

      for (Long subordinateId : subordinates) {
        restTemplate.exchange(
            BASE_URL + "/removeEdge/" + subordinateId, HttpMethod.DELETE, entity, String.class);
        restTemplate.exchange(
            BASE_URL + "/addEdge/" + supervisorId + "/" + subordinateId,
            HttpMethod.POST,
            entity,
            String.class);
      }

      restTemplate.exchange(
          BASE_URL + "/removeEdge/" + employeeId, HttpMethod.DELETE, entity, String.class);
      return ResponseEntity.ok("Employee deleted and subordinates reassigned successfully.");
    } catch (Exception e) {
      return ResponseEntity.badRequest().body("Failed to delete employee: " + e.getMessage());
    }
  }

  /**
   * Gets the managers of an employee up to a certain height.
   *
   * @param employeeId the ID of the employee to find the managers of
   * @param height the maximum number of levels to search up the hierarchy
   * @return a list of IDs of the managers of the given employee, up to the given height
   */
  @GetMapping("/managers/{employeeId}/{height}")
  public ResponseEntity<?> getManagersUpToHeight(
      @PathVariable Long employeeId, @PathVariable int height) {
    try {
      HttpHeaders headers = createHeaders();
      HttpEntity<String> entity = new HttpEntity<>(headers);

      List<Long> managers = new ArrayList<>();
      Long currentEmployeeId = employeeId;

      for (int i = 0; i < height; i++) {
        ResponseEntity<Object> supervisorResponse =
            restTemplate.exchange(
                BASE_URL + "/supervisor/" + currentEmployeeId,
                HttpMethod.GET,
                entity,
                Object.class);

        if (supervisorResponse.getStatusCode().is2xxSuccessful()
            && supervisorResponse.getBody() != null) {
          Long supervisorId;
          if (supervisorResponse.getBody() instanceof Integer) {
            supervisorId = ((Integer) supervisorResponse.getBody()).longValue();
          } else if (supervisorResponse.getBody() instanceof Long) {
            supervisorId = (Long) supervisorResponse.getBody();
          } else {
            break;
          }

          managers.add(supervisorId);
          currentEmployeeId = supervisorId;
        } else {
          break;
        }
      }

      return ResponseEntity.ok(managers);
    } catch (Exception e) {
      return ResponseEntity.badRequest()
          .body("Failed to fetch managers up to height " + height + ": " + e.getMessage());
    }
  }
}
