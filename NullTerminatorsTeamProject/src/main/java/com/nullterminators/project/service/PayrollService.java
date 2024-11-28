package com.nullterminators.project.service;

import java.util.Base64;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Service class for making REST API calls to the Payroll service.
 */
@Service
public class PayrollService {

  private final RestTemplate restTemplate;

  @Value("${service.auth.username}")
  private String username;

  @Value("${service.auth.password}")
  private String password;

  @Value("${service.url}")
  private String BASE_URL;

  @Autowired
  public PayrollService(RestTemplate restTemplate) {
    this.restTemplate  = restTemplate;
  }

  /**
   * Creates HTTP headers with Basic Authentication.
   */
  public HttpHeaders createHeaders() {
    HttpHeaders headers = new HttpHeaders();
    String auth = username + ":" + password;
    String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
    headers.set("Authorization", "Basic " + encodedAuth);
    headers.setContentType(MediaType.APPLICATION_JSON);
    return headers;
  }

  /**
   * Send request to get Payroll by Employee ID.
   *
   * @param employeeId (Integer) : Employee ID
   * @return Pair : Response from the service
   */
  public Pair<HttpStatus, Object> getPayrollByEmployeeId(Integer employeeId) {
    HttpHeaders headers = createHeaders();
    HttpEntity<String> entity = new HttpEntity<>(headers);
    try {
      ResponseEntity<Object> response = restTemplate.exchange(BASE_URL + "/payroll/" + employeeId,
              HttpMethod.GET, entity, Object.class);
      return Pair.of((HttpStatus) response.getStatusCode(), response.getBody());
    } catch (HttpClientErrorException e) {
      return Pair.of((HttpStatus) e.getStatusCode(), e.getResponseBodyAs(Object.class));
    }
  }

  /**
   * Send request to mark a Payroll as paid.
   *
   * @param employeeId (Integer) : Employee ID
   * @param updates (Map) : Map of details
   * @return Pair : Response from the service
   */
  public Pair<HttpStatus, Object> markAsPaid(Integer employeeId, Map<String, Object> updates) {
    HttpHeaders headers = createHeaders();
    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(updates, headers);
    try {
      ResponseEntity<Object> response = restTemplate.exchange(BASE_URL + "/payroll/" + employeeId
              + "/markAsPaid", HttpMethod.PATCH, entity, Object.class);
      return Pair.of((HttpStatus) response.getStatusCode(), response.getBody());
    } catch (HttpClientErrorException e) {
      return Pair.of((HttpStatus) e.getStatusCode(), e.getResponseBodyAs(Object.class));
    }
  }

  /**
   * Send request to mark a Payroll as unpaid.
   *
   * @param employeeId (Integer) : Employee ID
   * @param updates : Map of details
   * @return Pair : Response from the service
   */
  public Pair<HttpStatus, Object> markAsUnpaid(Integer employeeId,
                                               Map<String, Object> updates) {
    HttpHeaders headers = createHeaders();
    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(updates, headers);
    try {
      ResponseEntity<Object> response = restTemplate.exchange(BASE_URL + "/payroll/" + employeeId
              + "/markAsUnpaid", HttpMethod.PATCH, entity, Object.class);
      return Pair.of((HttpStatus) response.getStatusCode(), response.getBody());
    } catch (HttpClientErrorException e) {
      return Pair.of((HttpStatus) e.getStatusCode(), e.getResponseBodyAs(Object.class));
    }
  }

  /**
   * Send request to add a new Payroll.
   *
   * @param employeeId (Integer) : Employee ID
   * @param updates : Map of details
   * @return Pair : Response from the service
   */
  public Pair<HttpStatus, Object> createPayrollByEmployeeId(Integer employeeId,
                                                            Map<String, Object> updates) {
    HttpHeaders headers = createHeaders();
    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(updates, headers);
    try {
      ResponseEntity<Object> response = restTemplate.exchange(BASE_URL + "/payroll/" + employeeId
                      + "/addPayroll", HttpMethod.POST, entity, Object.class);
      return Pair.of((HttpStatus) response.getStatusCode(), response.getBody());
    } catch (HttpClientErrorException e) {
      return Pair.of((HttpStatus) e.getStatusCode(), e.getResponseBodyAs(Object.class));
    }
  }

