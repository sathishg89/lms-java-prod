package com.lms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.dto.UserVerifyDto;

public interface OtpRepo extends JpaRepository<UserVerifyDto, String> {

	Optional<UserVerifyDto> findByemail(String userEmail);
}
