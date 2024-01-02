package com.lms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lms.entity.CourseUsers;

public interface CourseUsersRepo extends JpaRepository<CourseUsers, Integer> {

	CourseUsers findByuserEmail(String userEmail);


	boolean existsByuserEmail(String userEmail);

	@Query("select cu from CourseUsers cu ")
	List<CourseUsers> getbycouser();

}
