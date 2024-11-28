# 4156-Team-Project-Client

This GitHub repository hosts the **client-side** implementation of the Team Project for COMS 4156: Advanced Software Engineering. Our team, NullTerminators, consists of the following members: Abhilash Ganga (ag4797), Ajit Sharma Kasturi (ak5055), Hamsitha Challagundla (hc3540), Madhura Chatterjee (mc5470), Samhit Chowdary Bhogavalli (sb4845)
## Viewing the Service Repository
Please use the following link to view the repository relevant to the service: https://github.com/Samhit-Chowdary/4156-Team-project

## About our Client
Our client application is designed specifically for **hospitals** and serves as a comprehensive tool for managing administrative and operational tasks. It enables the hospital's admin team to efficiently create and maintain records for doctors, nurses, and patients.

Doctors and nurses can use the client to access information about the patients they are currently assigned to and retrieve details about their personal profiles, payroll, time-off requests, and organizational hierarchy. They can also submit requests, such as leave applications or other approvals, which are managed by the admin team.

By providing a free and convenient platform, our application makes it easier than ever for hospitals to collaborate by sharing comments on patient diagnoses, prescriptions, and other critical information.

Details about how our app integrates with our service and its specific functionalities are described below.

### Client Functionality
1. Patients Management:
    * Create, update, and delete patient records.
    * View patient information, such as name, date of visit, age, and other relevant details.
    * Access patient records, such as medical history, prescriptions, and other relevant information.

2. Payroll Management:
    * Generate payslips for employees (Doctors and Nurses).
    * View Payroll information, such as salary, tax deducted, and other relevant details for doctors and nurses.
    * Download payslips in PDF format for employees (Doctors and Nurses)
    * Create and Manage Payroll for employees (Doctors and Nurses) including adding and removing payroll records, updating salaries, and other relevant information for authorized employees (Supervisors).

3. Timeoff Management:
    * Request time off for employees (Doctors and Nurses).
    * Approve time off requests for employees (Doctors and Nurses).
    * View and manage time off requests for employees (Doctors and Nurses).

4. Employee Client Hierarchy Controller:
    * Useful to maintain hierarchies of doctors and nurses.
    * Provide functionalities to get the parent hierarchy of a doctor or nurse till certain height and remove a doctor or nurse and adjust the hierarchy tree accordingly.
    * These functionalities use employee hierarchy service's endpoints to perform relevant core operations.

5. Employee Profile Management Controller:
    * Maintain a list of employee ID and designation for client.
    * Get all clent employees, get client employee by id and delete employee by id.
    * Create new employee and add (id, designation) to the client database.

### How it works with our Service
This is the client service that can be used by hospitals to manage different categories of employees and patients in the hospital.
All the functionalities of this client service regarding the employees (doctors, nurses, IT) uses our employee management service to perform core operations and then build various functionalities using those core endpoints. We perform
complex aggregations and store some additional information wherever required on the client side but most of the data is stored in the employee management service.


### What makes our App Better/Faster/Cheaper than prior Solutions
Our app is free to use and offers advanced functionalities such as profile management, hierarchy management, payroll management, payslip generation, and time-off management, all powered by our core employee management service.

This design provides tremendous flexibility for clients to focus on their unique business logic while leveraging our standard core features. For instance, in patient management, clients can concentrate on implementing custom logic for managing patient records while relying on our services for handling employee management, including doctors and nurses.

Take hierarchy management as an example: if a client wants to retrieve a list of doctors managed by a specific doctor within a hierarchy up to a certain depth (as defined by the **getManagersUpToHeight** API), they can build this functionality using our **getSupervisorByEmployeeId** API endpoint. By layering their custom logic over our robust services, clients can tailor solutions to their specific needs with ease.

## Building and Running a Local Instance

To run the client and service repositories locally, follow these steps:

