# Software System

## Software requirements
1) For development:
   1) java 21
   2) docker

## Components
1) user-service
   ### Features
   1) Create new account
   2) Login
   3) Logout
   4) Add user settings
   5) Get user settings
2) task-mgmt-service
   ### Features
    1) Create new task
    2) Get task by id
    3) Delete task by id
    4) Update task by id
    5) Filter tasks
3) notification-processor-service
   ### Features
   1) Will listen on /notifications exchange and check if a notification should be sent or not. The logic depends on User Settings.
4) notification-server-service
   ### Features
   1) Will listen on **/notifications/email** and **/notifications/realtime** topics and send the notification on the particular channel.

## Component diagram
![alt text](./assets/system_design.png)

## Run application
1) Every application can be started individually. Please check the env variables that should be configured by taking checking the _deployment/docker-compose.yaml_ file.
2) In order to run the entire application (**FE + BE**) in docker:
   ```shell
      cd deployment
      ./start-services.sh
    ```
    Application is accessible on: http://localhost:8080   
    Swagger will be accessible on: http://localhost:8080/api/swagger-ui.html (definition for both microservices can be selected from the upper right)

