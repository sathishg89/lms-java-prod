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
public class CourseUsersInfoDto {

	private int courseId;
	private String courseName;
	private String courseTrainer;
	private String courseCreateDate;
	@JsonIgnoreProperties("coursesList")
	private List<CourseUsers> courseUsers;

}
