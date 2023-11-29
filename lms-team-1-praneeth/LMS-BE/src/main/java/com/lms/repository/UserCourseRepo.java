package com.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.entity.CourseUser;

public interface UserCourseRepo extends JpaRepository<CourseUser, Integer> {

	CourseUser findByusername(String name);

	boolean existsByusername(String name);

}
