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
public class CourseUpdateDto {

	private String courseName;
	private String courseTrainer;
	private String description;
	private boolean archived;
	private MultipartFile courseImage;

}
