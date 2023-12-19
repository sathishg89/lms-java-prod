package com.lms.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseInfoDto {

	private String coursename;

	private String coursetrainer;
	private byte[] courseimage;

	private String description;
	private List<Integer> modulenum;
}
