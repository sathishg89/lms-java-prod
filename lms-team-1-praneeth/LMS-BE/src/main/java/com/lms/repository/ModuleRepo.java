package com.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.entity.CourseModules;

public interface ModuleRepo extends JpaRepository<CourseModules, Integer> {

}
