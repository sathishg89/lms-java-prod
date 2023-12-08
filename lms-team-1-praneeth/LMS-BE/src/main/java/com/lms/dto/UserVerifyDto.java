package com.lms.dto;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "userverifydto")
public class UserVerifyDto {

	@Id
	@Column(name = "email")
	private String userEmail;
	private String otp;
	private LocalDateTime otpGeneratedTime;

}
