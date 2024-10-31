package org.aibles.student.service;

import org.aibles.student.dto.StudentRequest;
import org.aibles.student.dto.StudentResponse;

import java.util.List;

public interface StudentService {

    StudentResponse create(StudentRequest studentRequestDTO);

    List<StudentResponse> getAll();

    StudentResponse getById(int studentId);

    StudentResponse update(int studentId, StudentRequest studentRequestDTO);

    void delete(int studentId);
}
