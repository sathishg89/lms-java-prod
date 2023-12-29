package com.lms.controller;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;
import java.util.zip.DataFormatException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lms.config.JwtService;
import com.lms.constants.CustomErrorCodes;
import com.lms.dto.CourseUserDto;
import com.lms.dto.UserDto;
import com.lms.dto.UserResponseDto;
import com.lms.entity.User;
import com.lms.exception.details.CustomException;
import com.lms.exception.details.EmailNotFoundException;
import com.lms.service.CourseService;
import com.lms.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private UserService us;

	@Autowired
	private CourseService cs;

	@Autowired
	private JwtService js;

	@Autowired
	private AuthenticationManager am;

	/*
	 * 
	 * API used to generate the and send the JWT token on login and name email
	 * 
	 */

	@PostMapping("/login")
	public ResponseEntity<UserResponseDto> getJwtToken(@RequestBody @Valid UserDto userDto)
			throws IOException, DataFormatException {

		try {
			Authentication authenticate = am.authenticate(
					new UsernamePasswordAuthenticationToken(userDto.getUserEmail(), userDto.getPassword()));

			if (authenticate.isAuthenticated()) {

				String genJwtToken = js.genJwtToken(userDto.getUserEmail());
				Optional<User> output = us.fingbyemail(userDto.getUserEmail());

				byte[] downloadImage = us.getProfilePhoto(userDto.getUserEmail());
				String encodeToString = "";
				String img = "";

				if (downloadImage != null) {
					encodeToString = Base64.getEncoder().encodeToString(downloadImage);
					img = "data:image/png;base64," + encodeToString;
				} else {
					img = output.get().getUserName().substring(0, 2).toUpperCase();
				}

				CourseUserDto uc = null;
				try {
					uc = cs.getCourseUsers(output.get().getUserEmail());

				} catch (Exception e) {

				}

				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);

				UserResponseDto ld2 = new UserResponseDto(output.get().getUserId(), output.get().getUserName(),
						userDto.getUserEmail(), genJwtToken, output.get().getRole(), img, uc);

				return ResponseEntity.ok().headers(headers).body(ld2);
			} else {
				throw new EmailNotFoundException(CustomErrorCodes.INVALID_EMAIL.getErrorMsg(),
						CustomErrorCodes.INVALID_EMAIL.getErrorCode());
			}
		} catch (BadCredentialsException ex) {
			throw new CustomException(CustomErrorCodes.INVALID_PASSWORD.getErrorMsg(),
					CustomErrorCodes.INVALID_PASSWORD.getErrorCode());
		}
	}

}
