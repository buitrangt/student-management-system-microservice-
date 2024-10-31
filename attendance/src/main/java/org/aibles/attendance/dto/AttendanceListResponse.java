package org.aibles.attendance.dto;

import lombok.Data;

import java.util.List;

@Data
public class AttendanceListResponse {

    private List<AttendanceResponseDTO> attendanceRecords;

    public AttendanceListResponse(List<AttendanceResponseDTO> attendanceRecords) {
        this.attendanceRecords = attendanceRecords;
    }


}

