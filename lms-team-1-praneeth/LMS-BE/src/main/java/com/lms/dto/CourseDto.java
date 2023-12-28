package com.lms.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseDto {

	@NotEmpty(message = "coursename cannot be empty")
	private String courseName;
	@NotEmpty(message = "courseTrainer cannot be empty")
	private String courseTrainer;
	private String courseDescription;
	private MultipartFile courseImage;

}
