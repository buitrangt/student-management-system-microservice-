package org.aibles.attendance.service;



import org.aibles.attendance.dto.AttendanceRequestDTO;
import org.aibles.attendance.dto.AttendanceResponseDTO;

import java.util.List;

public interface AttendanceService {
    AttendanceResponseDTO create(AttendanceRequestDTO attendanceRequestDTO);
    AttendanceResponseDTO update(Long attendanceId, AttendanceRequestDTO attendanceRequestDTO);
    void delete(Long attendanceId);
    AttendanceResponseDTO getById(Long attendanceId);
    List<AttendanceResponseDTO> getAll();
}

