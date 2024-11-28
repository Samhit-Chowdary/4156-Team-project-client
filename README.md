# 4156-Team-project-client

This is the GitHub repository for the **client portion** of the Team Project associated with COMS 4156 Advanced Software Engineering. Our team name is NullTerminators and the following are our members: Ajit, Samhit, Abhilash, Hamsitha and Madhura.

## Viewing the Service Repository
Please use the following link to view the repository relevant to the service: https://github.com/griffinnewbold/COMS-4156-Service

## About our App
Our app targets **healthcare workers**, it is an app that allows for the employee profile, payroll, timeoff and hierarchy management across various kinds of people belonging to healthcare.

All of that and how our app specifically works with our service is described in more detail below.
### App Functionality
1. Employee Client Hierarchy Controller:
    * Useful to maintain hierarchies of doctors and nurses.
    * Provide functionalities to get the parent hierarchy of a doctor or nurse till certain height and remove a doctor or nurse and adjust the hierarchy tree accordingly.
    * These functionalities use employee hierarchy service's endpoints to perform relevant core operations.

### How it works with our Service
This is the client service that can be used by hospitals to manage different categories of employees and patients in the hospital.
All the functionalities of this client service regarding the employees (doctors, nurses, IT) uses our employee management service to perform core operations and then build various functionalities using those core endpoints. We perform
complex aggregations and store some additional information wherever required on the client side but most of the data is stored in the employee management service.


### What makes our App Better/Faster/Cheaper than prior Solutions
Our app is free to use and provides many complex functionalities like profile management, hierarchy management, payroll management, generating payslips, timoff management from our core employee management service.
So there is so much flexibility on the kinds of operations that our clients use! They can just focus on their business logic and use our standard core functionalities to write that business logic.
In our case, to perform patient management, our clients just use their own custom business logic just for the patient management and patient records management and use our services for employee management (doctors, nurses etc.) of the hospital. For example, in case of hierarchy service, we assumed our client wants to get list of doctors that are managed by a certain doctor in the hierarchy tree till a certain height (as described by the API endpoint **getManagersUpToHeight**). To develop this logic,
our client can simply make use of **getSupervisorByEmployeeId** API endpoint of our employee management service and develop their custom business logic on top of this.

## Building and Running a Local Instance

To run the client and service repositories locally, follow these steps:

1. **Start the Service Repository:**
   - Navigate to the service repository directory.
   - Use the following command to start the server on port 8080:
     ```bash
     mvn spring-boot:run
     ```
   - Ensure the service is up and running on `http://localhost:8080`.


2. **Start the Client Repository:**
   - Navigate to the client repository directory.
   - Use the following command to start the client on port 8081:
     ```bash
     mvn spring-boot:run
     ```
   - Ensure the client is up and running on `http://localhost:8081`.


3. **Testing Employee Management Features:**
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

## Tools used 
- **Spring Boot:** For building the backend services and RESTful APIs.
- **Hibernate:** For ORM (Object-Relational Mapping) and database interactions.
- **Maven:** For project build and dependency management.
- **JUnit:** For unit testing and ensuring code quality.
- **Postman:** For API testing and interaction with backend services.
- **Git:** For version control and collaboration among team members.
- **IntelliJ IDEA:** As the primary IDE for developing Java applications.
