package com.nullterminators.project.repository;

import com.nullterminators.project.model.PayrollRequests;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PayrollRequestsRepository extends JpaRepository<PayrollRequests, Integer> {
    List<PayrollRequests> findAllByEmpIdOrderByCreatedDate(Integer empId);

    @Query(value = "SELECT * FROM payroll_requests p "
            + "WHERE p.approved = 1 "
            + "ORDER BY p.created_date DESC", nativeQuery = true)
    List<PayrollRequests> findAllByApprovedStatus();
}
