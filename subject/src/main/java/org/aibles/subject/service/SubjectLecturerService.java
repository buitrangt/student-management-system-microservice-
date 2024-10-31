package org.aibles.subject.service;



import org.aibles.subject.dto.SubjectLecturerRequestDTO;
import org.aibles.subject.dto.SubjectLecturerResponseDTO;
import org.aibles.subject.entity.SubjectLecturerId;

import java.util.List;

public interface SubjectLecturerService {
    SubjectLecturerResponseDTO create(SubjectLecturerRequestDTO subjectLecturerRequestDTO);
    List<SubjectLecturerResponseDTO> getAll();
    SubjectLecturerResponseDTO getById(SubjectLecturerId id);
    SubjectLecturerResponseDTO update(SubjectLecturerId id, SubjectLecturerRequestDTO subjectLecturerRequestDTO);
    void delete(SubjectLecturerId id);
}
