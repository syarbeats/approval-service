# approval-service
microservice to handle approval process for published blog.

## Architecture

<img width="516" alt="Microservice Architecture" src="https://user-images.githubusercontent.com/18225438/70013213-8ac45380-15a9-11ea-9786-e42bc4bfacbb.PNG">

1. Service Registry: https://github.com/syarbeats/service-registry.git
2. Blog-config: https://github.com/syarbeats/blog-config.git
3. Blog-app-config: https://github.com/syarbeats/configuration-server.git
4. Blog Microservice: https://github.com/syarbeats/blog-microservices-v3.git
5. Gateway microservice: https://github.com/syarbeats/gateway-application-v3.git

## URL
1. Spring Eureuka URL: http://localhost:8761/
2. Swagger UI for Gateway App: http://localhost:8090/swagger-ui.html
3. Swagger UI for Blog Microservices: http://localhost:8081/swagger-ui.html
4. Swagger UI for Approval Microservices: http://localhost:8087/swagger-ui.html

## The Stacks:
1. Springboot 2.1.6
2. MySQL
3. Swagger-ui
4. MockMVC for Integration testing
5. Cucumber & JUnit for High Level's Like Integration Testing
6. Cucumber for Frontend High Level Testing (Frontend: https://github.com/syarbeats/blog-frontend-application.git)
7. Chrome Webdriver
8. Zuul Proxy

### Workflow

### User Registration

<img width="512" alt="flow" src="https://user-images.githubusercontent.com/18225438/61030681-1c0f0f80-a3e8-11e9-9322-e65e298b26d7.PNG">

