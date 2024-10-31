package org.aibles.auth.utils.service;

import org.aibles.auth.utils.entity.Account;
import org.aibles.auth.utils.entity.User;

import java.util.Optional;


public interface UserService {
    void registerStudent(User student, Account account, String confirmPassword);
    void registerLecturer(User lecturer, Account account, String confirmPassword);
}

