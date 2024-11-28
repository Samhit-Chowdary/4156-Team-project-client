package com.nullterminators.project.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.nullterminators.project.model.EmployeeProfileManagement;
import com.nullterminators.project.repository.EmployeeProfileManagementRepository;

/** Service class for making REST API calls to the Employee Profile Management service. */
@Service
public class EmployeeProfileManagementService {

  @Autowired
  private EmployeeProfileManagementRepository employeeProfileManagementRepository;

  private final RestTemplate restTemplate;
  private static final String BASE_URL = "http://localhost:8080/employeeProfile/";

  @Value("${service.auth.username}")
  private String username;

  @Value("${service.auth.password}")
  private String password;

  public EmployeeProfileManagementService(RestTemplate restTemplate) {
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
   * Creates employee and saves it in service DB.
   * @param name Name of new employee
   * @param phoneNumber Phone Number of new employee
   * @param gender Gender of new employee
   * @param age Age of new employee 
   * @param startDate Starting date of new employee
   * @param designation Designation of new employee
   * @param email Email of new employee
   * @param emergencyContact Emergency Contact of new employee
   * @param baseSalary Base Salary of new employee
   * @return Id of of new employee created if successful
   */
  public int createEmployee(String name, String phoneNumber, String gender
      , int age, LocalDate startDate, String designation, String email, String emergencyContact
      , int baseSalary) {

    HttpHeaders headers = createHeaders();

    var requestBody = new LinkedHashMap<String, Object>();
    requestBody.put("name", name);
    requestBody.put("phoneNumber", phoneNumber);
    requestBody.put("gender", gender);
    requestBody.put("age", age);
    requestBody.put("startDate", startDate);
    requestBody.put("designation", designation);
    requestBody.put("email", email);
    requestBody.put("emergencyContact", emergencyContact);
    requestBody.put("baseSalary", baseSalary);

    HttpEntity<Map<String, Object>> reqEntity = new HttpEntity<>(requestBody, headers);

    try {
      String url = BASE_URL + "createNewEmployee?" + "name=" + name + "&phoneNumber="
          + phoneNumber + "&gender=" + gender + "&age=" + age + "&startDate=" + startDate
          + "&designation=" + designation + "&email=" + email + "&emergencyContact="
          + emergencyContact + "&baseSalary=" + baseSalary;
      ResponseEntity<Integer> response =
        restTemplate.exchange(url, HttpMethod.POST
        , reqEntity, Integer.class);
      return response.getBody();
    } catch(Exception e) {
      System.out.println("Error: " + e);
      return -1;
    }
  }

  /**
   * Adds employee to the client database.
   * @param id employee id
   * @param designation designation of employee
   */
  public void addEmployeeToClientDatabase(int id, String designation) {
    EmployeeProfileManagement employee = new EmployeeProfileManagement();
    employee.setId(id);
    employee.setDesignation(designation);
    employeeProfileManagementRepository.save(employee);
  }

  /**
   * Gets all employees.
   * @return
   */
  public List<Integer> getAllEmployees() {
    HttpHeaders headers = createHeaders();

    HttpEntity<String> entity = new HttpEntity<>(headers);
    ResponseEntity<String> response =
        restTemplate.exchange(BASE_URL + "/getAllEmployees", HttpMethod.GET
        , entity, String.class);
    if (response.getStatusCode() == HttpStatus.OK) {
      String employees = response.getBody();
      List<Integer> allEmployeeIds = new ArrayList<>();

      String[] splitEmployees = employees.substring(1
          , employees.length() - 1).split("},");
      
      for (String emp : splitEmployees) {
        int strt = emp.indexOf("\"id\":") + 5;
        int end = emp.indexOf(",", strt);

        if (end == -1) {
          end = emp.indexOf("}", strt);
        }

        String stringId = emp.substring(strt, end).trim();
        if (!stringId.isEmpty()) {
          allEmployeeIds.add(Integer.parseInt(stringId));
        }
      }

      return allEmployeeIds;

    } else {
      throw new RuntimeErrorException(null, "Failed to fetch employees. Status code: "
          + response.getStatusCode());
    }
  }

  /**
   * Get employee as per id given.
   * @param id
   * @return
   */
  public String getEmployee(int id) {
    HttpHeaders headers = createHeaders();

    HttpEntity<String> entity = new HttpEntity<>(headers);
    // System.out.println(+"{id}");
    ResponseEntity<String> response =
        restTemplate.exchange(BASE_URL + id, HttpMethod.GET
        , entity, String.class);
    if (response.getStatusCode() == HttpStatus.OK) {
      return response.getBody();
    } else {
      throw new RuntimeErrorException(null, "Failed to fetch employee. Status code: "
          + response.getStatusCode());
    }
  }

  /**
   * Deleting employee.
   * @param id
   * @return true if successful
   */
  public boolean deleteEmployee(int id) {
    HttpHeaders headers = createHeaders();

    HttpEntity<String> entity = new HttpEntity<>(headers);
    // System.out.println(+"{id}");
    ResponseEntity<String> response =
        restTemplate.exchange(BASE_URL + id, HttpMethod.DELETE
        , entity, String.class);
    if (response.getStatusCode() == HttpStatus.OK) {
      return true;
    } else {
      throw new RuntimeErrorException(null, "Failed to fetch employee. Status code: "
          + response.getStatusCode());
    }
  }
    
}
