package com.nullterminators.project.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TestingController {

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return new ResponseEntity<>(
                Map.of("response", "client is up and running"),
                HttpStatus.OK
        );
    }

}
