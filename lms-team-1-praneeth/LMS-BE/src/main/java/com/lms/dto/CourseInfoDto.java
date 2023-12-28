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

	private String courseName;
	private String courseTrainer;
	private byte[] courseImage;
	private String courseDescription;
	private List<Integer> moduleNumber;
}
