package org.aibles.attendance.controller;


import lombok.extern.slf4j.Slf4j;

import org.aibles.attendance.dto.AttendanceListResponse;
import org.aibles.attendance.dto.AttendanceRequestDTO;
import org.aibles.attendance.dto.AttendanceResponseDTO;
import org.aibles.attendance.dto.BaseResponse;
import org.aibles.attendance.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/attendances")
public class AttendanceController extends BaseController {

    private final AttendanceService attendanceService;

    @Autowired
    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody AttendanceRequestDTO attendanceRequestDTO) {
        log.info("(create) - attendanceRequestDTO: {}", attendanceRequestDTO);
        AttendanceResponseDTO responseDTO = attendanceService.create(attendanceRequestDTO);
        return successResponse(responseDTO);
    }

    @PutMapping("/{attendanceId}")
    public ResponseEntity<?> update(
            @PathVariable Long attendanceId,
            @RequestBody AttendanceRequestDTO attendanceRequestDTO) {
        log.info("(update) - attendanceId: {}, attendanceRequestDTO: {}", attendanceId, attendanceRequestDTO);
        AttendanceResponseDTO responseDTO = attendanceService.update(attendanceId, attendanceRequestDTO);
        return successResponse(responseDTO);
    }

    @DeleteMapping("/{attendanceId}")
    public ResponseEntity<?> delete(@PathVariable Long attendanceId) {
        log.info("(delete) - attendanceId: {}", attendanceId);
        attendanceService.delete(attendanceId);
        return successResponseNoContent();
    }

    @GetMapping("/{attendanceId}")
    public ResponseEntity<?> getById(@PathVariable Long attendanceId) {
        log.info("(getById) - attendanceId: {}", attendanceId);
        AttendanceResponseDTO responseDTO = attendanceService.getById(attendanceId);
        return successResponse(responseDTO);
    }

    @GetMapping("/attendances")
    public ResponseEntity<BaseResponse<AttendanceListResponse>> getAll() {
        log.info("(getAll) - retrieving all attendance records");

        List<AttendanceResponseDTO> responseDTOList = attendanceService.getAll();

        AttendanceListResponse attendanceListResponse = new AttendanceListResponse(responseDTOList);

        BaseResponse<AttendanceListResponse> response = BaseResponse.success(attendanceListResponse);

        return ResponseEntity.ok(response);
    }


}

