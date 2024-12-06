package com.nullterminators.project.service;

import java.time.LocalDate;
import java.util.*;

import com.nullterminators.project.enums.PayrollRequestsStatus;
import com.nullterminators.project.model.PayrollRequests;
import com.nullterminators.project.repository.EmployeeProfileManagementRepository;
import com.nullterminators.project.repository.PayrollRequestsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Service class for making REST API calls to the Payroll service.
 */
@Service
public class PayrollService {

  private final RestTemplate restTemplate;
  private final PayrollRequestsRepository payrollRequestsRepository;
  private final EmployeeProfileManagementRepository employeeProfileManagementRepository;

  @Value("${service.auth.username}")
  private String username;

  @Value("${service.auth.password}")
  private String password;

  @Value("${service.url}")
  private String BASE_URL;

  @Autowired
  public PayrollService(PayrollRequestsRepository payrollRequestsRepository,
                        EmployeeProfileManagementRepository employeeProfileManagementRepository) {
    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
    this.restTemplate  = new RestTemplate(requestFactory);
    this.payrollRequestsRepository = payrollRequestsRepository;
    this.employeeProfileManagementRepository = employeeProfileManagementRepository;
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
    } catch (Exception e) {
        return Pair.of(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
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
              + "/markPaid", HttpMethod.PATCH, entity, Object.class);
      return Pair.of((HttpStatus) response.getStatusCode(), response.getBody());
    } catch (HttpClientErrorException e) {
      return Pair.of((HttpStatus) e.getStatusCode(), e.getResponseBodyAs(Object.class));
    } catch (Exception e) {
      return Pair.of(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
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
              + "/markUnpaid", HttpMethod.PATCH, entity, Object.class);
      return Pair.of((HttpStatus) response.getStatusCode(), response.getBody());
    } catch (HttpClientErrorException e) {
      return Pair.of((HttpStatus) e.getStatusCode(), e.getResponseBodyAs(Object.class));
    } catch (Exception e) {
      return Pair.of(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
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
    } catch (Exception e) {
      return Pair.of(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
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
    } catch (Exception e) {
      return Pair.of(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
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
    } catch (Exception e) {
      return Pair.of(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
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
    } catch (Exception e) {
      return Pair.of(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
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
    } catch (Exception e) {
      return Pair.of(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
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
    } catch (Exception e) {
      return Pair.of(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }

  public PayrollRequestsStatus createRequest(Integer employeeId, Map<String, Object> updates) {
    List<UpdateField> flags = new ArrayList<>(Arrays.asList(UpdateField.day,
            UpdateField.month, UpdateField.year));
    Pair<PayrollRequestsStatus, Map<String, Integer>> data = checkError(updates, flags);

    if (data.getFirst() != PayrollRequestsStatus.OK) {
      return data.getFirst();
    }

    if (employeeProfileManagementRepository.findById(employeeId).isEmpty()) {
      return PayrollRequestsStatus.EMPLOYEE_NOT_FOUND;
    }

    PayrollRequests payrollRequests = new PayrollRequests();
    payrollRequests.setEmpId(employeeId);
    payrollRequests.setCreatedDate(LocalDate.of(data.getSecond().get("year"),
            data.getSecond().get("month"), data.getSecond().get("day")));
    payrollRequests.setApproved(PayrollRequestsStatus.PENDING.ordinal());
    payrollRequests.setMReason("");

    try {
      payrollRequests.setReason(updates.get("reason").toString());
    } catch (Exception e) {
      payrollRequests.setReason("Not Provided");
    }

    try {
      payrollRequestsRepository.save(payrollRequests);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return PayrollRequestsStatus.SUCCESS;
  }

  public PayrollRequestsStatus updateRequest(Integer requestId, Map<String, Object> updates) {
    PayrollRequests request = payrollRequestsRepository.findById(requestId).orElse(null);

    if (request == null) {
      return PayrollRequestsStatus.REQUEST_NOT_FOUND;
    } else {
      try {
        request.setMReason(updates.get("comments").toString());;
      } catch (Exception e) {
        request.setMReason("No comments");
      }

      try {
        String status = updates.get("status").toString();
        if (status.equals("approved")) {
          request.setApproved(PayrollRequestsStatus.APPROVED.ordinal());
        } else if (status.equals("rejected")) {
          request.setApproved(PayrollRequestsStatus.REJECTED.ordinal());
        } else {
          return PayrollRequestsStatus.INVALID_FORMAT;
        }
      } catch (Exception e) {
        return PayrollRequestsStatus.INVALID_FORMAT;
      }

      payrollRequestsRepository.save(request);
      return PayrollRequestsStatus.SUCCESS;
    }
  }

  public Pair<PayrollRequestsStatus, List<Map<String, Object>>> getRequests(Integer employeeId) {
    List<Map<String, Object>> returnValue = new ArrayList<>();

    if (employeeProfileManagementRepository.findById(employeeId).isEmpty()) {
      return Pair.of(PayrollRequestsStatus.EMPLOYEE_NOT_FOUND, returnValue);
    }

    List<PayrollRequests> requests = payrollRequestsRepository.findAllByEmpIdOrderByCreatedDate(employeeId);

    for (PayrollRequests request : requests) {
      Map<String, Object> data = new HashMap<>();
      data.put("id", request.getId());
      data.put("date", request.getCreatedDate());
      data.put("approved", PayrollRequestsStatus.values()[request.getApproved()]);
      data.put("reason", request.getReason());
      data.put("comments", request.getMReason());
      returnValue.add(data);
    }

    return Pair.of(PayrollRequestsStatus.SUCCESS, returnValue);

  }

  public PayrollRequestsStatus deleteRequest(Integer requestId) {
    PayrollRequests request = payrollRequestsRepository.findById(requestId).orElse(null);

    if (request == null) {
      return PayrollRequestsStatus.REQUEST_NOT_FOUND;
    } else {
      payrollRequestsRepository.deleteById(requestId);
      return PayrollRequestsStatus.SUCCESS;
    }
  }

  private enum UpdateField {
    day,
    month,
    year
  }

  private Pair<PayrollRequestsStatus, Map<String, Integer>> checkError(Map<String, Object> updates,
                                                               List<UpdateField> flags) {
    Map<String, Integer> data = new HashMap<>();
    try {
      for (UpdateField field : flags) {
        Integer value = (Integer) updates.get(field.name());
        if (value == null) {
          return Pair.of(PayrollRequestsStatus.INVALID_DATA, data);
        }
        data.put(field.name(), value);
      }
      try {
        LocalDate unusedDate;
        if (data.get("year") != null && data.get("month") != null && data.get("day") != null) {
          unusedDate = LocalDate.of(data.get("year"), data.get("month"), data.get("day"));
        } else if (data.get("month") != null && data.get("year") != null) {
          unusedDate = LocalDate.of(data.get("year"), data.get("month"), 1);
        }
      } catch (Exception e) {
        return Pair.of(PayrollRequestsStatus.INVALID_FORMAT, data);
      }
    } catch (Exception e) {
      return Pair.of(PayrollRequestsStatus.INVALID_FORMAT, data);
    }

    return Pair.of(PayrollRequestsStatus.OK, data);
  }


}
