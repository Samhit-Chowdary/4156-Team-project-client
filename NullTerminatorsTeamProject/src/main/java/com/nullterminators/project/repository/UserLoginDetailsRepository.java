package com.nullterminators.project.repository;

import com.nullterminators.project.model.UserLoginDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserLoginDetailsRepository extends JpaRepository<UserLoginDetails, Integer> {
    Optional<UserLoginDetails> findByUsername(String username);
}
