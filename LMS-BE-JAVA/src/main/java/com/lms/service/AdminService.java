package com.lms.service;

import org.springframework.web.multipart.MultipartFile;

import com.lms.entity.User;

public interface AdminService {

	public User saveUser(User user);

	boolean userImport(MultipartFile multipartfile) throws Exception;

}
