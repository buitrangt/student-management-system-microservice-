package org.aibles.grade.service;



import org.aibles.grade.dto.GradeRequestDTO;
import org.aibles.grade.dto.GradeResponseDTO;

import java.util.List;

public interface GradeService {

    GradeResponseDTO create(GradeRequestDTO gradeRequestDTO);

    List<GradeResponseDTO> getAll();

    GradeResponseDTO getById(Integer studentId, Integer courseId);

    GradeResponseDTO update(Integer studentId, Integer courseId, GradeRequestDTO gradeRequestDTO);

    void delete(Integer studentId, Integer courseId);
}

