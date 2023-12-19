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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lms.constants.CustomErrorCodes;
import com.lms.dto.AddCourseDto;
import com.lms.dto.CourseInfoDto;
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
@RequestMapping({ "/admin/course", "/user/course" })
public class CourseController {

	@Autowired
	private CourseService cs;

	@Autowired
	private UserRepo ur;

	@PostMapping("/addcourseuser")
	public ResponseEntity<String> addCourseUser(@RequestParam String userName, @RequestParam String userEmail) {

		Optional<User> user = ur.findByuserEmail(userEmail);

		if (user.isPresent()) {

			CourseUsers cu = CourseUsers.builder().userName(userName).userEmail(userEmail).build();

			boolean saveUserCourse = cs.saveCourseUser(cu);

			if (saveUserCourse) {
				return new ResponseEntity<String>("CourseUsers Saved", HttpStatus.CREATED);
			} else {
				return new ResponseEntity<String>("Unable To Save CourseUsers", HttpStatus.BAD_REQUEST);
			}
		} else {
			throw new CustomException(CustomErrorCodes.USER_NOT_FOUND.getErrorMsg(),
					CustomErrorCodes.USER_NOT_FOUND.getErrorCode());
		}

	}

	@PostMapping("/addcourse")
	public ResponseEntity<String> addCourse(@RequestBody @Valid AddCourseDto acd) throws Exception {
		Courses cc = Courses.builder().coursename(acd.getCourseName()).coursetrainer(acd.getCourseTrainer())
				.courseimage(acd.getCourseImage().getBytes()).description(acd.getDescription()).build();
		boolean saveUserCourse = cs.saveCourses(cc);

		if (saveUserCourse) {
			return new ResponseEntity<String>("Courses Saved", HttpStatus.CREATED);
		} else {
			return new ResponseEntity<String>("Unable To Save Courses", HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/accesscoursetouser")
	public ResponseEntity<String> accessCouresToUser(@RequestParam String courseUserEmail,
			@RequestParam String courseName, @RequestParam String trainerName) {
		boolean accessTocoures = cs.accessCouresToUser(courseUserEmail, courseName, trainerName);

		if (accessTocoures) {
			return new ResponseEntity<String>("Course Added To User", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Course Unable To Add User", HttpStatus.BAD_REQUEST);
		}

	}

	@PostMapping("/addvideolink")
	public ResponseEntity<String> addVideolink(@RequestBody @Valid VideoUploadDto videoDto) {
		boolean addVideoLink = cs.addVideoLink(videoDto);

		if (addVideoLink) {
			return new ResponseEntity<>("saved", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Video Not Saved", HttpStatus.BAD_REQUEST);
		}

	}

	@GetMapping("/getcourseuserinfo/{email}")
	public ResponseEntity<CourseUserDto> getCourseUserDetails(@PathVariable("email") String userEmail) {

		CourseUserDto uc = cs.getCourseUsers(userEmail);

		if (uc == null) {
			throw new CustomException(CustomErrorCodes.USER_NOT_FOUND.getErrorMsg(),
					CustomErrorCodes.USER_NOT_FOUND.getErrorCode());
		} else {
			return new ResponseEntity<CourseUserDto>(uc, HttpStatus.OK);
		}

	}

	@GetMapping("/getcourseusers/{coursename}/{trainername}")
	public ResponseEntity<List<CourseUsersInfoDto>> getCourses(@PathVariable("coursename") String courseName,
			@PathVariable("trainername") String trainerName) {

		List<CourseUsersInfoDto> uc = cs.getCourses(courseName, trainerName);

		if (uc.size() == 0) {
			throw new CustomException(CustomErrorCodes.INVALID_DETAILS.getErrorMsg(),
					CustomErrorCodes.INVALID_DETAILS.getErrorCode());
		} else {
			return new ResponseEntity<List<CourseUsersInfoDto>>(uc, HttpStatus.OK);
		}

	}

	@DeleteMapping("/deletecourse/{coursename}/{trainername}")
	public ResponseEntity<String> deleteCourse(@PathVariable("coursename") String courseName,
			@PathVariable("trainername") String trainerName) {

		if (cs.deleteCourse(courseName, trainerName)) {
			return new ResponseEntity<String>("Course Deleted", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("UnAble To  Course Deleted", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/{coursename}/{trainername}/getvideos")
	public ResponseEntity<List<CoursesModuleInfoDto>> getVideos(@PathVariable("coursename") String courseName,
			@PathVariable("trainername") String trainerName) {

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

	@GetMapping("/getallcourses")
	public ResponseEntity<List<CoursesListDto>> getAllCourses() {
		List<CoursesListDto> allCourses = cs.getAllCourses();
		return new ResponseEntity<List<CoursesListDto>>(allCourses, HttpStatus.OK);
	}

	@GetMapping("/{coursename}/courseinfo")
	public ResponseEntity<CourseInfoDto> getCourseInfo(@PathVariable("coursename") String courseName) {

		CourseInfoDto courseInfo = cs.getCourseInfo(courseName);

		if (courseInfo != null) {
			return new ResponseEntity<CourseInfoDto>(courseInfo, HttpStatus.OK);
		} else {
			throw new CustomException(CustomErrorCodes.COURSE_NOT_FOUND.getErrorMsg(),
					CustomErrorCodes.COURSE_NOT_FOUND.getErrorCode());
		}

	}

	@PostMapping("/uploadresume/{email}")
	public ResponseEntity<String> saveResume(@PathVariable("email") String userEmail,
			@RequestBody MultipartFile multipart) throws Exception {

		boolean saveResume = cs.saveResume(userEmail, multipart);

		if (saveResume) {
			return new ResponseEntity<String>("Resume Saved", HttpStatus.OK);
		}
		return new ResponseEntity<String>("Resume Not Saved", HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/{email}/resume")
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

	@DeleteMapping("/{email}/deleteresume")
	public ResponseEntity<String> deleteResume(@PathVariable("email") String userEmail) {

		boolean deleteResume = cs.deleteResume(userEmail);

		if (deleteResume) {
			return new ResponseEntity<String>("Resume Deletion Successfull", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Resume Deletion UnSuccessfull", HttpStatus.OK);
		}

	}

	@GetMapping("/{coursename}/{trainerName}/modules")
	public ResponseEntity<?> getModules(@PathVariable("coursename") String courseName,
			@PathVariable String trainerName) {

		List<CourseModules> courseModules = cs.getCourseModules(courseName, trainerName);

		if (courseModules.size() > 0) {
			return new ResponseEntity<List<CourseModules>>(courseModules, HttpStatus.OK);
		} else {
			return new ResponseEntity<List<CourseModules>>(courseModules, HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/{coursename}/{moduleid}/update")
	public ResponseEntity<List<CourseModules>> updateModules(@RequestBody ModuleUpdateDto mud,
			@PathVariable("coursename") String courseName, @PathVariable("moduleid") int modulenum) {

		HttpHeaders hd = new HttpHeaders();
		hd.setContentType(MediaType.APPLICATION_JSON);

		List<CourseModules> updateModule = cs.updateModule(courseName, modulenum, mud);

		return new ResponseEntity<List<CourseModules>>(updateModule, HttpStatus.OK);
	}

	@DeleteMapping("/{coursename}/{moduleid}/delete")
	public ResponseEntity<String> deleteModule(@PathVariable("coursename") String courseName,
			@PathVariable("moduleid") int modulenum) {
		boolean deleteModule = cs.deleteModule(courseName, modulenum);

		if (deleteModule) {
			return new ResponseEntity<String>("Module Deletion Successful", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Module Deletion UnSuccessful", HttpStatus.BAD_REQUEST);
		}
	}

}
