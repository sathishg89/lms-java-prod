package com.lms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.lms.entity.User;
import com.lms.exception.details.NameFoundException;
import com.lms.service.AdminService;
import com.lms.service.CourseService;
import com.lms.service.UserService;

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
	 * API takes the data from client and creates a account and store the data in Db
	 * 
	 */

	@PostMapping("/signup")
	@PreAuthorize("hasAuthority('superadmin')")
	public ResponseEntity<User> signUp(@RequestBody @Valid User user) {

		User saveLU = as.saveUser(user);
		if (saveLU == null) {
			throw new NameFoundException(CustomErrorCodes.USER_ALREADY_EXIST.getErrorMsg());
		} else {
			return new ResponseEntity<User>(saveLU, HttpStatus.CREATED);
		}
	}

	@PostMapping("/importusers")
	@PreAuthorize("hasAuthority('admin')")
	public ResponseEntity<String> signUpcsv(@RequestParam("file") MultipartFile multipartfile) throws Exception {
		boolean userImport = as.userImport(multipartfile);
		if (userImport) {
			return new ResponseEntity<String>("User Imported", HttpStatus.OK);
		}
		return new ResponseEntity<String>("Error In Importing Users", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PutMapping("/update")
	public ResponseEntity<String> UserUpdate(@RequestBody User user, @RequestParam String UserEmail) {
		User luupdate = us.userUpdate(user, UserEmail);
		if (luupdate == null) {
			return new ResponseEntity<String>("User details updated", HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<String>("User details updated", HttpStatus.OK);
		}
	}

	@DeleteMapping("/delete/{email}")
	public ResponseEntity<String> deleteUser(@PathVariable("email") String userEmail) {

		boolean deleteUser = us.deleteUser(userEmail);
		boolean deleterCourseUser = cs.deleterCourseUser(userEmail);

		if (deleterCourseUser && deleteUser) {
			return new ResponseEntity<String>("User Deleted Successfully", HttpStatus.OK);

		} else {
			return new ResponseEntity<String>("User Deletion UnSuccessfully", HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping("/removecourseaccess")
	public ResponseEntity<String> removeCourseAccess(@RequestParam String userEmail, @RequestParam String courseName,
			@RequestParam String trainerName) {
		boolean removeCourseAccess = cs.removeCourseAccess(userEmail, courseName, trainerName);
		if (removeCourseAccess) {
			return new ResponseEntity<String>("Access Removed", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Unable To Access Removed", HttpStatus.BAD_REQUEST);
		}
	}

	

}
