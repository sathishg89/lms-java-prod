package com.lms.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.lms.constants.CustomErrorCodes;
import com.lms.dto.CourseDto;
import com.lms.dto.CourseInfoDto;
import com.lms.dto.CourseUpdateDto;
import com.lms.dto.CourseUserDto;
import com.lms.dto.CourseUsersInfoDto;
import com.lms.dto.CoursesListDto;
import com.lms.dto.CoursesModuleInfoDto;
import com.lms.dto.CoursesModuleInfoDto.CoursesModuleInfoDtoBuilder;
import com.lms.dto.ModuleUpdateDto;
import com.lms.dto.VideoUploadDto;
import com.lms.entity.CourseLink;
import com.lms.entity.CourseModules;
import com.lms.entity.CourseUsers;
import com.lms.entity.Courses;
import com.lms.entity.User;
import com.lms.exception.details.CustomException;
import com.lms.repository.UserRepo;
import com.lms.service.CourseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping({ "/admin/course" })
public class CourseController {

	@Autowired
	private CourseService cs;

	@Autowired
	private UserRepo ur;

	/*
	 * 
	 * API used to add course user by admin
	 * 
	 */

	@PostMapping("/addcourseuser")
	public ResponseEntity<String> addCourseUser(@RequestParam String userName, @RequestParam String userEmail) {

		Optional<User> user = ur.findByuserEmail(userEmail);

		if (user.isPresent()) {

			CourseUsers cu = CourseUsers.builder().userName(userName).userEmail(userEmail).build();

			boolean saveUserCourse = cs.saveCourseUser(cu);

			if (saveUserCourse) {
				return new ResponseEntity<String>("CourseUsers Added Successful", HttpStatus.CREATED);
			} else {
				return new ResponseEntity<String>("CourseUsers Added UnSuccessful", HttpStatus.BAD_REQUEST);
			}
		} else {
			throw new CustomException(CustomErrorCodes.USER_NOT_FOUND.getErrorMsg(),
					CustomErrorCodes.USER_NOT_FOUND.getErrorCode());
		}

	}

	/*
	 * 
	 * API used to add course by admin
	 * 
	 */
	@PostMapping("/addcourse")
	public ResponseEntity<String> addCourse(@ModelAttribute @Valid CourseDto courseDto) throws Exception {

		Courses cc;
		if (courseDto.getCourseImage() != null && courseDto.getCourseImage().getBytes() != null) {
			cc = Courses.builder().courseName(courseDto.getCourseName()).courseTrainer(courseDto.getCourseTrainer())
					.courseImage(courseDto.getCourseImage().getBytes())
					.courseDescription(courseDto.getCourseDescription()).build();
		} else {
			cc = Courses.builder().courseName(courseDto.getCourseName()).courseTrainer(courseDto.getCourseTrainer())
					.courseDescription(courseDto.getCourseDescription()).build();
		}

		boolean saveUserCourse = cs.saveCourses(cc);

		if (saveUserCourse) {
			return new ResponseEntity<String>("Courses Added Successful", HttpStatus.CREATED);
		} else {
			return new ResponseEntity<String>("Courses Added UnSuccessful", HttpStatus.BAD_REQUEST);
		}
	}

	/*
	 * 
	 * API used to update course by admin
	 * 
	 */

	@PutMapping("/updatecourse/{coursename}/{courseTrainer}")
	public ResponseEntity<String> updateCourse(@ModelAttribute CourseUpdateDto courseUpdatedto,
			@PathVariable("coursename") String courseName, @PathVariable String courseTrainer) throws Exception {

		Courses cc;
		if (courseUpdatedto.getCourseImage() != null && courseUpdatedto.getCourseImage().getBytes() != null) {

			cc = Courses.builder().courseName(courseUpdatedto.getCourseName())
					.courseTrainer(courseUpdatedto.getCourseTrainer())
					.courseImage(courseUpdatedto.getCourseImage().getBytes())
					.courseDescription(courseUpdatedto.getCourseDescription()).archived(courseUpdatedto.isArchived())
					.build();
		} else {

			cc = Courses.builder().courseName(courseUpdatedto.getCourseName())
					.courseTrainer(courseUpdatedto.getCourseTrainer())
					.courseDescription(courseUpdatedto.getCourseDescription()).archived(courseUpdatedto.isArchived())
					.build();

		}

		boolean saveUserCourse = cs.updateCourses(cc, courseName, courseTrainer);

		if (saveUserCourse) {
			return new ResponseEntity<String>("Courses Updation Successful", HttpStatus.CREATED);
		} else {
			return new ResponseEntity<String>("Courses Updation UnSuccessful", HttpStatus.BAD_REQUEST);
		}
	}

	/*
	 * 
	 * API used to give access of course to user by admin
	 * 
	 */

