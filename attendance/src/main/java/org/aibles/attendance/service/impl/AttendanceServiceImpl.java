package org.aibles.attendance.service.impl;

import lombok.extern.slf4j.Slf4j;

import org.aibles.attendance.dto.AttendanceRequestDTO;
import org.aibles.attendance.dto.AttendanceResponseDTO;
import org.aibles.attendance.entity.Attendance;
import org.aibles.attendance.entity.AttendanceStatus;
import org.aibles.attendance.exception.BusinessException;
import org.aibles.attendance.exception.InstructorCode;
import org.aibles.attendance.exception.ResponseStatus;
import org.aibles.attendance.repository.AttendanceRepository;
import org.aibles.attendance.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;



@Slf4j
@Service
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public AttendanceServiceImpl(AttendanceRepository attendanceRepository, RestTemplate restTemplate) {
        this.attendanceRepository = attendanceRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    @Transactional
    public AttendanceResponseDTO create(AttendanceRequestDTO attendanceRequestDTO) {
        log.info("(create) Start - attendanceRequestDTO: {}", attendanceRequestDTO);

        validateAttendanceRequest(attendanceRequestDTO);

        // Kiểm tra sự tồn tại của course và student thông qua RestTemplate
        checkExistence("http://COURSE-SERVICE/api/v1/courses/" + attendanceRequestDTO.getCourseId(), InstructorCode.COURSE_NOT_FOUND);
        checkExistence("http://STUDENT-SERVICE/api/v1/students/" + attendanceRequestDTO.getStudentId(), InstructorCode.STUDENT_NOT_FOUND);

        Attendance attendance = new Attendance();
        attendance.setAttendanceDate(attendanceRequestDTO.getAttendanceDate());
        attendance.setStatus(AttendanceStatus.valueOf(attendanceRequestDTO.getStatus()));
        attendance.setCourseId(attendanceRequestDTO.getCourseId());
        attendance.setStudentId(attendanceRequestDTO.getStudentId());

        Attendance savedAttendance = attendanceRepository.save(attendance);
        log.info("(create) Successfully created attendance - attendanceId: {}", savedAttendance.getAttendanceId());

        return mapToResponseDTO(savedAttendance);
    }

    private void checkExistence(String url, ResponseStatus errorCode) {
        try {
            restTemplate.getForObject(url, Object.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new BusinessException(errorCode);
        } catch (Exception e) {
            throw new BusinessException(InstructorCode.INTERNAL_SERVER_ERROR);
        }
    }



    @Override
    @Transactional
    public AttendanceResponseDTO update(Long attendanceId, AttendanceRequestDTO attendanceRequestDTO) {
        log.info("(update) Start - attendanceId: {}", attendanceId);

        validateAttendanceRequest(attendanceRequestDTO);
        // Kiểm tra sự tồn tại của course và student thông qua RestTemplate
        checkExistence("http://COURSE-SERVICE/api/v1/courses/" + attendanceRequestDTO.getCourseId(), InstructorCode.COURSE_NOT_FOUND);
        checkExistence("http://STUDENT-SERVICE/api/v1/students/" + attendanceRequestDTO.getStudentId(), InstructorCode.STUDENT_NOT_FOUND);


        Attendance attendance = checkAttendanceExists(attendanceId);
        attendance.setAttendanceDate(attendanceRequestDTO.getAttendanceDate());
        attendance.setStatus(AttendanceStatus.valueOf(attendanceRequestDTO.getStatus()));
        attendance.setCourseId(attendanceRequestDTO.getCourseId());
        attendance.setStudentId(attendanceRequestDTO.getStudentId());

        Attendance updatedAttendance = attendanceRepository.save(attendance);
        log.info("(update) Successfully updated attendance - attendanceId: {}", updatedAttendance.getAttendanceId());

        return mapToResponseDTO(updatedAttendance);
    }

    @Override
    @Transactional
    public void delete(Long attendanceId) {
        log.info("(delete) Start - attendanceId: {}", attendanceId);
        Attendance attendance = checkAttendanceExists(attendanceId);
        attendanceRepository.delete(attendance);
        log.info("(delete) Successfully deleted attendance - attendanceId: {}", attendanceId);
    }

    @Override
    @Transactional(readOnly = true)
    public AttendanceResponseDTO getById(Long attendanceId) {
        log.info("(getById) Start - attendanceId: {}", attendanceId);
        Attendance attendance = checkAttendanceExists(attendanceId);
        log.info("(getById) Successfully retrieved attendance - attendanceId: {}", attendanceId);
        return mapToResponseDTO(attendance);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceResponseDTO> getAll() {
        log.info("(getAll) Start - retrieving all attendance records");
        List<Attendance> attendances = attendanceRepository.findAll();
        return attendances.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    private Attendance checkAttendanceExists(Long attendanceId) {
        return attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new BusinessException(InstructorCode.ATTENDANCE_NOT_FOUND));
    }

    private void validateAttendanceRequest(AttendanceRequestDTO attendanceRequestDTO) {
        if (attendanceRequestDTO == null) {
            throw new BusinessException(InstructorCode.INVALID_REQUEST);
        }
        if (attendanceRequestDTO.getAttendanceDate() == null) {
            throw new BusinessException(InstructorCode.INVALID_REQUEST);
        }
        if (attendanceRequestDTO.getStatus() == null || attendanceRequestDTO.getStatus().trim().isEmpty()) {
            throw new BusinessException(InstructorCode.INVALID_REQUEST);
        }
        if (attendanceRequestDTO.getCourseId() == null) {
            throw new BusinessException(InstructorCode.INVALID_REQUEST);
        }
        if (attendanceRequestDTO.getStudentId() == null) {
            throw new BusinessException(InstructorCode.INVALID_REQUEST);
        }
    }

    private AttendanceResponseDTO mapToResponseDTO(Attendance attendance) {
        AttendanceResponseDTO responseDTO = new AttendanceResponseDTO();
        responseDTO.setAttendanceId(attendance.getAttendanceId());
        responseDTO.setAttendanceDate(attendance.getAttendanceDate());
        responseDTO.setStatus(attendance.getStatus().name());
        responseDTO.setCourseId(attendance.getCourseId());
        responseDTO.setStudentId(attendance.getStudentId());
        return responseDTO;
    }
}

