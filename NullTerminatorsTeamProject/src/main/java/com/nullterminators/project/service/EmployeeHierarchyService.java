package com.nullterminators.project.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

/** Service class for making REST API calls to the Employee Hierarchy service. */
@Service
public class EmployeeHierarchyService {

  private final RestTemplate restTemplate;
  private static final String BASE_URL = "http://localhost:8080/api/employee-hierarchy";

  @Value("${service.auth.username}")
  private String username;

  @Value("${service.auth.password}")
  private String password;

  public EmployeeHierarchyService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  /** Creates HTTP headers with Basic Authentication. */
  public HttpHeaders createHeaders() {
    HttpHeaders headers = new HttpHeaders();
    String auth = username + ":" + password;
    String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
    headers.set("Authorization", "Basic " + encodedAuth);
    headers.setContentType(MediaType.APPLICATION_JSON);
    return headers;
  }

  /**
   * Retrieves a list of subordinates for a given employee.
   *
   * @param employeeId the ID of the employee whose subordinates are to be retrieved
   * @return a list of IDs representing the subordinates of the specified employee
   */
  public List<Long> getSubordinates(Long employeeId) {
    HttpHeaders headers = createHeaders();
    HttpEntity<String> entity = new HttpEntity<>(headers);
    ResponseEntity<List> response =
        restTemplate.exchange(
            BASE_URL + "/subordinates/" + employeeId, HttpMethod.GET, entity, List.class);
    return response.getBody();
  }

  /**
   * Retrieves the supervisor of the given employee.
   *
   * @param employeeId the ID of the employee whose supervisor is to be retrieved
   * @return the ID of the supervisor of the given employee, or null if the employee has no
   *     supervisor
   */
  public Long getSupervisor(Long employeeId) {
    HttpHeaders headers = createHeaders();
    HttpEntity<String> entity = new HttpEntity<>(headers);
    ResponseEntity<Object> response =
        restTemplate.exchange(
            BASE_URL + "/supervisor/" + employeeId, HttpMethod.GET, entity, Object.class);

    if (response.getBody() instanceof Integer) {
      return ((Integer) response.getBody()).longValue();
    } else if (response.getBody() instanceof Long) {
      return (Long) response.getBody();
    }
    return null;
  }

  /**
   * Retrieves the subtree of an employee with the given ID.
   *
   * @param employeeId the ID of the employee whose subtree is to be retrieved
   * @return the subtree of the given employee
   */
  public Object getSubtree(Long employeeId) {
    HttpHeaders headers = createHeaders();
    HttpEntity<String> entity = new HttpEntity<>(headers);
    ResponseEntity<Object> response =
        restTemplate.exchange(
            BASE_URL + "/tree/" + employeeId, HttpMethod.GET, entity, Object.class);
    return response.getBody();
  }

  /**
   * Adds a supervisor-employee edge.
   *
   * @param supervisorId the ID of the supervisor
   * @param employeeId the ID of the employee
   */
  public void addEdge(Long supervisorId, Long employeeId) {
    HttpHeaders headers = createHeaders();
    HttpEntity<String> entity = new HttpEntity<>(null, headers);
    restTemplate.exchange(
        BASE_URL + "/addEdge/" + supervisorId + "/" + employeeId,
        HttpMethod.POST,
        entity,
        String.class);
  }

  /**
   * Removes the supervisor-employee edge for the specified employee.
   *
   * @param employeeId the ID of the employee whose supervisor edge is to be removed
   */
  public void removeEdge(Long employeeId) {
    HttpHeaders headers = createHeaders();
    HttpEntity<String> entity = new HttpEntity<>(headers);
    restTemplate.exchange(
        BASE_URL + "/removeEdge/" + employeeId, HttpMethod.DELETE, entity, String.class);
  }

  /**
   * Reassigns the subordinates of an employee to a new supervisor.
   *
   * <p>This method iterates over the given list of subordinates, removes the edge between each of
   * them and their current supervisor, and adds an edge between each of them and the new
   * supervisor.
   *
   * @param supervisorId the ID of the new supervisor to whom the subordinates are to be reassigned
   * @param subordinates the list of IDs of the subordinates to be reassigned
   */
  public void reassignSubordinates(Long supervisorId, List<Long> subordinates) {
    for (Long subordinateId : subordinates) {
      removeEdge(subordinateId);
      addEdge(supervisorId, subordinateId);
    }
  }

  /**
   * Gets the managers of an employee up to a certain height.
   *
   * @param employeeId the ID of the employee to find the managers of
   * @param height the maximum number of levels to search up the hierarchy
   * @return a list of IDs of the managers of the given employee, up to the given height
   */
  public List<Long> getManagersUpToHeight(Long employeeId, int height) {
    List<Long> managers = new ArrayList<>();
    Long currentEmployeeId = employeeId;

    for (int i = 0; i < height; i++) {
      Long supervisorId = getSupervisor(currentEmployeeId);
      if (supervisorId == null) {
        break;
      }
      managers.add(supervisorId);
      currentEmployeeId = supervisorId;
    }

    return managers;
  }



  /**
   * Handles exceptions by returning an appropriate ResponseEntity.
   *
   * If the exception is an instance of HttpClientErrorException, the response entity will
   * contain the response body and status code from the exception. For other exceptions, a
   * generic "Internal Server Error" response with HTTP status 500 is returned.
   *
   * @param e the exception to handle
   * @return a ResponseEntity containing the error message and appropriate HTTP status code
   */
  public ResponseEntity<Object> handleException(Exception e) {
    if (e instanceof HttpClientErrorException clientErrorException) {
      return new ResponseEntity<>(
              clientErrorException.getResponseBodyAsString(),
              clientErrorException.getStatusCode()
      );
    }

    return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
