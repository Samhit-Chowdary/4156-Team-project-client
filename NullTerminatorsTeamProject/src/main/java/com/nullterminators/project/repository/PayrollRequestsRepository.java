package com.nullterminators.project.repository;

import com.nullterminators.project.model.PayrollRequests;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayrollRequestsRepository extends JpaRepository<PayrollRequests, Integer> {
    List<PayrollRequests> findAllByEmpIdOrderByCreatedDate(Integer empId);
}
