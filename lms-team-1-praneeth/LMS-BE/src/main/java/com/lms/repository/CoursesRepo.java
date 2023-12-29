package com.lms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lms.dto.CoursesListDto;
import com.lms.entity.CourseModules;
import com.lms.entity.Courses;

public interface CoursesRepo extends JpaRepository<Courses, Integer> {

	List<Courses> findBycourseName(String courseName);

	boolean existsBycourseName(String courseName);
	
	boolean existsBycourseNameAndCourseTrainer(String courseName,String courseTrainer);

	@Query("SELECT c FROM Courses c WHERE c.courseName = :courseName AND c.courseTrainer = :courseTrainer")
	List<Courses> findBycourseNameAndcourseTrainer(String courseName, String courseTrainer);

	@Query("select  new com.lms.dto.CoursesListDto(c.courseName ,c.courseTrainer) from Courses c")
	List<CoursesListDto> getOnlyCourses();

	@Query("SELECT cm FROM Courses c JOIN c.courseModule cm WHERE c.courseName = :coursenName and c.courseTrainer = :courseTrainer")
	List<CourseModules> findCourseModulesByCourseName(String coursenName, String courseTrainer);

	@Query("SELECT   c.courseName,c.courseTrainer,c.courseImage,c.courseDescription, cm.moduleNumber FROM Courses c LEFT JOIN c.courseModule cm WHERE c.courseName = :coursename")
	List<Object[]> getCourseDetails(@Param("coursename") String courseName);

}
