package com.lms.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

	private long userId;
	private String userName;
	private String userEmail;
	private String jwtToken;
	private String userRole;
	private String userImage;
	@JsonIgnoreProperties({ "useremail", "username" })
	private CourseUserDto userCourses;

}
