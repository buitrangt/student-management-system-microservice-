package org.aibles.exception;

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
    public static final ResponseStatus COURSE_AVAILABILITY_NOT_FOUND =
            new ResponseStatus("COURSE_AVAILABILITY_NOT_FOUND", "Course availability not found", HttpStatus.NOT_FOUND);

    public static final ResponseStatus NO_AVAILABLE_SLOTS =
            new ResponseStatus("NO_AVAILABLE_SLOTS", "No available slots for the course", HttpStatus.BAD_REQUEST);

    public static final ResponseStatus SLOTS_EXCEED_TOTAL =
            new ResponseStatus("SLOTS_EXCEED_TOTAL", "Cannot increase slots beyond total slots for the course", HttpStatus.BAD_REQUEST);

    public static final ResponseStatus AVAILABILITY_ALREADY_EXISTS =
            new ResponseStatus("AVAILABILITY_ALREADY_EXISTS", "Availability already exists for the course", HttpStatus.BAD_REQUEST);
}

