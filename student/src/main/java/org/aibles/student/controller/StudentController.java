package org.aibles.student.controller;


import org.aibles.student.dto.BaseResponse;
import org.aibles.student.dto.StudentListResponse;
import org.aibles.student.dto.StudentRequest;
import org.aibles.student.dto.StudentResponse;
import org.aibles.student.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController extends BaseController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<BaseResponse<StudentResponse>> create(@RequestBody StudentRequest studentRequest) {
        StudentResponse studentResponse = studentService.create(studentRequest);
        return successResponse(studentResponse, HttpStatus.CREATED);
    }
    @GetMapping("/students")
    public ResponseEntity<BaseResponse<StudentListResponse>> getAllStudents() {
        List<StudentResponse> students = studentService.getAll();

        StudentListResponse studentListResponse = new StudentListResponse(students, students.size());

        BaseResponse<StudentListResponse> response = BaseResponse.success(studentListResponse);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<StudentResponse>> getById(@PathVariable int id) {
        StudentResponse studentResponse = studentService.getById(id);
        return successResponse(studentResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<StudentResponse>> update(@PathVariable int id, @RequestBody StudentRequest studentRequest) {
        StudentResponse studentResponse = studentService.update(id, studentRequest);
        return successResponse(studentResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> delete(@PathVariable int id) {
        studentService.delete(id);
        return successResponseNoContent();
    }
}