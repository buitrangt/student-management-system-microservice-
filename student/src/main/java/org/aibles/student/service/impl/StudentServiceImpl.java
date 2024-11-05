package org.aibles.student.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.aibles.student.dto.StudentRequest;
import org.aibles.student.dto.StudentResponse;
import org.aibles.student.entity.Student;
import org.aibles.student.exception.BusinessException;
import org.aibles.student.exception.InstructorCode;
import org.aibles.student.repository.StudentRepository;
import org.aibles.student.service.StudentService;
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
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final RestTemplate restTemplate;
    private final String classServiceUrl;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository, RestTemplate restTemplate,
                              @Value("${class.service.url}") String classServiceUrl) {
        this.studentRepository = studentRepository;
        this.restTemplate = restTemplate;
        this.classServiceUrl = classServiceUrl;
    }

    @Override
    @Transactional
    public StudentResponse create(StudentRequest studentRequestDTO) {
        log.info("(createStudent) Start - studentRequestDTO: {}", studentRequestDTO);

        checkClassExists(studentRequestDTO.getClassId());

        Student student = new Student();
        student.setUserId(studentRequestDTO.getUserId());
        student.setFullName(studentRequestDTO.getFullName());
        student.setDateOfBirth(studentRequestDTO.getDateOfBirth());
        student.setGender(Student.Gender.valueOf(studentRequestDTO.getGender()));
        student.setAddress(studentRequestDTO.getAddress());
        student.setEmail(studentRequestDTO.getEmail());
        student.setPhoneNumber(studentRequestDTO.getPhoneNumber());
        student.setClassId(studentRequestDTO.getClassId());

        Student savedStudent = studentRepository.save(student);
        log.info("(createStudent) Successfully created student - studentId: {}", savedStudent.getStudentId());

        return mapToStudentResponseDTO(savedStudent);
    }

    private void checkClassExists(Integer classId) {
        String classUrl = classServiceUrl + "/" + classId;
        try {
            restTemplate.getForObject(classUrl, Object.class);
            log.info("(checkClassExists) Class found - classId: {}", classId);
        } catch (HttpClientErrorException.NotFound e) {
            log.error("(checkClassExists) Class not found - classId: {}", classId);
            throw new BusinessException(InstructorCode.CLASS_NOT_FOUND);
        } catch (Exception e) {
            log.error("(checkClassExists) An error occurred while verifying the class - classId: {}", classId, e);
            throw new BusinessException(InstructorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponse> getAll() {
        log.info("(getAllStudents) Start - retrieving all students");
        List<Student> students = studentRepository.findAll();
        log.info("(getAllStudents) Successfully retrieved all students - total: {}", students.size());
        return students.stream().map(this::mapToStudentResponseDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponse getById(int studentId) {
        log.info("(getStudentById) Start - studentId: {}", studentId);
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> {
                    log.error("(getStudentById) Student not found - studentId: {}", studentId);
                    return new BusinessException(InstructorCode.STUDENT_NOT_FOUND);
                });
        log.info("(getStudentById) Successfully retrieved student - studentId: {}", studentId);
        return mapToStudentResponseDTO(student);
    }

    @Override
    @Transactional
    public StudentResponse update(int studentId, StudentRequest studentRequestDTO) {
        log.info("(updateStudent) Start - studentId: {}", studentId);

        checkClassExists(studentRequestDTO.getClassId());

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> {
                    log.error("(updateStudent) Student not found - studentId: {}", studentId);
                    return new BusinessException(InstructorCode.STUDENT_NOT_FOUND);
                });

        student.setUserId(studentRequestDTO.getUserId());
        student.setFullName(studentRequestDTO.getFullName());
        student.setDateOfBirth(studentRequestDTO.getDateOfBirth());
        student.setGender(Student.Gender.valueOf(studentRequestDTO.getGender()));
        student.setAddress(studentRequestDTO.getAddress());
        student.setEmail(studentRequestDTO.getEmail());
        student.setPhoneNumber(studentRequestDTO.getPhoneNumber());
        student.setClassId(studentRequestDTO.getClassId());

        Student updatedStudent = studentRepository.save(student);
        log.info("(updateStudent) Successfully updated student - studentId: {}", updatedStudent.getStudentId());
        return mapToStudentResponseDTO(updatedStudent);
    }

    @Override
    @Transactional
    public void delete(int studentId) {
        log.info("(deleteStudent) Start - studentId: {}", studentId);
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> {
                    log.error("(deleteStudent) Student not found - studentId: {}", studentId);
                    return new BusinessException(InstructorCode.STUDENT_NOT_FOUND);
                });
        studentRepository.delete(student);
        log.info("(deleteStudent) Successfully deleted student - studentId: {}", studentId);
    }

    private StudentResponse mapToStudentResponseDTO(Student student) {
        StudentResponse studentResponseDTO = new StudentResponse();
        studentResponseDTO.setStudentId(student.getStudentId());
        studentResponseDTO.setUserId(student.getUserId());
        studentResponseDTO.setFullName(student.getFullName());
        studentResponseDTO.setDateOfBirth(student.getDateOfBirth());
        studentResponseDTO.setGender(student.getGender().name());
        studentResponseDTO.setAddress(student.getAddress());
        studentResponseDTO.setEmail(student.getEmail());
        studentResponseDTO.setPhoneNumber(student.getPhoneNumber());
        studentResponseDTO.setClassId(student.getClassId());
        return studentResponseDTO;
    }
}
