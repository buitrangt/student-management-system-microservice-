package org.aibles.subject.service;


import org.aibles.subject.dto.SubjectRequestDTO;
import org.aibles.subject.dto.SubjectResponseDTO;

import java.util.List;

public interface SubjectService {
    SubjectResponseDTO create(SubjectRequestDTO subjectRequestDTO);
    List<SubjectResponseDTO> getAll();
    SubjectResponseDTO getById(int subjectId);
    SubjectResponseDTO update(int subjectId, SubjectRequestDTO subjectRequestDTO);
    void delete(int subjectId);
}
