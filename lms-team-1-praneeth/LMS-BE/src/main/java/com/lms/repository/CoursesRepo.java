package com.lms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lms.dto.CoursesListDto;
import com.lms.entity.CourseModules;
import com.lms.entity.Courses;

public interface CoursesRepo extends JpaRepository<Courses, Integer> {

	List<Courses> findBycoursename(String courseName);

	boolean existsBycoursename(String courseName);

	@Query("SELECT c FROM Courses c WHERE c.coursename = :courseName AND c.coursetrainer = :courseTrainer")
	List<Courses> findBycoursenameAndcoursetrainer(String courseName, String courseTrainer);

	@Query("select  new com.lms.dto.CoursesListDto(c.coursename ,c.coursetrainer) from Courses c")
	List<CoursesListDto> getOnlyCourses();

	@Query("SELECT cm FROM Courses c JOIN c.coursemodule cm WHERE c.coursename = :coursename and c.coursetrainer = :trainername")
	List<CourseModules> findCourseModulesByCourseName(String coursename, String trainername);

	@Query("SELECT   c.coursename,c.coursetrainer,c.courseimage,c.description, cm.modulenum FROM Courses c LEFT JOIN c.coursemodule cm WHERE c.coursename = :coursename")
	List<Object[]> getCourseDetails(@Param("coursename") String courseName);

}
