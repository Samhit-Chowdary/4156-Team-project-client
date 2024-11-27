package com.nullterminators.project.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EmployeeHierarchyClientControllerTest {

    @InjectMocks
    private EmployeeHierarchyClientController clientController;

    @Mock
    private RestTemplate restTemplate;

    private final HttpHeaders headers = new HttpHeaders();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        String auth = "hardcodedUsername:hardcodedPassword";
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        headers.set("Authorization", "Basic " + encodedAuth);
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Test
    void testGetSubordinates() {
        Long employeeId = 1L;
        List<Long> subordinates = List.of(2L, 3L);

        when(restTemplate.exchange(
                "http://localhost:8080/api/employee-hierarchy/subordinates/1",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                List.class
        )).thenReturn(new ResponseEntity<>(subordinates, HttpStatus.OK));

        ResponseEntity<?> response = clientController.getSubordinates(employeeId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(subordinates, response.getBody());
    }

    @Test
    void testGetSupervisor() {
        Long employeeId = 1L;
        Long supervisorId = 10L;

        when(restTemplate.exchange(
                "http://localhost:8080/api/employee-hierarchy/supervisor/1",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Object.class
        )).thenReturn(new ResponseEntity<>(supervisorId, HttpStatus.OK));

        ResponseEntity<?> response = clientController.getSupervisor(employeeId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(supervisorId, response.getBody());
    }

    @Test
    void testDeleteEmployeeWithSupervisorAndSubordinates() {
        Long employeeId = 1L;
        Long supervisorId = 10L;
        List<Long> subordinates = List.of(2L, 3L);

        // Mock supervisor response
        when(restTemplate.exchange(
                "http://localhost:8080/api/employee-hierarchy/supervisor/1",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Object.class
        )).thenReturn(new ResponseEntity<>(supervisorId, HttpStatus.OK));

        // Mock subordinates response
        when(restTemplate.exchange(
                "http://localhost:8080/api/employee-hierarchy/subordinates/1",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                List.class
        )).thenReturn(new ResponseEntity<>(subordinates, HttpStatus.OK));

        // Mock add edge and remove edge calls
        for (Long subordinateId : subordinates) {
            when(restTemplate.exchange(
                    "http://localhost:8080/api/employee-hierarchy/addEdge/10/" + subordinateId,
                    HttpMethod.POST,
                    new HttpEntity<>(headers),
                    String.class
            )).thenReturn(new ResponseEntity<>("Edge added successfully.", HttpStatus.OK));
        }

        when(restTemplate.exchange(
                "http://localhost:8080/api/employee-hierarchy/removeEdge/1",
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                String.class
        )).thenReturn(new ResponseEntity<>("Edge removed successfully.", HttpStatus.OK));

        ResponseEntity<?> response = clientController.deleteEmployeeAndReassign(employeeId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Employee deleted and subordinates reassigned successfully.", response.getBody());
    }

    @Test
    void testDeleteEmployeeWithNoSupervisorButHasSubordinates() {
        Long employeeId = 1L;
        List<Long> subordinates = List.of(2L, 3L);

        // Mock supervisor response as error
        when(restTemplate.exchange(
                "http://localhost:8080/api/employee-hierarchy/supervisor/1",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Object.class
        )).thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        // Mock subordinates response
        when(restTemplate.exchange(
                "http://localhost:8080/api/employee-hierarchy/subordinates/1",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                List.class
        )).thenReturn(new ResponseEntity<>(subordinates, HttpStatus.OK));

        ResponseEntity<?> response = clientController.deleteEmployeeAndReassign(employeeId);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Cannot delete employee with ID 1 as they have subordinates but no supervisor.", response.getBody());
    }

    @Test
    void testDeleteEmployeeWithNoSupervisorAndNoSubordinates() {
        Long employeeId = 1L;
        List<Long> subordinates = List.of();

        // Mock supervisor response as error
        when(restTemplate.exchange(
                "http://localhost:8080/api/employee-hierarchy/supervisor/1",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Object.class
        )).thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        // Mock subordinates response as empty list
        when(restTemplate.exchange(
                "http://localhost:8080/api/employee-hierarchy/subordinates/1",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                List.class
        )).thenReturn(new ResponseEntity<>(subordinates, HttpStatus.OK));

        ResponseEntity<?> response = clientController.deleteEmployeeAndReassign(employeeId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Employee deleted and subordinates reassigned successfully.", response.getBody());
    }
}