	@PostMapping("/accesscoursetouser")
	public ResponseEntity<String> accessCouresToUser(@RequestParam String userEmail, @RequestParam String courseName,
			@RequestParam String courseTrainer) {
		boolean accessTocoures = cs.accessCouresToUser(userEmail, courseName, courseTrainer);

		if (accessTocoures) {
			return new ResponseEntity<String>("Course Access Successful", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Course Access UnSuccessful", HttpStatus.BAD_REQUEST);
		}

	}

	/*
	 * 
	 * API used to upload videos by admin
	 * 
	 */

	@PostMapping("/savevideo")
	public ResponseEntity<String> saveVideo(@RequestBody @Valid VideoUploadDto videoDto) {
		boolean addVideoLink = cs.addVideoLink(videoDto);

		if (addVideoLink) {
			return new ResponseEntity<>("Video Uploading Successful", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Video Uploading UnSuccessful", HttpStatus.BAD_REQUEST);
		}

	}

	/*
	 * 
	 * API used to get course user info of particular user by admin
	 * 
	 */

	@GetMapping("/getcourseuserinfo/{userEmail}")
	public ResponseEntity<CourseUserDto> getCourseUserDetails(@PathVariable("userEmail") String userEmail) {

		CourseUserDto uc = cs.getCourseUsers(userEmail);

		if (uc == null) {
			throw new CustomException(CustomErrorCodes.USER_NOT_FOUND.getErrorMsg(),
					CustomErrorCodes.USER_NOT_FOUND.getErrorCode());
		} else {
			return new ResponseEntity<CourseUserDto>(uc, HttpStatus.OK);
		}

	}

	/*
	 * 
	 * API used to get all user under the particular course by admin
	 * 
	 */

	@GetMapping("/getcourseusers/{courseName}/{courseTrainer}")
	public ResponseEntity<List<CourseUsersInfoDto>> getCourses(@PathVariable("courseName") String courseName,
			@PathVariable("courseTrainer") String courseTrainer) {

		List<CourseUsersInfoDto> uc = cs.getCourses(courseName, courseTrainer);

		if (uc.size() == 0) {
			throw new CustomException(CustomErrorCodes.INVALID_DETAILS.getErrorMsg(),
					CustomErrorCodes.INVALID_DETAILS.getErrorCode());
		} else {
			return new ResponseEntity<List<CourseUsersInfoDto>>(uc, HttpStatus.OK);
		}

	}

	/*
	 * 
	 * API used to delete course by admin
	 * 
	 */

	@DeleteMapping("/deletecourse/{courseName}/{courseTrainer}")
	public ResponseEntity<String> deleteCourse(@PathVariable("courseName") String courseName,
			@PathVariable("courseTrainer") String courseTrainer) {

		if (cs.deleteCourse(courseName, courseTrainer)) {
			return new ResponseEntity<String>("Course Deletion Successful", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Course Deletion UnSuccessful", HttpStatus.BAD_REQUEST);
		}
	}

	/*
	 * 
	 * API used to get all videos of course by admin
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

	/*
	 * 
	 * API used to get all course by admin
	 * 
	 */

	@GetMapping("/getallcourses")
	public ResponseEntity<List<CoursesListDto>> getAllCourses() {
		List<CoursesListDto> allCourses = cs.getAllCourses();
		return new ResponseEntity<List<CoursesListDto>>(allCourses, HttpStatus.OK);
	}

	/*
	 * 
	 * API used to get course info of particular course by admin
	 * 
	 */

	@GetMapping("/{courseName}/courseinfo")
	public ResponseEntity<CourseInfoDto> getCourseInfo(@PathVariable("courseName") String courseName) {

		CourseInfoDto courseInfo = cs.getCourseInfo(courseName);

		if (courseInfo != null) {
			return new ResponseEntity<CourseInfoDto>(courseInfo, HttpStatus.OK);
		} else {
			throw new CustomException(CustomErrorCodes.COURSE_NOT_FOUND.getErrorMsg(),
					CustomErrorCodes.COURSE_NOT_FOUND.getErrorCode());
		}

	}

	/*
	 * 
	 * API used to get modules of particular course by admin
	 * 
	 */

	@GetMapping("/{courseName}/{courseTrainer}/getmodules")
	public ResponseEntity<List<CourseModules>> getModules(@PathVariable("courseName") String courseName,
			@PathVariable String courseTrainer) {

		List<CourseModules> courseModules = cs.getCourseModules(courseName, courseTrainer);

		if (courseModules.size() > 0) {
			return new ResponseEntity<List<CourseModules>>(courseModules, HttpStatus.OK);
		} else {
			return new ResponseEntity<List<CourseModules>>(courseModules, HttpStatus.BAD_REQUEST);
		}
	}

	/*
	 * 
	 * API used to update modules of particular course by admin
	 * 
	 */

	@PutMapping("/{courseName}/{moduleId}/updatemodules")
	public ResponseEntity<List<CourseModules>> updateModules(@RequestBody @Valid ModuleUpdateDto mud,
			@PathVariable("courseName") String courseName, @PathVariable("moduleId") int moduleNumber) {

		HttpHeaders hd = new HttpHeaders();
		hd.setContentType(MediaType.APPLICATION_JSON);

		List<CourseModules> updateModule = cs.updateModule(courseName, moduleNumber, mud);

		return new ResponseEntity<List<CourseModules>>(updateModule, HttpStatus.OK);
	}

	/*
	 * 
	 * API used to delete modules of particular course by admin
	 * 
	 */
	@DeleteMapping("/{courseName}/{moduleId}/deletemodule")
	public ResponseEntity<String> deleteModule(@PathVariable("courseName") String courseName,
			@PathVariable("moduleId") int moduleNumber) {
		boolean deleteModule = cs.deleteModule(courseName, moduleNumber);

		if (deleteModule) {
			return new ResponseEntity<String>("Module Deletion Successful", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Module Deletion UnSuccessful", HttpStatus.BAD_REQUEST);
		}
	}

}
