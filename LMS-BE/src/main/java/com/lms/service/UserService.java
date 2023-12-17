package com.lms.service;

import java.io.IOException;
import java.util.Optional;
import java.util.zip.DataFormatException;

import org.springframework.web.multipart.MultipartFile;

import com.lms.dto.UserVerifyDto;
import com.lms.entity.User;

public interface UserService {

	public User getUser(User user);

	public String saveImg(MultipartFile multiPartFile, String userEmail) throws Exception;

	byte[] downloadImage(String userEmail) throws IOException, DataFormatException;

	Optional<User> fingbyemail(String userEmail);

	public User userUpdate(User user, String userEmail);

	boolean verifyAccount(String userEmail, String otp);

	boolean saveotp(UserVerifyDto uvt);

	boolean resetPassword(String password, String verifypassword, long userId);

	boolean deleteUser(String email);


}
