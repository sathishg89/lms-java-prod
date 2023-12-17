package com.lms.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.lms.entity.User;

public interface AdminService {

	public User saveUser(User user);

	boolean userImport(MultipartFile multipartfile) throws Exception;

	public void deleteLU(long userId);

	List<User> gestLU(long userId);

}
