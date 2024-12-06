package com.nullterminators.project.controller;

import com.nullterminators.project.enums.PayrollRequestsStatus;
import com.nullterminators.project.service.PayrollService;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * API Endpoints for Payroll.
 */
@RestController
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
  @GetMapping(value = "/client/payroll/{employeeId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> getPayrollByEmployeeId(@PathVariable("employeeId") Integer employeeId) {
    try {
      Pair<HttpStatus, Object> result = payrollService.getPayrollByEmployeeId(employeeId);
      if (result.getFirst() == HttpStatus.INTERNAL_SERVER_ERROR) {
        return new ResponseEntity<>(Map.of("response", "Internal Server Error in Service"),
                HttpStatus.INTERNAL_SERVER_ERROR);
      }
      return new ResponseEntity<>(result.getSecond(), result.getFirst());
    } catch (Exception e) {
      return handleException(e);
    }
  }

  @PatchMapping(value = "/payroll/{employeeId}/markPaid", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> markPaid(@PathVariable("employeeId") Integer employeeId,
                                    @RequestBody Map<String, Object> updates) {
    try {
      Pair<HttpStatus, Object> result = payrollService.markAsPaid(employeeId, updates);
      if (result.getFirst() == HttpStatus.INTERNAL_SERVER_ERROR) {
        return new ResponseEntity<>(Map.of("response", "Internal Server Error in Service"),
                HttpStatus.INTERNAL_SERVER_ERROR);
      }
      return new ResponseEntity<>(result.getSecond(), result.getFirst());
    } catch (Exception e) {
      return handleException(e);
    }
  }

  @PatchMapping(value = "/payroll/{employeeId}/markUnpaid", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> markUnpaid(@PathVariable("employeeId") Integer employeeId,
                                      @RequestBody Map<String, Object> updates) {
    try {
      Pair<HttpStatus, Object> result = payrollService.markAsUnpaid(employeeId, updates);
      if (result.getFirst() == HttpStatus.INTERNAL_SERVER_ERROR) {
        return new ResponseEntity<>(Map.of("response", "Internal Server Error in Service"),
                HttpStatus.INTERNAL_SERVER_ERROR);
      }
      return new ResponseEntity<>(result.getSecond(), result.getFirst());
    } catch (Exception e) {
      return handleException(e);
    }
  }

  @PatchMapping(value = "/payroll/{employeeId}/adjustDay", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> markPending(@PathVariable("employeeId") Integer employeeId,
                                       @RequestBody Map<String, Object> updates) {
    try {
      Pair<HttpStatus, Object> result = payrollService.adjustDay(employeeId, updates);
      if (result.getFirst() == HttpStatus.INTERNAL_SERVER_ERROR) {
        return new ResponseEntity<>(Map.of("response", "Internal Server Error in Service"),
                HttpStatus.INTERNAL_SERVER_ERROR);
      }
      return new ResponseEntity<>(result.getSecond(), result.getFirst());
    } catch (Exception e) {
      return handleException(e);
    }
  }

  @PatchMapping(value = "/payroll/{employeeId}/adjustSalary", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> adjustSalary(@PathVariable("employeeId") Integer employeeId,
                                        @RequestBody Map<String, Object> updates) {
    try {
      Pair<HttpStatus, Object> result = payrollService.adjustSalary(employeeId, updates);
      if (result.getFirst() == HttpStatus.INTERNAL_SERVER_ERROR) {
        return new ResponseEntity<>(Map.of("response", "Internal Server Error in Service"),
                HttpStatus.INTERNAL_SERVER_ERROR);
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
  @PostMapping(value = "/payroll/{employeeId}/addPayroll", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> createPayrollByEmployeeId(@PathVariable("employeeId") Integer employeeId,
                                                     @RequestBody Map<String, Object> updates) {
    try {
      Pair<HttpStatus, Object> result =
              payrollService.createPayrollByEmployeeId(employeeId, updates);
      if (result.getFirst() == HttpStatus.INTERNAL_SERVER_ERROR) {
        return new ResponseEntity<>(Map.of("response", "Internal Server Error in Service"),
                HttpStatus.INTERNAL_SERVER_ERROR);
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
  @DeleteMapping(value = "/payroll/{employeeId}/deletePayroll", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> deletePayrollByEmployeeId(@PathVariable("employeeId") Integer employeeId,
                                                     @RequestBody Map<String, Object> updates) {
    try {
      Pair<HttpStatus, Object> result =
              payrollService.deletePayrollByEmployeeId(employeeId, updates);
      if (result.getFirst() == HttpStatus.INTERNAL_SERVER_ERROR) {
        return new ResponseEntity<>(Map.of("response", "Internal Server Error in Service"),
                HttpStatus.INTERNAL_SERVER_ERROR);
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
  @PostMapping(value = "/payroll/generatePayroll", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> generatePayroll(@RequestBody Map<String, Object> updates) {
    try {
      Pair<HttpStatus, Object> result = payrollService.generatePayroll(updates);
      if (result.getFirst() == HttpStatus.INTERNAL_SERVER_ERROR) {
        return new ResponseEntity<>(Map.of("response", "Internal Server Error in Service"),
                HttpStatus.INTERNAL_SERVER_ERROR);
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
  @DeleteMapping(value = "/payroll/deletePayroll", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> deletePayroll(@RequestBody Map<String, Object> updates) {
    try {
      Pair<HttpStatus, Object> result = payrollService.deletePayroll(updates);
      if (result.getFirst() == HttpStatus.INTERNAL_SERVER_ERROR) {
        return new ResponseEntity<>(Map.of("response", "Internal Server Error in Service"),
                HttpStatus.INTERNAL_SERVER_ERROR);
      }
      return new ResponseEntity<>(result.getSecond(), result.getFirst());
    } catch (Exception e) {
      return handleException(e);
    }
  }

  @GetMapping(value = "/client/payroll/{employeeId}/getRequests", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> getRequests(@PathVariable("employeeId") Integer employeeId) {
    try {
      Pair<PayrollRequestsStatus, List<Map<String, Object>>> result =
              payrollService.getRequests(employeeId);
      if (result.getFirst() == PayrollRequestsStatus.EMPLOYEE_NOT_FOUND) {
        return new ResponseEntity<>(Map.of("response",
                "Employee Not Found in Company"), HttpStatus.NOT_FOUND);
      } else if (result.getSecond().isEmpty()) {
        return new ResponseEntity<>(Map.of("response", "Details Not Found"), HttpStatus.NOT_FOUND);
      }
      return new ResponseEntity<>(result.getSecond(), HttpStatus.OK);
    } catch (Exception e) {
      return handleException(e);
    }
  }

  @PostMapping(value = "/client/payroll/{employeeId}/createRequest", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> createRequest(@PathVariable("employeeId") Integer employeeId, @RequestBody Map<String, Object> updates) {
    try {
      PayrollRequestsStatus result = payrollService.createRequest(employeeId, updates);
      return switch (result) {
        case EMPLOYEE_NOT_FOUND -> new ResponseEntity<>(Map.of("response",
                "Employee not found in Company"), HttpStatus.NOT_FOUND);
        case INVALID_DATA -> new ResponseEntity<>(Map.of("response",
                "Invalid month or year"), HttpStatus.BAD_REQUEST);
        case INVALID_FORMAT -> new ResponseEntity<>(Map.of("response",
                "Invalid format for month or year"), HttpStatus.BAD_REQUEST);
        case SUCCESS -> new ResponseEntity<>(Map.of("response",
                "Payroll Request has been created"), HttpStatus.OK);
        default -> new ResponseEntity<>(Map.of("response", "An Error has occurred"),
                HttpStatus.INTERNAL_SERVER_ERROR);
      };
    } catch (Exception e) {
      return handleException(e);
    }
  }

  @PatchMapping(value = "/client/payroll/{requestId}/updateRequest", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> updateRequest(@PathVariable("requestId") Integer requestId, @RequestBody Map<String, Object> updates) {
    try {
      PayrollRequestsStatus result = payrollService.updateRequest(requestId, updates);
      return switch (result) {
        case INVALID_FORMAT -> new ResponseEntity<>(Map.of("response",
                "Invalid format for status"), HttpStatus.BAD_REQUEST);
        case REQUEST_NOT_FOUND -> new ResponseEntity<>(Map.of("response",
                "Payroll Request not found"), HttpStatus.NOT_FOUND);
        case SUCCESS -> new ResponseEntity<>(Map.of("response",
                "Payroll Request has been updated"), HttpStatus.OK);
        default -> new ResponseEntity<>(Map.of("response", "An Error has occurred"),
                HttpStatus.INTERNAL_SERVER_ERROR);
      };
    } catch (Exception e) {
      return handleException(e);
    }
  }

  @DeleteMapping(value = "/client/payroll/{requestId}/deleteRequest", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> deleteRequest(@PathVariable("requestId") Integer requestId) {
    try {
      PayrollRequestsStatus result = payrollService.deleteRequest(requestId);
      return switch (result) {
        case REQUEST_NOT_FOUND -> new ResponseEntity<>(Map.of("response",
                "Payroll Request not found"), HttpStatus.NOT_FOUND);
        case SUCCESS -> new ResponseEntity<>(Map.of("response",
                "Payroll Request has been deleted"), HttpStatus.OK);
        default -> new ResponseEntity<>(Map.of("response", "An Error has occurred"),
                HttpStatus.INTERNAL_SERVER_ERROR);
      };
    } catch (Exception e) {
      return handleException(e);
    }
  }

  private ResponseEntity<?> handleException(Exception e) {
    return new ResponseEntity<>(Map.of("response", e.toString()),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
