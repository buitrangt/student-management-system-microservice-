package org.aibles.auth.utils.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.aibles.auth.utils.controller.converter.AccountDtoConverter;
import org.aibles.auth.utils.controller.converter.LoginDtoConverter;
import org.aibles.auth.utils.controller.converter.UserDtoConverter;
import org.aibles.auth.utils.dto.*;
import org.aibles.auth.utils.entity.Account;
import org.aibles.auth.utils.entity.Role;
import org.aibles.auth.utils.entity.User;
import org.aibles.auth.utils.service.JWTService;
import org.aibles.auth.utils.service.LoginService;
import org.aibles.auth.utils.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
public class AuthController {

    private final UserService userService;
    private final LoginService loginService;
    private final JWTService jwtService;

    public AuthController(final UserService userService, final LoginService loginService, final JWTService jwtService) {
        this.userService = userService;
        this.loginService = loginService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register/student")
    public ApplicationResponse<String> registerStudent(@Valid @RequestBody RegisterUserRequest request) {
        log.info("Registering student: {}", request);
        User user = UserDtoConverter.toEntity(request);
        user.setRole(Role.STUDENT);
        Account account = AccountDtoConverter.toEntity(request, user);

        userService.registerStudent(user, account, request.getConfirmPassword());

        log.info("Registered Student Successfully");
        return ApplicationResponse.of(HttpStatus.CREATED.value(), "Student registered successfully.");
    }

    @PostMapping("/register/lecturer")
    public ApplicationResponse<String> registerLecturer(@Valid @RequestBody RegisterUserRequest request) {
        log.info("Registering lecturer: {}", request);
        User user = UserDtoConverter.toEntity(request);
        user.setRole(Role.LECTURER);
        Account account = AccountDtoConverter.toEntity(request, user);

        userService.registerLecturer(user, account, request.getConfirmPassword());

        log.info("Registered Lecturer Successfully");
        return ApplicationResponse.of(HttpStatus.CREATED.value(), "Lecturer registered successfully.");
    }

    @PostMapping("/login")
    public ResponseEntity<ApplicationResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest loginRequest) {
        log.info("=== Start API login ===");
        log.info("Request Body: {}", loginRequest);

        Map<String, Object> loginResult = loginService.login(
                loginRequest.getUsername(), loginRequest.getPassword());

        LoginResponse loginResponse = LoginDtoConverter.toLoginResponse(
                (String) loginResult.get("accessToken"),
                (String) loginResult.get("refreshToken"),
                (Long) loginResult.get("expiresIn"),
                (Long) loginResult.get("refreshExpiresIn")
        );

        ApplicationResponse<LoginResponse> applicationResponse = ApplicationResponse.of(
                HttpStatus.OK.value(), loginResponse);

        log.info("Login successful for user: {}", loginRequest.getUsername());
        log.info("=== Finish API login ===");

        return new ResponseEntity<>(applicationResponse, HttpStatus.OK);
    }

    @PostMapping("/verify-token")
    public ResponseEntity<ApplicationResponse<VerifyTokenResponse>> verifyToken(@RequestBody VerifyTokenRequest request) {
        VerifyTokenResponse response = jwtService.verifyToken(request);
        return ResponseEntity.ok(ApplicationResponse.of(HttpStatus.OK.value(), response));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApplicationResponse<String>> logout(@RequestHeader("Authorization") String authorizationHeader) {
        log.info("=== Start API logout ===");

        String accessToken = authorizationHeader.replace("Bearer ", "");

        loginService.logout(accessToken);

        log.info("Logout successful for token: {}", accessToken);
        log.info("=== Finish API logout ===");

        return ResponseEntity.ok(ApplicationResponse.of(HttpStatus.OK.value(), "Logout successful"));
    }
}
