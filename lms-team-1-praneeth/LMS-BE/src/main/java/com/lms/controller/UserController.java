package com.lms.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.zip.DataFormatException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lms.constants.CustomErrorCodes;
import com.lms.dto.CoursesModuleInfoDto;
import com.lms.dto.UserVerifyDto;
import com.lms.dto.CoursesModuleInfoDto.CoursesModuleInfoDtoBuilder;
import com.lms.entity.CourseLink;
import com.lms.entity.CourseModules;
import com.lms.entity.User;
import com.lms.exception.details.CustomException;
import com.lms.service.CourseService;
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

	@PostMapping("/uploadimage/{userEmail}")
	public ResponseEntity<String> uploadImage(@RequestParam("file") @Valid MultipartFile multiPartFile,
			@PathVariable("userEmail") String userEmail) throws Exception {

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

	@GetMapping("/downloadimage/{userEmail}")
	public ResponseEntity<byte[]> downloadImage(@PathVariable("userEmail") String userEmail)
			throws IOException, DataFormatException {
		byte[] imageData = us.downloadImage(userEmail);

		if (imageData != null) {
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.IMAGE_JPEG).body(imageData);
		} else {
			throw new CustomException(CustomErrorCodes.MISSING_IMAGE.getErrorMsg(),
					CustomErrorCodes.MISSING_IMAGE.getErrorCode());
		}

	}

	/*
	 * 
	 * API used to update the details of user
	 * 
	 */

	@PutMapping("/update/{userEmail}")
	public ResponseEntity<User> UserUpdate(@RequestBody User user, @PathVariable("userEmail") String UserEmail) {

		User luupdate = us.userUpdate(user, UserEmail);
		if (luupdate != null) {

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
			@RequestParam("verifypassword") String verifypassword, @RequestParam("id") long id) {

		boolean resetPassword = us.resetPassword(password, verifypassword, id);

		if (resetPassword) {
			return new ResponseEntity<String>("Reset Password Done", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Unable To Reset Password", HttpStatus.BAD_REQUEST);
		}

	}

	@PostMapping("/{userEmail}/uploadresume")
	public ResponseEntity<String> saveResume(@PathVariable("userEmail") String userEmail,
			@RequestBody MultipartFile multipart) throws Exception {

		boolean saveResume = cs.saveResume(userEmail, multipart);

		if (saveResume) {
			return new ResponseEntity<String>("Resume Saved", HttpStatus.OK);
		}
		return new ResponseEntity<String>("Resume Not Saved", HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/{userEmail}/getresume")
	public ResponseEntity<byte[]> getResumes(@PathVariable("email") String userEmail) {

		byte[] resume = cs.getResume(userEmail);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		if (resume != null) {
			return new ResponseEntity<byte[]>(resume, HttpStatus.OK);
		} else {
			return new ResponseEntity<byte[]>(resume, HttpStatus.BAD_REQUEST);
		}

	}

	@DeleteMapping("/{userEmail}/deleteresume")
	public ResponseEntity<String> deleteResume(@PathVariable("userEmail") String userEmail) {

		boolean deleteResume = cs.deleteResume(userEmail);

		if (deleteResume) {
			return new ResponseEntity<String>("Resume Deletion Successfull", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Resume Deletion UnSuccessfull", HttpStatus.OK);
		}

	}

	@GetMapping("/{courseName}/{trainerName}/getvideos")
	public ResponseEntity<List<CoursesModuleInfoDto>> getVideos(@PathVariable("courseName") String courseName,
			@PathVariable("trainerName") String trainerName) {

		List<CourseModules> getcourse = cs.getCourseModules(courseName, trainerName);

		List<Integer> mn = getcourse.stream().map(x -> x.getModulenum()).collect(Collectors.toList());

		List<String> mname = getcourse.stream().map(x -> x.getModulename()).collect(Collectors.toList());

		List<List<CourseLink>> collect = getcourse.stream().map(x -> x.getClinks()).collect(Collectors.toList());

		List<List<CourseLink>> findFirst = collect.stream().toList();

		List<List<String>> listoflinks = findFirst.stream().flatMap(clinks -> clinks.stream().map(CourseLink::getLinks))
				.collect(Collectors.toList());

		List<List<String>> listofvideonames = findFirst.stream()
				.flatMap(clinks -> clinks.stream().map(CourseLink::getVideoname)).collect(Collectors.toList());

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
						.modulenum(mn.get(i)).modulename(mname.get(i)).videos(resultMapList.get(i)))
				.collect(Collectors.toList());

		List<CoursesModuleInfoDto> list = combinedList.stream().map(CoursesModuleInfoDtoBuilder::build)
				.collect(Collectors.toList());

		return new ResponseEntity<List<CoursesModuleInfoDto>>(list, HttpStatus.OK);
	}

}
