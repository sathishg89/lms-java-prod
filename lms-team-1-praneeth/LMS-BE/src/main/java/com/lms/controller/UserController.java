package com.lms.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.zip.DataFormatException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import com.lms.constants.CustomErrorCodes;
import com.lms.dto.CoursesModuleInfoDto;
import com.lms.dto.CoursesModuleInfoDto.CoursesModuleInfoDtoBuilder;
import com.lms.dto.UserUpdateDto;
import com.lms.dto.UserVerifyDto;
import com.lms.entity.CourseLink;
import com.lms.entity.CourseModules;
import com.lms.entity.CourseUsers;
import com.lms.entity.User;
import com.lms.exception.details.CustomException;
import com.lms.service.CourseService;
import com.lms.service.UserService;
import com.lms.serviceImpl.EmailService;
import com.lms.serviceImpl.OtpService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService us;

	@Autowired
	private OtpService otps;

	@Autowired
	private EmailService es;

	@Autowired
	private CourseService cs;

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

	@Value("${spring.servlet.multipart.max-file-size}")
	private String maxFileSize;

	private long parseSize(String size) {
		size = size.toUpperCase();
		long multiplier = 1;
		if (size.endsWith("KB")) {
			multiplier = 1024;
			size = size.substring(0, size.length() - 2);
		} else if (size.endsWith("MB")) {
			multiplier = 1024 * 1024;
			size = size.substring(0, size.length() - 2);
		} else if (size.endsWith("GB")) {
			multiplier = 1024 * 1024 * 1024;
			size = size.substring(0, size.length() - 2);
		}
		return Long.parseLong(size) * multiplier;
	}

	@PostMapping("/uploadimage/{userEmail}")
	public ResponseEntity<String> uploadImage(@RequestParam("photo") @Valid MultipartFile profilePhoto,
			@PathVariable("userEmail") String userEmail) throws Exception {

		long parseSize = parseSize(maxFileSize);

		String uploadImage = us.saveProfilePhoto(profilePhoto, userEmail);

		if (parseSize < profilePhoto.getSize()) {
			throw new MaxUploadSizeExceededException(profilePhoto.getSize());
		} else if (uploadImage.equals(null)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed To Upload Image");
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(uploadImage);
		}
	}

	/*
	 * 
	 * API used to retrieve the image from the DB using the email as parameter
	 * 
	 */

	@GetMapping("/downloadimage/{userEmail}")
	public ResponseEntity<String> downloadImage(@PathVariable("userEmail") String userEmail)
			throws IOException, DataFormatException {
		byte[] imageData = us.getProfilePhoto(userEmail);

		String encodeToString = Base64.getEncoder().encodeToString(imageData);
		String img = "data:image/png;base64," + encodeToString;

		if (imageData != null) {
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_PLAIN).body(img);
		} else {
			throw new CustomException(CustomErrorCodes.MISSING_IMAGE.getErrorMsg(),
					CustomErrorCodes.MISSING_IMAGE.getErrorCode());
		}

	}

	/*
	 * 
	 * API used to update the details by user
	 * 
	 */

	@PutMapping("/update/{userEmail}")
	public ResponseEntity<User> UserUpdate(@ModelAttribute UserUpdateDto userUpdateDto,
			@PathVariable("userEmail") String UserEmail) throws Exception {

		User luupdate = us.userUpdate(userUpdateDto, UserEmail);

		CourseUsers cs1 = CourseUsers.builder().userEmail(userUpdateDto.getUserEmail())
				.userName(userUpdateDto.getUserName()).build();

		boolean updateCourseUser = cs.updateCourseUser(cs1, UserEmail);

		if (luupdate == null && !updateCourseUser) {
			return new ResponseEntity<User>(luupdate, HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<User>(luupdate, HttpStatus.OK);
		}

	}

	/*
	 * 
	 * API used to get otp for generating the otp for verify the account
	 * 
	 */

	@PostMapping("/getotp/{userEmail}")
	public ResponseEntity<String> getotp(@PathVariable("userEmail") String userEmail) throws Exception {

		String generateOtp = otps.generateOtp();

		es.sendOtpEmail(userEmail, generateOtp);

		UserVerifyDto userVerifyDto = UserVerifyDto.builder().userEmail(userEmail).otp(generateOtp)
				.otpGeneratedTime(LocalDateTime.now()).build();
		boolean saveotp = us.saveotp(userVerifyDto);

		if (saveotp) {
			return new ResponseEntity<String>("OTP Sent Successful", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("OTP Sent UnSuccessful", HttpStatus.BAD_GATEWAY);
		}

	}

	/*
	 * 
	 * API used to verify the otp for verify the user and reseting the password
	 * 
	 */

	@GetMapping("/verifyotp")
	public ResponseEntity<String> verifyAccount(@RequestParam("userEmail") String userEmail,
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
			@RequestParam("verifypassword") String verifyPassword, @RequestParam("id") long userId) {

		boolean resetPassword = us.resetPassword(password, verifyPassword, userId);

		if (resetPassword) {
			return new ResponseEntity<String>("Reset Password Successful", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Reset Password UnSuccessful", HttpStatus.BAD_REQUEST);
		}

	}

	/*
	 * 
	 * API used to upload the resume by user
	 * 
	 */

	@PostMapping("/{userEmail}/uploadresume")
	public ResponseEntity<String> saveResume(@PathVariable("userEmail") String userEmail,
			@RequestBody MultipartFile resume) throws Exception {

		boolean saveResume = cs.saveResume(userEmail, resume);

		if (saveResume) {
			return new ResponseEntity<String>("Resume Saved Successful", HttpStatus.OK);
		}
		return new ResponseEntity<String>("Resume Saving UnSuccessful", HttpStatus.BAD_REQUEST);
	}

	/*
	 * 
	 * API used to download resume by user
	 * 
	 */

	@GetMapping("/{userEmail}/getresume")
	public ResponseEntity<byte[]> getResumes(@PathVariable("userEmail") String userEmail) {

		byte[] resume = cs.getResume(userEmail);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		if (resume != null) {
			return new ResponseEntity<byte[]>(resume, HttpStatus.OK);
		} else {
			return new ResponseEntity<byte[]>(resume, HttpStatus.BAD_REQUEST);
		}

	}

	/*
	 * 
	 * API used to delete resume by user
	 * 
	 */

	@DeleteMapping("/{userEmail}/deleteresume")
	public ResponseEntity<String> deleteResume(@PathVariable("userEmail") String userEmail) {

		boolean deleteResume = cs.deleteResume(userEmail);

		if (deleteResume) {
			return new ResponseEntity<String>("Resume Deletion Successfull", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Resume Deletion UnSuccessfull", HttpStatus.OK);
		}

	}

	/*
	 * 
	 * API used to get all videos of particular course and trainer by user
	 * 
	 */

	@GetMapping("/{courseName}/{courseTrainer}/getvideos")
	public ResponseEntity<List<CoursesModuleInfoDto>> getVideos(@PathVariable("courseName") String courseName,
			@PathVariable("courseTrainer") String courseTrainer) {

		List<CourseModules> getcourse = cs.getCourseModules(courseName, courseTrainer);

		List<Integer> mn = getcourse.stream().map(x -> x.getModuleNumber()).collect(Collectors.toList());

		List<String> mname = getcourse.stream().map(x -> x.getModuleName()).collect(Collectors.toList());

		List<List<CourseLink>> collect = getcourse.stream().map(x -> x.getCourseLinks()).collect(Collectors.toList());

		List<List<CourseLink>> findFirst = collect.stream().toList();

		List<List<String>> listoflinks = findFirst.stream()
				.flatMap(clinks -> clinks.stream().map(CourseLink::getVideoLink)).collect(Collectors.toList());

		List<List<String>> listofvideonames = findFirst.stream()
				.flatMap(clinks -> clinks.stream().map(CourseLink::getVideoName)).collect(Collectors.toList());

		List<Map<String, String>> resultMapList = new ArrayList<>();

		for (int i = 0; i < listoflinks.size(); i++) {
			List<String> list2 = listoflinks.get(i);
			List<String> list3 = listofvideonames.get(i);

			Map<String, String> resultMap = new HashMap<>();

			for (int j = 0; j < list2.size(); j++) {

				resultMap.put(list3.get(j), list2.get(j));
			}

			resultMapList.add(resultMap);
		}

		List<CoursesModuleInfoDtoBuilder> combinedList = IntStream
				.range(0, Math.min(mn.size(), resultMapList.size())).mapToObj(i -> CoursesModuleInfoDto.builder()
						.moduleNumber(mn.get(i)).moduleName(mname.get(i)).videoInfo(resultMapList.get(i)))
				.collect(Collectors.toList());

		List<CoursesModuleInfoDto> list = combinedList.stream().map(CoursesModuleInfoDtoBuilder::build)
				.collect(Collectors.toList());

		return new ResponseEntity<List<CoursesModuleInfoDto>>(list, HttpStatus.OK);
	}

}
