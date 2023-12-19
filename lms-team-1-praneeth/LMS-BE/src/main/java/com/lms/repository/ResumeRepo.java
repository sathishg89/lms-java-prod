package com.lms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.entity.Resume;

public interface ResumeRepo extends JpaRepository<Resume, Integer> {

	Optional<Resume> findByUserEmail(String userEmail);

}
