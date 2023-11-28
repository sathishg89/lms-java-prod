package com.lms.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.zip.DataFormatException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.lms.dto.UserVerifyDto;
import com.lms.entity.Courses;
import com.lms.entity.User;
import com.lms.entity.UserCourse;

public interface UserService {

	public User saveLU(User lu);

	public List<User> getLU(long id);

	public User updateLU(User lu);

	public void deleteLU(long id);

	Boolean getByemail(User lu);

	public ResponseEntity<?> getby(User lu);

	public String saveImg(MultipartFile file, String name) throws Exception;

	byte[] downloadImage(String email) throws IOException, DataFormatException;

	Optional<User> fingbyemail(String email);

	public User Luupdate(User lu1);

	boolean verifyAccount(String email, String otp);

	boolean saveotp(UserVerifyDto uvt);

	boolean resetPassword(String password, String verifypassword, long id);

	boolean saveUserCourse(UserCourse uc);

	boolean saveCourses(Courses cc);

	boolean accessTocoures(String name, String cname);

	UserCourse getUserCourses(String name);

}
