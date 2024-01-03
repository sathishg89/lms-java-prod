package com.lms.service;

import java.io.IOException;
import java.util.Optional;
import java.util.zip.DataFormatException;

import org.springframework.web.multipart.MultipartFile;

import com.lms.dto.UserUpdateDto;
import com.lms.dto.UserVerifyDto;
import com.lms.entity.User;

public interface UserService {

	Optional<User> fingbyemail(String userEmail);

	public String saveProfilePhoto(MultipartFile multiPartFile, String userEmail) throws Exception;

	byte[] getProfilePhoto(String userEmail) throws IOException, DataFormatException;

	public User userUpdate(UserUpdateDto user, String userEmail) throws Exception;

	boolean verifyAccount(String userEmail, String otp);

	boolean saveotp(UserVerifyDto uvt);

	boolean resetPassword(String password, String verifypassword, long userId);

	boolean deleteUser(String email);

}
