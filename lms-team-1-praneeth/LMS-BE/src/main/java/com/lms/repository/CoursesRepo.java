package com.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.entity.Courses;

public interface CoursesRepo extends JpaRepository<Courses, Integer> {

	Courses findBycoursename(String coursename);

	boolean existsBycoursename(String coursename);

}
