package com.nullterminators.project.controller;

import com.nullterminators.project.repository.EmployeeProfileManagementRepository;
import com.nullterminators.project.service.EmployeeProfileManagementService;
import com.nullterminators.project.service.UserLoginDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/client/employeeProfile")
public class EmployeeProfileController {

  @Autowired
  private EmployeeProfileManagementRepository employeeProfileManagementRepository;

  @Autowired
  private UserLoginDetailsService userLoginDetailsService;

  @Autowired
  private EmployeeProfileManagementService employeeProfileManagementService;

  /**
   * Creates employee and saves it in the client DB.
   * @param name Name of new employee
   * @param phoneNumber Phone Number of new employee
   * @param gender Gender of new employee
   * @param age Age of new employee 
   * @param startDate Starting date of new employee
   * @param designation Designation of new employee
   * @param email Email of new employee
   * @param emergencyContact Emergency Contact of new employee
   * @param baseSalary Base Salary of new employee
   * @return id of the new employee
   */
  @PostMapping("/createNewEmployeeAndAddToClientDB")
  public ResponseEntity<?> createNewEmployeeAndAddToClientDB(@RequestParam String name, @RequestParam String phoneNumber
      , @RequestParam String gender, @RequestParam int age, @RequestParam LocalDate startDate
      , @RequestParam String designation, @RequestParam String email, @RequestParam String emergencyContact
      , @RequestParam int baseSalary, @RequestParam String username, @RequestParam String password) {
      // creating employee
      int employeeId;
        try {
          employeeId = employeeProfileManagementService.createEmployee(name, phoneNumber, gender
              , age, startDate, designation, email, emergencyContact, baseSalary);

          if (employeeId != -1) {
            // creating a user for the employee
            String error = userLoginDetailsService.createUser(employeeId, username, password, designation);
            if (!error.isEmpty()) {
              return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
            }
            // adding employee to client db
            employeeProfileManagementService.addEmployeeToClientDatabase(employeeId, designation);
            return new ResponseEntity<>(employeeId, HttpStatus.CREATED);
          } else {
            return new ResponseEntity<>("Failed to create employee profile.", HttpStatus.INTERNAL_SERVER_ERROR);
          }
      } catch (Exception e) {
        return new ResponseEntity<>("Error in client service : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
      }
  }

  /**
   * Returns list of IDs of employees in client.
   * @return
   */
  @GetMapping("/getAllEmployeesInClient")
  public ResponseEntity<?> getAllEmployeesInClient() {
    try{
      List<Integer> allEmployeeIds = employeeProfileManagementService
          .getAllEmployees();
      List<Integer> clientEmployees = new ArrayList<>();

      for (Integer id : allEmployeeIds) {
        if (employeeProfileManagementRepository.existsById(id)) {
          clientEmployees.add(id);
        }
      }
      return new ResponseEntity<>(clientEmployees, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>("Error in client service : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Get employee by id.
   * @param id
   * @return
   */
  @GetMapping("/getEmployee/{id}")
  public ResponseEntity<?> getEmployeeInClient(@PathVariable int id) {
    try{
      String employee = employeeProfileManagementService
          .getEmployee(id);
      return new ResponseEntity<>(employee, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>("Error in client service : " + e.getMessage()
          , HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Delete employee by id.
   * @param id
   * @return
   */
  @DeleteMapping("/deleteEmployee/{id}")
  public ResponseEntity<?> deleteEmployee(@PathVariable int id) {
    try{
      boolean employee = employeeProfileManagementService
          .deleteEmployee(id);
      return new ResponseEntity<>(employee, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>("Error in client service : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}
