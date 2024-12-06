package com.nullterminators.project.controller;

import com.nullterminators.project.service.EmployeeHierarchyService;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Client wrapper APIs for EmployeeHierarchyController. */
@RestController
@RequestMapping("/client/hierarchy")
public class EmployeeHierarchyClientController {

  private final EmployeeHierarchyService hierarchyService;

  public EmployeeHierarchyClientController(EmployeeHierarchyService hierarchyService) {
    this.hierarchyService = hierarchyService;
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
      List<Long> subordinates = hierarchyService.getSubordinates(employeeId);
      return ResponseEntity.ok(subordinates);
    } catch (Exception e) {
      return hierarchyService.handleException(e);
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
      Long supervisor = hierarchyService.getSupervisor(employeeId);
      return supervisor != null
          ? ResponseEntity.ok(supervisor)
          : ResponseEntity.badRequest().body("Supervisor not found");
    } catch (Exception e) {
      return hierarchyService.handleException(e);
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
      Object subtree = hierarchyService.getSubtree(employeeId);
      return ResponseEntity.ok(subtree);
    } catch (Exception e) {
      return hierarchyService.handleException(e);
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
      hierarchyService.addEdge(supervisorId, employeeId);
      return ResponseEntity.ok("Edge added successfully.");
    } catch (Exception e) {
      return hierarchyService.handleException(e);
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
      hierarchyService.removeEdge(employeeId);
      return ResponseEntity.ok("Edge removed successfully.");
    } catch (Exception e) {
      return hierarchyService.handleException(e);
    }
  }

  /**
   * Deletes an employee with the given ID and reassigns their subordinates to the employee's
   * supervisor. This method performs the following operations: 1. Fetches the supervisor of the
   * employee. 2. Fetches the subordinates of the employee. 3. If the employee has subordinates but
   * no supervisor, returns a BAD_REQUEST response. 4. If the employee has a supervisor, reassigns
   * all subordinates to the supervisor. 5. Removes the supervisor-employee edge. 6. Returns an OK
   * response upon successful deletion and reassignment.
   *
   * @param employeeId the ID of the employee to be deleted
   * @return a response entity indicating the status of the operation
   */
  @DeleteMapping("/delete/{employeeId}")
  public ResponseEntity<?> deleteEmployeeAndReassign(@PathVariable Long employeeId) {
    try {
      List<Long> rawSubordinates = hierarchyService.getSubordinates(employeeId);
      List<Long> subordinates = new ArrayList<>();
      for (Object item : rawSubordinates) {
        subordinates.add(Long.valueOf(((LinkedHashMap) item).get("toEmployeeId").toString()));
      }

      Long supervisor = hierarchyService.getSupervisor(employeeId);
      if (supervisor == null && !subordinates.isEmpty()) {
        return ResponseEntity.badRequest()
            .body("Cannot delete employee with subordinates but no supervisor.");
      }

      hierarchyService.reassignSubordinates(supervisor, subordinates);
      hierarchyService.removeEdge(employeeId);
      return ResponseEntity.ok("Employee deleted and subordinates reassigned successfully.");
    } catch (Exception e) {
      return hierarchyService.handleException(e);
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

    if (height <= 0) {
      return ResponseEntity.badRequest().body("Height must be a positive value!");
    }

    try {
      List<Long> managers = hierarchyService.getManagersUpToHeight(employeeId, height);
      return ResponseEntity.ok(managers);
    } catch (Exception e) {
      return hierarchyService.handleException(e);
    }
  }
}
