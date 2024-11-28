package com.nullterminators.project.controller;

import com.nullterminators.project.service.PayrollService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * API Endpoints for Payroll.
 */
@RestController
@RequestMapping("/payroll")
public class PayrollController {

  private final PayrollService payrollService;

  @Autowired
  public PayrollController(PayrollService payrollService) {
    this.payrollService = payrollService;
  }

  /**
   * Get Payroll by employee id.
   *
   * @param employeeId       A (@code int) representing the employee id the user wishes to get
   *                         the payrolls for.
   * @return                 A (@code ResponseEntity) object containing either a list of required
   *                         details and an HTTP 200 response or, an appropriate message
   *                         indicating the proper response.
   */
  @GetMapping(value = "/{employeeId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> getPayrollByEmployeeId(@PathVariable("employeeId") Integer employeeId) {
    try {
      Pair<HttpStatus, Object> result = payrollService.getPayrollByEmployeeId(employeeId);
      if (result.getFirst() == HttpStatus.INTERNAL_SERVER_ERROR) {
        return new ResponseEntity<>(Map.of("response", "Internal Server Error in Service"),
                HttpStatus.NO_CONTENT);
      }
      return new ResponseEntity<>(result.getSecond(), result.getFirst());
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Attempt to create a payroll entry.
   *
   * @param employeeId       A (@code int) representing the employee id the user wishes to delete
   *                         the payroll for.
   * @param updates          A (@code Map) of details that the user provides to update the
   *                         payroll for a particular month and year.
   * @return                 A (@code ResponseEntity) object containing either a response and
   *                         an HTTP 200 response or, an appropriate message indicating the
   *                         proper response.
   */
  @PostMapping(value = "/{employeeId}/addPayroll", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> createPayrollByEmployeeId(@PathVariable("employeeId") Integer employeeId,
                                                     @RequestBody Map<String, Object> updates) {
    try {
      Pair<HttpStatus, Object> result =
              payrollService.createPayrollByEmployeeId(employeeId, updates);
      if (result.getFirst() == HttpStatus.INTERNAL_SERVER_ERROR) {
        return new ResponseEntity<>(Map.of("response", "Internal Server Error in Service"),
                HttpStatus.NO_CONTENT);
      }
      return new ResponseEntity<>(result.getSecond(), result.getFirst());
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Attempt to delete a payroll entry.
   *
   * @param employeeId       A (@code int) representing the employee id the user wishes to delete
   *                         the payroll for.
   * @param updates          A (@code Map) of details that the user provides to update the
   *                         payroll for a particular month and year.
   * @return                 A (@code ResponseEntity) object containing either a response and
   *                         an HTTP 200 response or, an appropriate message indicating the
   *                         proper response.
   */
  @DeleteMapping(value = "/{employeeId}/deletePayroll", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> deletePayrollByEmployeeId(@PathVariable("employeeId") Integer employeeId,
                                                     @RequestBody Map<String, Object> updates) {
    try {
      Pair<HttpStatus, Object> result =
              payrollService.deletePayrollByEmployeeId(employeeId, updates);
      if (result.getFirst() == HttpStatus.INTERNAL_SERVER_ERROR) {
        return new ResponseEntity<>(Map.of("response", "Internal Server Error in Service"),
                HttpStatus.NO_CONTENT);
      }
      return new ResponseEntity<>(result.getSecond(), result.getFirst());
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Attempt to generate a payroll for the company.
   *
   * @param updates       A (@code Map) of details that the team provides to generate
   *                      the payrolls for a particular month and year.
   * @return              A (@code ResponseEntity) object containing either a response and
   *                      an HTTP 200 response or, an appropriate message indicating the
   *                      proper response.
   */
  @PostMapping(value = "/generatePayroll", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> generatePayroll(@RequestBody Map<String, Object> updates) {
    try {
      Pair<HttpStatus, Object> result = payrollService.generatePayroll(updates);
      if (result.getFirst() == HttpStatus.INTERNAL_SERVER_ERROR) {
        return new ResponseEntity<>(Map.of("response", "Internal Server Error in Service"),
                HttpStatus.NO_CONTENT);
      }
      return new ResponseEntity<>(result.getSecond(), result.getFirst());
    } catch (Exception e) {
      return handleException(e);
    }
  }

  /**
   * Attempt to delete a payroll for the company.
   *
   * @param updates       A (@code Map) of details that the team provides to delete
   *                      the payrolls for a particular month and year.
   * @return              A (@code ResponseEntity) object containing either a response and
   *                      an HTTP 200 response or, an appropriate message indicating the
   *                      proper response.
   */
  @DeleteMapping(value = "/deletePayroll", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> deletePayroll(@RequestBody Map<String, Object> updates) {
    try {
      Pair<HttpStatus, Object> result = payrollService.deletePayroll(updates);
      if (result.getFirst() == HttpStatus.INTERNAL_SERVER_ERROR) {
        return new ResponseEntity<>(Map.of("response", "Internal Server Error in Service"),
                HttpStatus.NO_CONTENT);
      }
      return new ResponseEntity<>(result.getSecond(), result.getFirst());
    } catch (Exception e) {
      return handleException(e);
    }
  }

  private ResponseEntity<?> handleException(Exception e) {
    return new ResponseEntity<>(Map.of("response", e.toString()),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
