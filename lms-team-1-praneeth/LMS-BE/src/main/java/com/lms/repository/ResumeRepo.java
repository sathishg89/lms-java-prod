package com.lms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.entity.Resume;

public interface ResumeRepo extends JpaRepository<Resume, Integer> {

	List<Resume> findByUserEmail(String userEmail);

}
