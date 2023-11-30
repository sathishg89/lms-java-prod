package com.lms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.entity.Courses;

public interface CoursesRepo extends JpaRepository<Courses, Integer> {

	List<Courses> findBycoursename(String coursename);

	boolean existsBycoursename(String coursename);

}
