package org.aibles.service;


import org.aibles.entity.CourseAvailability;

public interface CourseAvailabilityService {
    /**
     * Lấy thông tin số lượng chỗ của khóa học.
     * @param courseId ID của khóa học
     * @return CourseAvailability
     */
    CourseAvailability getAvailability(Integer courseId);

    /**
     * Giảm số lượng chỗ khi sinh viên đăng ký.
     * @param courseId ID của khóa học
     */
    void decreaseSlots(Integer courseId);

    /**
     * Tăng số lượng chỗ khi sinh viên hủy đăng ký.
     * @param courseId ID của khóa học
     */
    void increaseSlots(Integer courseId);

    /**
     * Tạo thông tin availability cho một khóa học mới.
     * @param courseAvailability Thông tin của khóa học
     * @return CourseAvailability đã được tạo
     */
    CourseAvailability createAvailability(CourseAvailability courseAvailability);
}

