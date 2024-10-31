package org.aibles.subject.controller;


import org.aibles.subject.dto.BaseResponse;
import org.aibles.subject.dto.SubjectLecturerListResponse;
import org.aibles.subject.dto.SubjectLecturerRequestDTO;
import org.aibles.subject.dto.SubjectLecturerResponseDTO;
import org.aibles.subject.entity.SubjectLecturerId;
import org.aibles.subject.service.SubjectLecturerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subjectlecturers")
public class SubjectLecturerController extends BaseController {

    private final SubjectLecturerService subjectLecturerService;

    public SubjectLecturerController(SubjectLecturerService subjectLecturerService) {
        this.subjectLecturerService = subjectLecturerService;
    }

    @PostMapping
    public ResponseEntity<BaseResponse<SubjectLecturerResponseDTO>> create(@RequestBody SubjectLecturerRequestDTO subjectLecturerRequestDTO) {
        SubjectLecturerResponseDTO subjectLecturerResponseDTO = subjectLecturerService.create(subjectLecturerRequestDTO);
        return successResponse(subjectLecturerResponseDTO,HttpStatus.CREATED);
    }

    @GetMapping("/subject-lecturers")
    public ResponseEntity<BaseResponse<SubjectLecturerListResponse>> getAll() {
        List<SubjectLecturerResponseDTO> subjectLecturers = subjectLecturerService.getAll();

        SubjectLecturerListResponse subjectLecturerListResponse = new SubjectLecturerListResponse(subjectLecturers);

        BaseResponse<SubjectLecturerListResponse> response = BaseResponse.success(subjectLecturerListResponse);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/{subjectId}/{lecturerId}")
    public ResponseEntity<BaseResponse<SubjectLecturerResponseDTO>> getById(@PathVariable Integer subjectId, @PathVariable Integer lecturerId) {
        SubjectLecturerId id = new SubjectLecturerId(subjectId, lecturerId);
        SubjectLecturerResponseDTO subjectLecturerResponseDTO = subjectLecturerService.getById(id);
        return successResponse(subjectLecturerResponseDTO);
    }

    @PutMapping("/{subjectId}/{lecturerId}")
    public ResponseEntity<BaseResponse<SubjectLecturerResponseDTO>> update(
            @PathVariable Integer subjectId,
            @PathVariable Integer lecturerId,
            @RequestBody SubjectLecturerRequestDTO subjectLecturerRequestDTO
    ) {
        SubjectLecturerId id = new SubjectLecturerId(subjectId, lecturerId);
        SubjectLecturerResponseDTO subjectLecturerResponseDTO = subjectLecturerService.update(id, subjectLecturerRequestDTO);
       return successResponse(subjectLecturerResponseDTO);
    }

    @DeleteMapping("/{subjectId}/{lecturerId}")
    public ResponseEntity<BaseResponse<Void>> delete(@PathVariable Integer subjectId, @PathVariable Integer lecturerId) {
        SubjectLecturerId id = new SubjectLecturerId(subjectId, lecturerId);
        subjectLecturerService.delete(id);
        return successResponseNoContent();
    }
}
