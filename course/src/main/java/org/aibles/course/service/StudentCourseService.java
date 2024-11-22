package org.aibles.course.service;



import com.fasterxml.jackson.core.JsonProcessingException;
import org.aibles.course.dto.StudentCourseRequestDTO;
import org.aibles.course.dto.StudentCourseResponseDTO;

import java.util.List;

public interface StudentCourseService {

    StudentCourseResponseDTO create(StudentCourseRequestDTO studentCourseRequestDTO) throws JsonProcessingException;

    List<StudentCourseResponseDTO> getAll();

    StudentCourseResponseDTO getById(int studentId, int courseId);

    void delete(int studentId, int courseId);
}

