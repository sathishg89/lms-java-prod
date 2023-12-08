package com.lms.service;

import org.springframework.web.multipart.MultipartFile;

import com.lms.entity.User;

public interface AdminService {

	public User saveUser(User lu);

	boolean userImport(MultipartFile mp) throws Exception;
	
}
