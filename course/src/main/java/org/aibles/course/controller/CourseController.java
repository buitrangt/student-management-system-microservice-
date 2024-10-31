package org.aibles.course.controller;



import org.aibles.course.dto.BaseResponse;
import org.aibles.course.dto.CourseListResponse;
import org.aibles.course.dto.CourseRequestDTO;
import org.aibles.course.dto.CourseResponseDTO;
import org.aibles.course.service.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController extends BaseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<BaseResponse<CourseResponseDTO>> create(@RequestBody CourseRequestDTO courseRequestDTO) {
        CourseResponseDTO courseResponseDTO = courseService.create(courseRequestDTO);
        return successResponse(courseResponseDTO, HttpStatus.CREATED);

    }

    @GetMapping("/courses")
    public ResponseEntity<BaseResponse<CourseListResponse>> getAll() {
        List<CourseResponseDTO> courses = courseService.getAll();

        CourseListResponse courseListResponse = new CourseListResponse(courses);

        BaseResponse<CourseListResponse> response = BaseResponse.success(courseListResponse);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<CourseResponseDTO>> getById(@PathVariable int id) {
        CourseResponseDTO courseResponseDTO = courseService.getById(id);
       return successResponse(courseResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<CourseResponseDTO>> update(
            @PathVariable int id,
            @RequestBody CourseRequestDTO courseRequestDTO
    ) {
        CourseResponseDTO courseResponseDTO = courseService.update(id, courseRequestDTO);
        return successResponse(courseResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> delete(@PathVariable int id) {
        courseService.delete(id);
       return successResponseNoContent();
    }
}

