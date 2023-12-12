package com.lms.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.zip.DataFormatException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lms.dto.UserVerifyDto;
import com.lms.entity.User;
import com.lms.service.UserService;
import com.lms.serviceImpl.EmailService;
import com.lms.serviceImpl.OtpService;

import jakarta.validation.Valid;

//@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService us;

	@Autowired
	private OtpService otps;

	@Autowired
	private EmailService es;

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
	 * API used to upload the image files to the DB based on the email
	 * 
	 */

	@PostMapping("/uploadimage")
	public ResponseEntity<String> uploadImage(@RequestParam("file") @Valid MultipartFile multiPartFile,
			String userEmail) throws Exception {

		String uploadImage = us.saveImg(multiPartFile, userEmail);

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

	@GetMapping("/download/{email}")
	public ResponseEntity<String> downloadImage(@PathVariable("email") String userEmail)
			throws IOException, DataFormatException {
		byte[] imageData = us.downloadImage(userEmail);
		String encodeToString = "";
		String img = "";

		if (imageData != null) {
			encodeToString = Base64.getEncoder().encodeToString(imageData);
			img = "data:image/png;base64," + encodeToString;
		} else {
			img = userEmail.substring(0, 2).toUpperCase();
		}
		return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_HTML).body(img);

	}

	/*
	 * 
	 * API used to update the details of user
	 * 
	 */

	@PutMapping("/update")
	public ResponseEntity<String> UserUpdate(@RequestBody User user, @RequestParam String UserEmail) {

		User luupdate = us.userUpdate(user, UserEmail);
		if (luupdate == null) {
			return new ResponseEntity<String>("User details updated", HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<String>("User details updated", HttpStatus.OK);
		}

	}

	/*
	 * 
	 * API used to get otp for generating the otp for verify the account
	 * 
	 */

	@PostMapping("/getotp")
	public ResponseEntity<String> getotp(@RequestParam String userEmail) throws Exception {

		String generateOtp = otps.generateOtp();

		es.sendOtpEmail(userEmail, generateOtp);

		UserVerifyDto userVerifyDto = UserVerifyDto.builder().userEmail(userEmail).otp(generateOtp)
				.otpGeneratedTime(LocalDateTime.now()).build();
		boolean saveotp = us.saveotp(userVerifyDto);

		if (saveotp) {
			return new ResponseEntity<String>("OTP SENT", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("OTP NOT SENT", HttpStatus.BAD_GATEWAY);
		}

	}

	/*
	 * 
	 * API used to verify the otp for verify the user and reseting the password
	 * 
	 */

	@GetMapping("/verifyacc")
	public ResponseEntity<String> verifyAccount(@RequestParam("email") String userEmail,
			@RequestParam("otp") String otp) {
		boolean verifyAccount = us.verifyAccount(userEmail, otp);

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

	@PutMapping("/resetpassword")
	public ResponseEntity<String> saveNewPassword(@RequestParam("password") String password,
			@RequestParam("verifypassword") String verifypassword, @RequestParam("id") long id) {

		boolean resetPassword = us.resetPassword(password, verifypassword, id);

		if (resetPassword) {
			return new ResponseEntity<String>("Reset Password Done", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Unable To Reset Password", HttpStatus.BAD_REQUEST);
		}

	}

}