  /**
   * Send request to delete a Payroll.
   *
   * @param employeeId (Integer) : Employee ID.
   * @param updates : Map of details
   * @return Pair : Response from the service
   */
  public Pair<HttpStatus, Object>  deletePayrollByEmployeeId(Integer employeeId,
                                                             Map<String, Object> updates) {
    HttpHeaders headers = createHeaders();
    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(updates, headers);
    try {
      ResponseEntity<Object> response = restTemplate.exchange(BASE_URL + "/payroll/" + employeeId
                      + "/deletePayroll", HttpMethod.DELETE, entity, Object.class);
      return Pair.of((HttpStatus) response.getStatusCode(), response.getBody());
    } catch (HttpClientErrorException e) {
      return Pair.of((HttpStatus) e.getStatusCode(), e.getResponseBodyAs(Object.class));
    }
  }

  /**
   * Send request to adjust the salary of a Payroll.
   *
   * @param employeeId (Integer) : Employee ID
   * @param updates : Map of details
   * @return Pair : Response from the service
   */
  public Pair<HttpStatus, Object> adjustSalary(Integer employeeId, Map<String, Object> updates) {
    HttpHeaders headers = createHeaders();
    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(updates, headers);
    try {
      ResponseEntity<Object> response = restTemplate.exchange(BASE_URL + "/payroll/" + employeeId
                      + "/adjustSalary", HttpMethod.PATCH, entity, Object.class);
      return Pair.of((HttpStatus) response.getStatusCode(), response.getBody());
    } catch (HttpClientErrorException e) {
      return Pair.of((HttpStatus) e.getStatusCode(), e.getResponseBodyAs(Object.class));
    }
  }

  /**
   * Send request to adjust the day of a Payroll.
   *
   * @param employeeId (Integer) : Employee ID
   * @param updates : Map of details
   * @return Pair : Response from the service
   */
  public Pair<HttpStatus, Object> adjustDay(Integer employeeId, Map<String, Object> updates) {
    HttpHeaders headers = createHeaders();
    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(updates, headers);
    try {
      ResponseEntity<Object> response = restTemplate.exchange(BASE_URL + "/payroll/" + employeeId
              + "/adjustDay", HttpMethod.PATCH, entity, Object.class);
      return Pair.of((HttpStatus) response.getStatusCode(), response.getBody());
    } catch (HttpClientErrorException e) {
      return Pair.of((HttpStatus) e.getStatusCode(), e.getResponseBodyAs(Object.class));
    }
  }

  /**
   * Send request to generate all Payrolls.
   *
   * @param updates : Map of details
   * @return Pair : Response from the service
   */
  public Pair<HttpStatus, Object> generatePayroll(Map<String, Object> updates) {
    HttpHeaders headers = createHeaders();
    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(updates, headers);
    try {
      ResponseEntity<Object> response = restTemplate.exchange(BASE_URL + "/payroll/generatePayroll",
              HttpMethod.POST, entity, Object.class);
      return Pair.of((HttpStatus) response.getStatusCode(), response.getBody());
    } catch (HttpClientErrorException e) {
      return Pair.of((HttpStatus) e.getStatusCode(), e.getResponseBodyAs(Object.class));
    }
  }

  /**
   * Send request to delete all Payrolls.
   *
   * @param updates : Map of details
   * @return Pair : Response from the service
   */
  public Pair<HttpStatus, Object> deletePayroll(Map<String, Object> updates) {
    HttpHeaders headers = createHeaders();
    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(updates, headers);
    try {
      ResponseEntity<Object> response = restTemplate.exchange(BASE_URL + "/payroll/deletePayroll",
              HttpMethod.DELETE, entity, Object.class);
      return Pair.of((HttpStatus) response.getStatusCode(), response.getBody());
    } catch (HttpClientErrorException e) {
      return Pair.of((HttpStatus) e.getStatusCode(), e.getResponseBodyAs(Object.class));
    }
  }
}
