package com.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.entity.CourseModules;

public interface ModulesRepo extends JpaRepository<CourseModules, Integer> {

}
