package com.lms.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "build")
public class UserDto {

	@NotEmpty(message = "Email cannot be empty")
	private String userEmail;
	@NotEmpty(message = "Password cannot be empty")
	private String password;

}
