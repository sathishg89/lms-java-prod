package com.lms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lms.entity.User;
import com.lms.exception.details.NameFoundException;
import com.lms.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private UserService lus;

	/*
	 * 
	 * API takes the data from client and creates a account and store the data in Db
	 * 
	 */

	@PostMapping("/signup")
	@PreAuthorize("hasAuthority('superadmin')")
	public ResponseEntity<User> signUp(@RequestBody @Valid User lu) {

		User saveLU = lus.saveLU(lu);
		if (saveLU == null) {
			throw new NameFoundException();
		} else {
			return new ResponseEntity<User>(saveLU, HttpStatus.CREATED);
		}
	}

}
