package org.aibles.classservice.controller;


import org.aibles.classservice.dto.BaseResponse;
import org.aibles.classservice.dto.ClassListResponse;
import org.aibles.classservice.dto.ClassRequestDTO;
import org.aibles.classservice.dto.ClassResponseDTO;
import org.aibles.classservice.service.ClassService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/classes")
public class ClassController extends BaseController {

    private final ClassService classService;

    public ClassController(ClassService classService) {
        this.classService = classService;
    }

    @PostMapping
    public ResponseEntity<BaseResponse<ClassResponseDTO>> create(@RequestBody ClassRequestDTO classRequestDTO) {
        ClassResponseDTO classResponseDTO = classService.create(classRequestDTO);
        return successResponse(classResponseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/classes")
    public ResponseEntity<BaseResponse<ClassListResponse>> getAll() {
        List<ClassResponseDTO> classes = classService.getAll();

        ClassListResponse classListResponse = new ClassListResponse(classes);

        BaseResponse<ClassListResponse> response = BaseResponse.success(classListResponse);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<ClassResponseDTO>> getById(@PathVariable int id) {
        ClassResponseDTO classResponseDTO = classService.getById(id);
        return successResponse(classResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<ClassResponseDTO>> update(
            @PathVariable int id,
            @RequestBody ClassRequestDTO classRequestDTO
    ) {
        ClassResponseDTO classResponseDTO = classService.update(id, classRequestDTO);
        return successResponse(classResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> delete(@PathVariable int id) {
        classService.delete(id);
        return successResponseNoContent();
    }

    @GetMapping("/search")
    public ResponseEntity<BaseResponse<List<ClassResponseDTO>>> search(@RequestParam String className) {
        List<ClassResponseDTO> classes = classService.searchByName(className);
        return successResponse(classes);
    }
}
