package com.lms.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateDto {

	private String userName;

	private String userEmail;

	private String password;

	private MultipartFile profilePhoto;

	private String role;

	private boolean isActive;

}
