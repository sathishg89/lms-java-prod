package com.lms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lms.dto.AllCoursesDto;
import com.lms.entity.Courses;

public interface CoursesRepo extends JpaRepository<Courses, Integer> {

	List<Courses> findBycoursename(String courseName);

	@Query("SELECT c FROM Courses c WHERE c.coursename = :courseName AND c.coursetrainer = :courseTrainer")
	List<Courses> findBycoursenameAndcoursetrainer(String courseName, String courseTrainer);

	boolean existsBycoursename(String courseName);

	@Query("select  new com.lms.dto.AllCoursesDto(c.coursename ,c.coursetrainer) from Courses c")
	List<AllCoursesDto> getOnlyCourses();

}
