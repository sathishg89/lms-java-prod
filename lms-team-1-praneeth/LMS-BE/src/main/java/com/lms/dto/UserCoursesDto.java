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
public class UserCoursesDto {

	private String username;
	private String useremail;
	@JsonIgnoreProperties({ "coursemodule", "courseusers", "courseinsertdate" })
	private List<Courses> courseslist;

}
