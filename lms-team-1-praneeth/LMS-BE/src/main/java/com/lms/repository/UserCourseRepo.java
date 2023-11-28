package com.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.entity.UserCourse;

public interface UserCourseRepo extends JpaRepository<UserCourse, Integer> {

	UserCourse findByusername(String name);

	boolean existsByusername(String name);

}
