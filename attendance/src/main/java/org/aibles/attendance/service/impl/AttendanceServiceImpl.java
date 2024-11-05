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
import org.springframework.beans.factory.annotation.Value;
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
    private final String courseServiceUrl;
    private final String studentServiceUrl;

    @Autowired
    public AttendanceServiceImpl(
            AttendanceRepository attendanceRepository,
            RestTemplate restTemplate,
            @Value("${course.service.url}") String courseServiceUrl,
            @Value("${student.service.url}") String studentServiceUrl) {
        this.attendanceRepository = attendanceRepository;
        this.restTemplate = restTemplate;
        this.courseServiceUrl = courseServiceUrl;
        this.studentServiceUrl = studentServiceUrl;
    }

    @Override
    @Transactional
    public AttendanceResponseDTO create(AttendanceRequestDTO attendanceRequestDTO) {
        log.info("(create) Start - attendanceRequestDTO: {}", attendanceRequestDTO);

        validateAttendanceRequest(attendanceRequestDTO);

        checkExistence(courseServiceUrl + "/" + attendanceRequestDTO.getCourseId(), InstructorCode.COURSE_NOT_FOUND);
        checkExistence(studentServiceUrl + "/" + attendanceRequestDTO.getStudentId(), InstructorCode.STUDENT_NOT_FOUND);

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
            log.info("(checkExistence) Verified existence for URL: {}", url);
        } catch (HttpClientErrorException.NotFound e) {
            log.error("(checkExistence) Resource not found for URL: {}", url);
            throw new BusinessException(errorCode);
        } catch (Exception e) {
            log.error("(checkExistence) Error verifying existence for URL: {}", url, e);
            throw new BusinessException(InstructorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional
    public AttendanceResponseDTO update(Long attendanceId, AttendanceRequestDTO attendanceRequestDTO) {
        log.info("(update) Start - attendanceId: {}", attendanceId);

        validateAttendanceRequest(attendanceRequestDTO);
        checkExistence(courseServiceUrl + "/" + attendanceRequestDTO.getCourseId(), InstructorCode.COURSE_NOT_FOUND);
        checkExistence(studentServiceUrl + "/" + attendanceRequestDTO.getStudentId(), InstructorCode.STUDENT_NOT_FOUND);

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
        if (attendanceRequestDTO == null || attendanceRequestDTO.getAttendanceDate() == null ||
                attendanceRequestDTO.getStatus() == null || attendanceRequestDTO.getStatus().trim().isEmpty() ||
                attendanceRequestDTO.getCourseId() == null || attendanceRequestDTO.getStudentId() == null) {
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
