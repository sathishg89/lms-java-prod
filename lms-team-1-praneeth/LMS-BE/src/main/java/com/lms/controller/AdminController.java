package com.lms.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lms.constants.CustomErrorCodes;
import com.lms.dto.UserUpdateDto;
import com.lms.entity.CourseUsers;
import com.lms.entity.User;
import com.lms.exception.details.NameFoundException;
import com.lms.service.AdminService;
import com.lms.service.CourseService;
import com.lms.service.UserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private AdminService as;

	@Autowired
	private UserService us;

	@Autowired
	private CourseService cs;

	/*
	 * 
	 * API fetches the swagger document
	 * 
	 */

	@GetMapping("/getallapi")
	public void redirectToSwagger(HttpServletResponse response) throws IOException {
		response.sendRedirect("/swagger-ui/index.html#/");
	}

	/*
	 * 
	 * API takes the data from client and creates a account and store the data in Db
	 * 
	 */

	@PostMapping("/signup")
	@PreAuthorize("hasAuthority('admin')")
	public ResponseEntity<User> signUp(@RequestBody @Valid User user) {

		User saveLU = as.saveUser(user);
		if (saveLU == null) {
			throw new NameFoundException(CustomErrorCodes.USER_ALREADY_EXIST.getErrorMsg());
		} else {
			return new ResponseEntity<User>(saveLU, HttpStatus.CREATED);
		}
	}

	/*
	 * 
	 * API used to import users by admin
	 * 
	 */

	@PostMapping("/importusers")
	@PreAuthorize("hasAuthority('admin')")
	public ResponseEntity<String> signUpcsv(@RequestParam("file") MultipartFile csvFile) throws Exception {
		boolean userImport = as.userImport(csvFile);
		if (userImport) {
			return new ResponseEntity<String>("User Imported  Successfull", HttpStatus.OK);
		}
		return new ResponseEntity<String>("Error In Importing Users", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/*
	 * 
	 * API used to update users by admin
	 * 
	 */

	@PutMapping("/userupdate/{userEmail}")
	@PreAuthorize("hasAuthority('admin')")
	public ResponseEntity<User> UserUpdate(@ModelAttribute UserUpdateDto user,
			@PathVariable("userEmail") String UserEmail) throws Exception {
		User luupdate = us.userUpdate(user, UserEmail);

		CourseUsers cs1 = CourseUsers.builder().userEmail(user.getUserEmail()).userName(user.getUserName()).build();

		boolean updateCourseUser = cs.updateCourseUser(cs1, UserEmail);

		if (luupdate == null && !updateCourseUser) {
			return new ResponseEntity<User>(luupdate, HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<User>(luupdate, HttpStatus.OK);
		}
	}

	/*
	 * 
	 * API used to delete user by admin
	 * 
	 */

	@DeleteMapping("/userdelete/{userEmail}")
	@PreAuthorize("hasAuthority('admin')")
	public ResponseEntity<String> deleteUser(@PathVariable("userEmail") String userEmail) {

		boolean deleteUser = us.deleteUser(userEmail);
		boolean deleterCourseUser = cs.deleterCourseUser(userEmail);

		if (deleterCourseUser && deleteUser) {
			return new ResponseEntity<String>("User Deletion Successfully", HttpStatus.OK);

		} else {
			return new ResponseEntity<String>("User Deletion UnSuccessfully", HttpStatus.BAD_REQUEST);
		}
	}

	/*
	 * 
	 * API used to remove access for course to user by admin
	 * 
	 */

	@PatchMapping("/removecourseaccess/{userEmail}/{courseName}/{courseTrainer}")
	@PreAuthorize("hasAuthority('admin')")
	public ResponseEntity<String> removeCourseAccess(@PathVariable("userEmail") String userEmail,
			@PathVariable("courseName") String courseName, @PathVariable("courseTrainer") String courseTrainer) {
		boolean removeCourseAccess = cs.removeCourseAccess(userEmail, courseName, courseTrainer);
		if (removeCourseAccess) {
			return new ResponseEntity<String>("Course Access Removal  Successful", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Course Access Removal  UnSuccessful", HttpStatus.BAD_REQUEST);
		}
	}

}