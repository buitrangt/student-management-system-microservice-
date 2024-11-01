package org.aibles.auth.utils.service.impl;

import jdk.jfr.Category;
import lombok.extern.slf4j.Slf4j;
import org.aibles.auth.utils.dto.StudentRequest;
import org.aibles.auth.utils.entity.Account;
import org.aibles.auth.utils.entity.Role;
import org.aibles.auth.utils.entity.User;
import org.aibles.auth.utils.exception.EmailAlreadyExistsException;
import org.aibles.auth.utils.exception.ResourceNotFoundException;
import org.aibles.auth.utils.exception.UsernameAlreadyExistsException;
import org.aibles.auth.utils.repository.AccountRepository;
import org.aibles.auth.utils.repository.UserRepository;
import org.aibles.auth.utils.service.AccountService;
import org.aibles.auth.utils.service.UserService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;


@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final RestTemplate restTemplate;

    public UserServiceImpl(UserRepository userRepository, AccountRepository accountRepository, AccountService accountService, RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.accountService = accountService;
        this.restTemplate = restTemplate;
    }

    @Override
    public void registerStudent(User student, Account account, String confirmPassword) {
        log.info("Start Registering student");
        checkEmailAndUsernameExistence(student.getEmail(), account.getUsername());

        student.setRole(Role.STUDENT);
        userRepository.save(student);
        log.info("Student UserId {}", student.getUserId());

        accountService.createAccount(student.getUserId(), account.getUsername(), account.getPassword(), confirmPassword);
        log.info("Student account created");

        // Gọi Student Service để tạo Student
    //    createStudentInStudentService(student.getUserId(), student);

        log.info("Student registered successfully for username: {}", account.getUsername());
    }
    /**
     * Phương thức gọi đến Student Service để tạo bản ghi Student, sử dụng trực tiếp đối tượng User.
     */
//    private void createStudentInStudentService(String userId, User student) {
//        String studentServiceUrl = "http://localhost:8087/api/v1/students";
//
//        // Tạo StudentRequest với các thông tin cần thiết
//        StudentRequest studentRequest = new StudentRequest();
//        studentRequest.setUserId(userId);
//        studentRequest.setFullName(student.getFullName());
//        studentRequest.setEmail(student.getEmail());
//        studentRequest.setClassId(student.getClassId());
//
//        // Kiểm tra các trường có giá trị null và ghi log cảnh báo
//        if (student.getFullName() == null || student.getEmail() == null || student.getClassId() == null) {
//            log.warn("(createStudentInStudentService) Some fields are null: fullName={}, email={}, classId={}",
//                    student.getFullName(), student.getEmail(), student.getClassId());
//        }
//
//        // Tạo HttpEntity với header và body
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<StudentRequest> requestEntity = new HttpEntity<>(studentRequest, headers);
//
//        try {
//            restTemplate.postForObject(studentServiceUrl, requestEntity, Void.class);
//            log.info("Successfully created student in Student Service for userId: {}", userId);
//        } catch (Exception e) {
//            log.error("Failed to create student in Student Service for userId: {}", userId, e);
//            throw new RuntimeException("Failed to create student in Student Service", e);
//        }
//    }



    private void checkEmailAndUsernameExistence(String email, String username) {
        if (userRepository.existsByEmail(email)) {
            log.info("Email already exists");
            throw new EmailAlreadyExistsException("Email already exists", email);
        }
        if (accountRepository.existsByUsername(username)) {
            log.info("Username already exists {}", username);
            throw new UsernameAlreadyExistsException("Username already exists", username);
        }
    }


    @Override
    public void registerLecturer(User lecturer, Account account, String confirmPassword) {
        log.info("Start Registering lecturer");
        checkEmailAndUsernameExistence(lecturer.getEmail(), account.getUsername());

        lecturer.setRole(Role.LECTURER);
        userRepository.save(lecturer);
        log.info("Lecturer UserId {}", lecturer.getUserId());

        accountService.createAccount(lecturer.getUserId(), account.getUsername(), account.getPassword(), confirmPassword);
        log.info("Lecturer account created");

        log.info("Lecturer registered successfully for username: {}", account.getUsername());
    }


}


