package org.aibles.classservice.exception;

import org.springframework.http.HttpStatus;

public class InstructorCode {
    public static final ResponseStatus INSTRUCTOR_CHANGE_PASSWORD_NULL =
            new ResponseStatus("INSTRUCTOR_CHANGE_PASSWORD_NULL", "Object is null", HttpStatus.BAD_REQUEST);

    public static final ResponseStatus CLASS_NOT_FOUND =
            new ResponseStatus("CLASS_NOT_FOUND", "Class not found", HttpStatus.NOT_FOUND);

    public static final ResponseStatus DEPARTMENT_NOT_FOUND =
            new ResponseStatus("DEPARTMENT_NOT_FOUND", "Department not found", HttpStatus.NOT_FOUND);

    public static final ResponseStatus INVALID_REQUEST =
            new ResponseStatus("INVALID_REQUEST", "Invalid request", HttpStatus.BAD_REQUEST);

    public static final ResponseStatus COURSE_NOT_FOUND =
            new ResponseStatus("COURSE_NOT_FOUND", "Course not found", HttpStatus.NOT_FOUND);

    public static final ResponseStatus LECTURER_NOT_FOUND =
            new ResponseStatus("LECTURER_NOT_FOUND", "Lecturer not found", HttpStatus.NOT_FOUND);

    public static final ResponseStatus SUBJECT_NOT_FOUND =
            new ResponseStatus("SUBJECT_NOT_FOUND", "Subject not found", HttpStatus.NOT_FOUND);


    public static final ResponseStatus GRADE_NOT_FOUND =
            new ResponseStatus("GRADE_NOT_FOUND", "Grade not found", HttpStatus.NOT_FOUND);

    public static final ResponseStatus STUDENT_NOT_FOUND =
            new ResponseStatus("STUDENT_NOT_FOUND", "Student not found", HttpStatus.NOT_FOUND);

    public static final ResponseStatus STUDENT_COURSE_NOT_FOUND =
            new ResponseStatus("STUDENT_COURSE_NOT_FOUND", "Student course not found", HttpStatus.NOT_FOUND);


    public static final ResponseStatus SUBJECT_LECTURER_NOT_FOUND =
            new ResponseStatus("SUBJECT_LECTURER_NOT_FOUND", "Subject Lecturer not found", HttpStatus.NOT_FOUND);


    public static final ResponseStatus ATTENDANCE_NOT_FOUND =
            new ResponseStatus("ATTENDANCE_NOT_FOUND", "Attendance not found", HttpStatus.NOT_FOUND);

    public static final ResponseStatus INTERNAL_SERVER_ERROR =
            new ResponseStatus("INTERNAL_SERVER_ERROR", "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
}