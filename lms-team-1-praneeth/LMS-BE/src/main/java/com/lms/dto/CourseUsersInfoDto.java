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

	private int courseid;
	private String coursename;
	private String coursetrainer;
	private String coursecreatedate;
	@JsonIgnoreProperties("coursesList")
	private List<CourseUsers> courseusers;

}
