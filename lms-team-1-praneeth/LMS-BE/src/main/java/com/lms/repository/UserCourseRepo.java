package com.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.entity.CourseUsers;

public interface UserCourseRepo extends JpaRepository<CourseUsers, Integer> {

	CourseUsers findByusername(String name);

	boolean existsByusername(String name);

}
