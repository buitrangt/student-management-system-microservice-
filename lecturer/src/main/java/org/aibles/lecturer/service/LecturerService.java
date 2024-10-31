package org.aibles.lecturer.service;



import org.aibles.lecturer.dto.LecturerRequestDTO;
import org.aibles.lecturer.dto.LecturerResponseDTO;

import java.util.List;

public interface LecturerService {

    LecturerResponseDTO create(LecturerRequestDTO lecturerRequestDTO);

    List<LecturerResponseDTO> getAll();

    LecturerResponseDTO getById(int lecturerId);

    LecturerResponseDTO update(int lecturerId, LecturerRequestDTO lecturerRequestDTO);

    void delete(int lecturerId);
}

