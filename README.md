# 4156-Team-Project-Client

This GitHub repository hosts the **client-side** implementation of the Team Project for COMS 4156: Advanced Software Engineering. Our team, NullTerminators, consists of the following members: Abhilash Ganga (ag4797), Ajit Sharma Kasturi (ak5055), Hamsitha Challagundla (hc3540), Madhura Chatterjee (mc5470), Samhit Chowdary Bhogavalli (sb4845)
## Viewing the Service Repository
Please use the following link to view the repository relevant to the service: https://github.com/Samhit-Chowdary/4156-Team-project

## About our Client
Our client targets **Hospitals**, it allows the admin team of the hospital to create for the employee profile, payroll, timeoff and hierarchy management across various kinds of people belonging to healthcare.
Role based access control is provided for the admin team to manage the employees and patients of the hospital.
Patient records management is also provided for the admin team to manage the records of the patients.


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

## End to End Testing

## Backend Checkstyle Report

## Continuous Integration Report

## A Final Note to Developers

## Tools used 

## Third Party API Documentation
