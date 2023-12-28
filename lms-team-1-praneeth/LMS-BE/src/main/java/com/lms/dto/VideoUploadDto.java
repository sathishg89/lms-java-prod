package com.lms.dto;

import java.util.LinkedHashSet;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoUploadDto {

	@NotEmpty(message = "Course name cannot be empty")
	private String courseName;
	@NotEmpty(message = "tname cannot be empty")
	private String courseTrainer;
	@NotEmpty(message = "modulename cannot be empty")
	private String moduleName;
	@Positive(message = "modulenum cannot be negative or empty")
	private int moduleNumber;
	@NotEmpty(message = "videoname cannot be empty")
	private List<String> videoName;
	@NotEmpty(message = "videolink cannot be empty")
	private LinkedHashSet<String> videoLink;

}