1. **Install Dependencies:**
   - Ensure you have Maven installed on your system. Use this [link](https://archive.apache.org/dist/maven/maven-3/3.9.5/source/) to download the maven package
     and follow the instructions mentioned in [here](https://maven.apache.org/guides/getting-started/windows-prerequisites.html)
   - OpenJDK 17.0.12 - Use this [link](https://www.openlogic.com/openjdk-downloads) to download the OpenJDK package
   - Sync the service repository using the command: https://github.com/Samhit-Chowdary/4156-Team-project.git
   - Sync the client repository using the command: https://github.com/Samhit-Chowdary/4156-Team-project-client.git


2. **Start the Service Repository:**
   - Navigate to the service repository directory.
   - Build the service using the following command.
     ```bash
     mvn install
     ```
   - Use the following command to start the server on port 8080:
     ```bash
     mvn spring-boot:run
     ```
   - Ensure the service is up and running on `http://localhost:8080`.


3. **Start the Client Repository:**
   - Navigate to the client repository directory. 
   - Build the service using the following command.
     ```bash
     mvn install
     ```
   - Use the following command to start the client on port 8081:
     ```bash
     mvn spring-boot:run
     ```
   - Ensure the client is up and running on `http://localhost:8081`.


4. **Testing Employee Management Features:**
   - Access the client application through application like postman at `http://localhost:8081`.
   - Test various features provided by the client:
     - **Patients Management:** The information of patients and their records and auth are managed by the client database itself.
     - **Payroll Management:** Navigate to the payroll section and verify functionalities such as viewing and generating payslips.
     - **Profile Management:** Access the profile management features to update and view employee profiles.
     - **Timeoff Management:** Test requesting and approving time off for employees.
     - **Hierarchy Management:** Use the hierarchy functionalities to view and manage the employee hierarchy, including retrieving supervisors and subordinates.

Ensure that all interactions with the client are correctly interfacing with the service running on port 8080.

## End to End Testing
* Employee Client Hierarchy Controller:
   1. **Get Subordinates of an Employee:**
      * Endpoint: `GET /subordinates/{employeeId}`
      * Testing: Send a GET request to the endpoint with a valid employee ID. Verify that the response contains a list of subordinates of the given employee.
   2. **Get Supervisor of an Employee:**
      * Endpoint: `GET /supervisor/{employeeId}`
      * Testing: Send a GET request to the endpoint with a valid employee ID. Verify that the response contains the ID of the supervisor of the given employee.
   3. **Get Subtree of an Employee:**
      * Endpoint: `GET /tree/{employeeId}`
      * Testing: Send a GET request to the endpoint with a valid employee ID. Verify that the response contains the subtree of the given employee.
   4. **Add Supervisor-Employee Edge:**
      * Endpoint: `POST /addEdge/{supervisorId}/{employeeId}`
      * Testing: Send a POST request to the endpoint with valid supervisor and employee IDs. Verify that the response contains a status of OK and that the edge is added to the database.
   5. **Remove Supervisor-Employee Edge:**
      * Endpoint: `DELETE /removeEdge/{employeeId}`
      * Testing: Send a DELETE request to the endpoint with a valid employee ID. Verify that the response contains a status of OK and that the edge is removed from the database.
   6. **Delete Employee and Reassign Subordinates:**
      * Endpoint: `DELETE /delete/{employeeId}`
      * Testing: Send a DELETE request to the endpoint with a valid employee ID. Verify that the response contains a status of OK and that the employee is deleted and subordinates are reassigned.
   7. **Get Managers Up to a Certain Height:**
      * Endpoint: `GET /managers/{employeeId}/{height}`
      * Testing: Send a GET request to the endpoint with valid employee ID and height. Verify that the response contains a list of manager IDs up to the specified height.
   8. **Get Payroll by Employee ID:**
      * Endpoint: `GET /payroll/{employeeId}`
      * Testing: Send a GET request to the endpoint with a valid employee ID. Verify that the response contains the payroll of the given employee.
   9. **Create Paryoll by Employee ID:**
      * Endpoint: `POST /payroll/{employeeId}/addPayroll`
      * Testing: Send a POST request to the endpoint with a valid employee ID. Verify that the response contains a status of OK and that the payroll is created.
  10. **Delete Payroll by Employee ID:**
      * Endpoint: `DELETE /payroll/{employeeId}/deletePayroll`
      * Testing: Send a DELETE request to the endpoint with a valid employee ID. Verify that the response contains a status of OK and that the payroll is deleted.
  11. **Create Payroll for Entire Company:**
      * Endpoint: `POST /payroll/createPayroll`
      * Testing: Send a POST request to the endpoint. Verify that the response contains a status of OK and that the payroll is created for all employees.
  12. **Delete Payroll for Entire Company:**
      * Endpoint: `DELETE /payroll/deletePayroll`
      * Testing: Send a DELETE request to the endpoint. Verify that the response contains a status of OK and that the payroll is deleted for all employees.
  13. **Get Timeoff by Employee ID:**
      * Endpoint: `GET /client/timeoff/{requestorEmployeeId}/{requestedEmployeeId}`
      * Testing: Send a GET request to the endpoint with a valid requestor and requested employee ID. Verify that the response contains the timeoff of the given employee.
  14. **Get Timeoff by Employee ID within a Specific Date Range:**
      * Endpoint: `GET /client/timeoff/{requestorEmployeeId}/{requestedEmployeeId}/range`
      * Testing: Send a GET request to the endpoint with a valid requestor and requested employee ID. Verify that the response contains list of timeoffs of the given employee within the specified date range.
  15. **Create Timeoff by Employee ID:**
      * Endpoint: `POST /client/timeoff/create`
      * Testing: Send a POST request to the endpoint with a valid employee ID. Verify that the response contains a status of OK and that the timeoff is created.
  16. **Update Timeoff by Employee ID:**
      * Endpoint: `PUT /client/timeoff/{requestorEmployeeId}/{requestedEmployeeId}/{timeOffId}/update-status`
      * Testing: Send a PUT request to the endpoint with valid requestor and requested employee ID. Verify that the response contains a status of OK and that the timeoff is updated.
  17. **Create new employee and add it to Client DB:**
      * Endpoint: `POST /client/employeeProfile/createNewEmployeeAndAddToClientDB`
      * Testing: Send a POST request to the endpoint with data. Verify that the response contains a status of OK and the created employee id.
  18. **Get all employees in Client:**
      * Endpoint: `GET /client/employeeProfile/getAllEmployeesInClient`
      * Testing: Send a GET request to the endpoint . Verify that the response contains a status of OK and the list of employee IDs.
  19. **Get employee in by Employee ID:**
      * Endpoint: `GET /client/getEmployee/{id}`
      * Testing: Send a GET request to the endpoint with an employee ID. Verify that the response contains a status of OK and the corresponding employee details.
  20. **Delete employee in by Employee ID:**
      * Endpoint: `DELETE /client/deleteEmployee/{id}`
      * Testing: Send a DELETE request to the endpoint with an employee ID. Verify that the response contains a status of OK and that employee has been deleted.
  

## Tools used 
- **Spring Boot:** For building the backend services and RESTful APIs.
- **Hibernate:** For ORM (Object-Relational Mapping) and database interactions.
- **Maven:** For project build and dependency management.
- **JUnit:** For unit testing and ensuring code quality.
- **Postman:** For API testing and interaction with backend services.
- **Git:** For version control and collaboration among team members.
- **IntelliJ IDEA:** As the primary IDE for developing Java applications.
