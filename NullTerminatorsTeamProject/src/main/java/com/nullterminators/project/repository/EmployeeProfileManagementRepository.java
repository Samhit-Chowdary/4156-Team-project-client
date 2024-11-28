package com.nullterminators.project.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nullterminators.project.model.EmployeeProfileManagement;

/**
 * Repository for storing employee profile DB.
 */
public interface EmployeeProfileManagementRepository extends JpaRepository<EmployeeProfileManagement, Integer> {

    /*
    * Return count of every designation.
    */
    @Query("SELECT e.designation AS designation," + 
        " COUNT(e) AS total FROM EmployeeProfileManagement e GROUP BY e.designation")
    List<Map<String, Object>> findDesignationAndTotalCounts();
      
  }
