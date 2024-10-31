package org.aibles.subject.controller;


import org.aibles.subject.dto.BaseResponse;
import org.aibles.subject.dto.SubjectListResponse;
import org.aibles.subject.dto.SubjectRequestDTO;
import org.aibles.subject.dto.SubjectResponseDTO;
import org.aibles.subject.service.SubjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subjects")
public class SubjectController extends BaseController {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @PostMapping
    public ResponseEntity<BaseResponse<SubjectResponseDTO>> create(@RequestBody SubjectRequestDTO subjectRequestDTO) {
        SubjectResponseDTO subjectResponseDTO = subjectService.create(subjectRequestDTO);
       return successResponse(subjectResponseDTO,HttpStatus.CREATED);
    }

    @GetMapping("/subjects")
    public ResponseEntity<BaseResponse<SubjectListResponse>> getAll() {
        List<SubjectResponseDTO> subjects = subjectService.getAll();

        SubjectListResponse subjectListResponse = new SubjectListResponse(subjects);

        BaseResponse<SubjectListResponse> response = BaseResponse.success(subjectListResponse);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<SubjectResponseDTO>> getById(@PathVariable int id) {
        SubjectResponseDTO subjectResponseDTO = subjectService.getById(id);
        return successResponse(subjectResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<SubjectResponseDTO>> update(
            @PathVariable int id,
            @RequestBody SubjectRequestDTO subjectRequestDTO
    ) {
        SubjectResponseDTO subjectResponseDTO = subjectService.update(id, subjectRequestDTO);
        return successResponse(subjectResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> delete(@PathVariable int id) {
        subjectService.delete(id);
        return successResponseNoContent();
    }
}
