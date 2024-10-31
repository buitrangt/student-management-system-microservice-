package org.aibles.classservice.service;



import org.aibles.classservice.dto.ClassRequestDTO;
import org.aibles.classservice.dto.ClassResponseDTO;

import java.util.List;

public interface ClassService {

    ClassResponseDTO create(ClassRequestDTO classRequestDTO);

    List<ClassResponseDTO> getAll();

    ClassResponseDTO getById(int classId);

    ClassResponseDTO update(int classId, ClassRequestDTO classRequestDTO);

    void delete(int classId);

    List<ClassResponseDTO> searchByName(String className);  // Thêm phương thức này
}



