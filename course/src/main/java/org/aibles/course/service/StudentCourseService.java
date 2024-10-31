package org.aibles.course.service;



import org.aibles.course.dto.StudentCourseRequestDTO;
import org.aibles.course.dto.StudentCourseResponseDTO;

import java.util.List;

public interface StudentCourseService {

    StudentCourseResponseDTO create(StudentCourseRequestDTO studentCourseRequestDTO);

    List<StudentCourseResponseDTO> getAll();

    StudentCourseResponseDTO getById(int studentId, int courseId);

    void delete(int studentId, int courseId);
}

