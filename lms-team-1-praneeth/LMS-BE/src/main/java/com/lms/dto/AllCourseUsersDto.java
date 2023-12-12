package com.lms.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lms.entity.CourseUsers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AllCourseUsersDto {

	private String coursename;
	private String coursetrainer;
	
	@JsonIgnoreProperties("coursesList")
	private List<CourseUsers> courseusers;

}
