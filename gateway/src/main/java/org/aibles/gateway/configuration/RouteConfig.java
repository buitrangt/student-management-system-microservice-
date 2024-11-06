package org.aibles.gateway.configuration;


import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("course-service-route", r -> r.path("/api/v1/courses")
                        .uri("lb://COURSE-SERVICE"))
                .route("student-service-route", r-> r.path("/api/v1/students")
                        .uri("lb://STUDENT-SERVICE"))
                .route("attendance-service-route", r->r.path("/api/v1/attendances")
                        .uri("lb://ATTENDANCE-SERVICE"))
                .route("grade-service-route", r->r.path("/api/v1/grades")
                        .uri("lb://GRADE-SERVICE"))
                .route("subject-service-route", r->r.path("/api/v1/subjects")
                        .uri("lb://SUBJECT-SERVICE"))
                .route("lecturer-service-route", r->r.path("/api/v1/lecturers")
                        .uri("lb://LECTURER-SERVICE"))
                .route("class-service-route", r->r.path("/api/v1/classes")
                        .uri("lb://CLASS-SERVICE"))
                .route("department-service-route", r->r.path("/api/v1/departments")
                        .uri("lb://DEPARTMENT-SERVICE"))
                .route("studentcourse-service-route", r->r.path("/api/v1/studentcourses")
                        .uri("lb://COURSE-SERVICE"))
                .route("subjectlecturer-service-route", r->r.path("/api/v1/subjectlecturers")
                        .uri("lb://SUBJECT-SERVICE"))
                .route("auth-service-login-route", r -> r.path("/api/v1/users/login")
                        .uri("lb://AUTH-SERVICE"))
                .route("auth-service-register-route", r -> r.path("/api/v1/users/register/**")
                        .uri("lb://AUTH-SERVICE"))
                .route("auth-service-verify-token-route", r -> r.path("/api/v1/users/verify-token")
                        .uri("lb://AUTH-SERVICE"))
                .route("auth-service-logout", r -> r.path("/api/v1/users/logout")
                        .uri("lb://AUTH-SERVICE"))

                .build();
    }
}
