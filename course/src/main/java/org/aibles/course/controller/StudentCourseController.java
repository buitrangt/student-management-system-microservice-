package org.aibles.course.controller;



import com.fasterxml.jackson.core.JsonProcessingException;
import org.aibles.course.dto.BaseResponse;
import org.aibles.course.dto.StudentCourseListResponse;
import org.aibles.course.dto.StudentCourseRequestDTO;
import org.aibles.course.dto.StudentCourseResponseDTO;
import org.aibles.course.service.StudentCourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/studentcourses")
public class StudentCourseController extends BaseController{

    private final StudentCourseService studentCourseService;

    public StudentCourseController(StudentCourseService studentCourseService) {
        this.studentCourseService = studentCourseService;
    }

    @PostMapping
    public ResponseEntity<BaseResponse<StudentCourseResponseDTO>> create(@RequestBody StudentCourseRequestDTO studentCourseRequestDTO) throws JsonProcessingException {
        StudentCourseResponseDTO studentCourseResponseDTO = studentCourseService.create(studentCourseRequestDTO);
       return successResponse(studentCourseResponseDTO,HttpStatus.CREATED);
    }

    @GetMapping("/student-courses")
    public ResponseEntity<BaseResponse<StudentCourseListResponse>> getAll() {
        List<StudentCourseResponseDTO> studentCourses = studentCourseService.getAll();

        StudentCourseListResponse studentCourseListResponse = new StudentCourseListResponse(studentCourses);

        BaseResponse<StudentCourseListResponse> response = BaseResponse.success(studentCourseListResponse);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @GetMapping("/students/{studentId}/courses/{courseId}")
    public ResponseEntity<BaseResponse<StudentCourseResponseDTO>> getById(
            @PathVariable int studentId,
            @PathVariable int courseId
    ) {
        StudentCourseResponseDTO studentCourseResponseDTO = studentCourseService.getById(studentId, courseId);
       return successResponse(studentCourseResponseDTO);
    }

    @DeleteMapping("/students/{studentId}/courses/{courseId}")
    public ResponseEntity<BaseResponse<Void>> delete(
            @PathVariable int studentId,
            @PathVariable int courseId
    ) {
        studentCourseService.delete(studentId, courseId);
        return successResponseNoContent();
    }
}

