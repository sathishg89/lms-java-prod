package com.lms.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;
import java.util.zip.DataFormatException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lms.config.JwtService;
import com.lms.dto.UserCoursesDto;
import com.lms.dto.UserDto;
import com.lms.dto.UserResponseDto;
import com.lms.dto.UserVerifyDto;
import com.lms.entity.User;
import com.lms.exception.details.CustomException;
import com.lms.exception.details.EmailNotFoundException;
import com.lms.service.CourseService;
import com.lms.service.UserService;
import com.lms.serviceImpl.EmailService;
import com.lms.serviceImpl.OtpService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

//@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService lus;

	@Autowired
	private CourseService cs;

	@Autowired
	private JwtService js;

	@Autowired
	private AuthenticationManager am;

	@Autowired
	private OtpService otps;

	@Autowired
	private EmailService es;

	@GetMapping("/getallapi")
	public void redirectToSwagger(HttpServletResponse response) throws IOException {
		response.sendRedirect("/swagger-ui/index.html#/");
	}

	/*
	 * 
	 * API used to test the connect for connection of back-end with front-end
	 * 
	 */

	@GetMapping("/connect")
	public String login() {
		return "Connected To Back-End";
	}

	/*
	 * 
	 * API used to generate the and send the JWT token on login and name email
	 * 
	 */

	@PostMapping("/login")
	public ResponseEntity<UserResponseDto> getJwtToken(@RequestBody @Valid UserDto jwt)
			throws IOException, DataFormatException {

		try {
			Authentication authenticate = am
					.authenticate(new UsernamePasswordAuthenticationToken(jwt.getEmail(), jwt.getPassword()));

			if (authenticate.isAuthenticated()) {

				String genJwtToken = js.genJwtToken(jwt.getEmail());
				Optional<User> output = lus.fingbyemail(jwt.getEmail());

				byte[] downloadImage = lus.downloadImage(jwt.getEmail());
				String encodeToString = "";
				String img = "";

				if (downloadImage != null) {
					encodeToString = Base64.getEncoder().encodeToString(downloadImage);
					img = "data:image/png;base64" + encodeToString;
				} else {
					img = output.get().getName().substring(0, 2).toUpperCase();
				}

				UserCoursesDto uc = null;
				try {
					uc = cs.getCourseUsers(output.get().getEmail());

				} catch (Exception e) {

				}

				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);

				UserResponseDto ld2 = new UserResponseDto(output.get().getId(), output.get().getName(), jwt.getEmail(),
						genJwtToken, output.get().getRoles(), img, uc);

				return ResponseEntity.ok().headers(headers).body(ld2);
			} else {
				throw new EmailNotFoundException("Email Not Found");
			}
		} catch (BadCredentialsException ex) {
			throw new CustomException("Password Incorrect");
		}
	}

	/*
	 * 
	 * API used to upload the image files to the DB based on the email
	 * 
	 */

	@PostMapping("/upload")
	public ResponseEntity<String> uploadImage(@RequestParam("file") @Valid MultipartFile mp, String email)
			throws Exception {

		String uploadImage = lus.saveImg(mp, email);

		if (uploadImage.equals(null)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed");
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(uploadImage);
		}
	}

	/*
	 * 
	 * API used to retrieve the image from the DB using the email as parameter
	 * 
	 */

	@GetMapping("/{email}")
	public ResponseEntity<String> downloadImage(@PathVariable("email") String email)
			throws IOException, DataFormatException {
		byte[] imageData = lus.downloadImage(email);
		String encodeToString = "";
		String img = "";

		if (imageData != null) {
			encodeToString = Base64.getEncoder().encodeToString(imageData);
			img = "data:image/png;base64 " + encodeToString;
		} else {
			img = email.substring(0, 2).toUpperCase();
		}
		return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_HTML).body(img);

	}

	/*
	 * 
	 * API used to update the details of user
	 * 
	 */

	@PutMapping("/update")
	public ResponseEntity<String> learnerUserUpdate(@RequestBody User lu, @RequestParam String email) {

		lus.Luupdate(lu, email);

		return new ResponseEntity<String>("User details updated", HttpStatus.ACCEPTED);

	}

	/*
	 * 
	 * API used to get otp for generating the otp for verify the account
	 * 
	 */

	@PostMapping("/getotp")
	public ResponseEntity<String> getotp(@RequestParam String email) throws Exception {

		String generateOtp = otps.generateOtp();

		es.sendOtpEmail(email, generateOtp);

		UserVerifyDto userVerifyDto = new UserVerifyDto(email, generateOtp, LocalDateTime.now());
		lus.saveotp(userVerifyDto);

		return new ResponseEntity<String>("OTP SENT", HttpStatus.OK);
	}

	/*
	 * 
	 * API used to verify the otp for verify the user and reseting the password
	 * 
	 */

	@GetMapping("/verifyacc")
	public ResponseEntity<String> verifyAccount(@RequestParam("email") String email, @RequestParam("otp") String otp) {
		boolean verifyAccount = lus.verifyAccount(email, otp);

		if (verifyAccount) {
			return new ResponseEntity<String>("OTP Verified", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Invalid OTP", HttpStatus.BAD_REQUEST);
		}
	}

	/*
	 * 
	 * API used to reset the password and save the new password
	 * 
	 */

	@PostMapping("/resetpassword")
	public ResponseEntity<String> saveNewPassword(@RequestParam("password") String password,
			@RequestParam("verifypassword") String verifypassword, @RequestParam("id") long id) {

		boolean resetPassword = lus.resetPassword(password, verifypassword, id);

		if (resetPassword) {
			return new ResponseEntity<String>("Reset Password Done", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Unable To Reset Password", HttpStatus.BAD_REQUEST);
		}

	}

}
