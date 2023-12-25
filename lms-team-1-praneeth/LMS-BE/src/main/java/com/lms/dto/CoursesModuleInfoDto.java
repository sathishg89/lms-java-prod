package com.lms.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CoursesModuleInfoDto {

	private int modulenum;
	private String modulename;
	private Map<String, String> videos;

}
