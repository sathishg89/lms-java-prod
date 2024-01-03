package com.lms.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lms.entity.Courses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseUserDto {

	private String userName;
	private String userEmail;
	@JsonIgnoreProperties({ "courseModule", "courseUsers", "courseCreateDate" })
	private List<Courses> coursesList;

}
