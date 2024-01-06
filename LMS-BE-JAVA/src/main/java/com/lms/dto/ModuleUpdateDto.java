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
public class ModuleUpdateDto {

	private String moduleName;
	private List<String> videoLink;
	private List<String> videoName;

}
