package org.aibles.lecturer.controller;



import org.aibles.lecturer.dto.BaseResponse;
import org.aibles.lecturer.dto.LecturerListResponse;
import org.aibles.lecturer.dto.LecturerRequestDTO;
import org.aibles.lecturer.dto.LecturerResponseDTO;
import org.aibles.lecturer.service.LecturerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/lecturers")
public class LecturerController extends BaseController {

    private final LecturerService lecturerService;

    public LecturerController(LecturerService lecturerService) {
        this.lecturerService = lecturerService;
    }

    @PostMapping
    public ResponseEntity<BaseResponse<LecturerResponseDTO>> create(@RequestBody LecturerRequestDTO lecturerRequestDTO) {
        LecturerResponseDTO lecturerResponseDTO = lecturerService.create(lecturerRequestDTO);
        return successResponse(lecturerResponseDTO,HttpStatus.CREATED);
    }

    @GetMapping("/lecturers")
    public ResponseEntity<BaseResponse<LecturerListResponse>> getAll() {
        List<LecturerResponseDTO> lecturers = lecturerService.getAll();

        LecturerListResponse lecturerListResponse = new LecturerListResponse(lecturers);

        BaseResponse<LecturerListResponse> response = BaseResponse.success(lecturerListResponse);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<LecturerResponseDTO>> getById(@PathVariable int id) {
        LecturerResponseDTO lecturerResponseDTO = lecturerService.getById(id);
        return successResponse(lecturerResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<LecturerResponseDTO>> update(@PathVariable int id, @RequestBody LecturerRequestDTO lecturerRequestDTO) {
        LecturerResponseDTO lecturerResponseDTO = lecturerService.update(id, lecturerRequestDTO);
       return successResponse(lecturerResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> delete(@PathVariable int id) {
        lecturerService.delete(id);
       return successResponseNoContent();
    }
}

