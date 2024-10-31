package org.aibles.course.service;



import org.aibles.course.dto.CourseRequestDTO;
import org.aibles.course.dto.CourseResponseDTO;

import java.util.List;

public interface CourseService {

    CourseResponseDTO create(CourseRequestDTO courseRequestDTO);

    List<CourseResponseDTO> getAll();

    CourseResponseDTO getById(int courseId);

    CourseResponseDTO update(int courseId, CourseRequestDTO courseRequestDTO);

    void delete(int courseId);
}

