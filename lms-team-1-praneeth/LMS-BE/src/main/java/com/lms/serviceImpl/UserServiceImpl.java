package com.lms.serviceImpl;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.zip.DataFormatException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lms.constants.CustomErrorCodes;
import com.lms.dto.UserUpdateDto;
import com.lms.dto.UserVerifyDto;
import com.lms.entity.User;
import com.lms.exception.details.CustomException;
import com.lms.exception.details.EmailNotFoundException;
import com.lms.repository.OtpRepo;
import com.lms.repository.UserRepo;
import com.lms.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepo ur;

	@Autowired
	private PasswordEncoder pe;

	@Autowired
	private OtpRepo or;

	@Override
	public Optional<User> fingbyemail(String userEmail) {

		Optional<User> findByemail;
		try {
			findByemail = ur.findByuserEmail(userEmail);
			return findByemail;
		} catch (Exception e) {
			throw new EmailNotFoundException(CustomErrorCodes.INVALID_EMAIL.getErrorMsg(),
					CustomErrorCodes.INVALID_EMAIL.getErrorCode());
		}
	}

	@Override
	public String saveProfilePhoto(MultipartFile photo, String userEmail) throws Exception {

		User op = ur.findByuserEmail(userEmail)
				.orElseThrow(() -> new EmailNotFoundException(CustomErrorCodes.INVALID_EMAIL.getErrorMsg(),
						CustomErrorCodes.INVALID_EMAIL.getErrorCode()));
		try {
			op.setProfilePhoto(photo.getBytes());
			ur.save(op);
			return "Image File Uploaded Successfully :" + photo.getOriginalFilename().toLowerCase();
		} catch (IOException e) {
			throw new CustomException(CustomErrorCodes.MISSING_IMAGE.getErrorMsg(),
					CustomErrorCodes.MISSING_IMAGE.getErrorCode());
		}

	}

	@Override
	public byte[] getProfilePhoto(String userEmail) throws IOException, DataFormatException {

		User img = ur.findByuserEmail(userEmail)
				.orElseThrow(() -> new EmailNotFoundException(CustomErrorCodes.INVALID_EMAIL.getErrorMsg(),
						CustomErrorCodes.INVALID_EMAIL.getErrorCode()));

		if (img.getProfilePhoto() != null) {
			return img.getProfilePhoto();
		} else {
			return img.getProfilePhoto();
		}

	}

	@Override
	public User userUpdate(UserUpdateDto userUpdateDto, String userEmail) throws Exception {

		User lu1;
		if (userUpdateDto.getUserEmail() == null && userUpdateDto.getProfilePhoto() == null
				&& userUpdateDto.getUserName() == null && userUpdateDto.getPassword() == null) {
			throw new CustomException(CustomErrorCodes.INVALID_DETAILS.getErrorMsg(),
					CustomErrorCodes.INVALID_DETAILS.getErrorCode());

		} else {
			lu1 = ur.findByuserEmail(userEmail)
					.orElseThrow(() -> new EmailNotFoundException(CustomErrorCodes.INVALID_EMAIL.getErrorMsg(),
							CustomErrorCodes.INVALID_EMAIL.getErrorCode()));
		}

		if (userUpdateDto.getUserEmail() != null && !userUpdateDto.getUserEmail().isEmpty()) {
			lu1.setUserEmail(userUpdateDto.getUserEmail());
		}
		if (userUpdateDto.getPassword() != null && !userUpdateDto.getPassword().isEmpty()) {
			lu1.setPassword(pe.encode(userUpdateDto.getPassword()));
		}
		if (userUpdateDto.getUserName() != null && !userUpdateDto.getUserName().isEmpty()) {
			lu1.setUserName(userUpdateDto.getUserName());
		}
		if (userUpdateDto.getProfilePhoto() != null && !userUpdateDto.getProfilePhoto().isEmpty()) {
			lu1.setProfilePhoto(userUpdateDto.getProfilePhoto().getBytes());
		}

		return ur.save(lu1);

	}

	@Override
	public boolean saveotp(UserVerifyDto userVerifyDto) {

		if (!or.save(userVerifyDto).equals(null)) {
			return true;
		} else {
			return false;
		}

	}

	@Override
	public boolean verifyAccount(String userEmail, String otp) {

		Optional<UserVerifyDto> findByemail;
		try {
			findByemail = or.findByuserEmail(userEmail);

			if (findByemail.get().getOtp().equals(otp) && Duration
					.between(findByemail.get().getOtpGeneratedTime(), LocalDateTime.now()).getSeconds() < (1 * 60)) {

				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw new EmailNotFoundException(CustomErrorCodes.INVALID_EMAIL.getErrorMsg(),
					CustomErrorCodes.INVALID_EMAIL.getErrorCode());
		}
	}

	@Override
	public boolean resetPassword(String password, String verifyPassword, long userId) {

		User findById = ur.findById(userId)
				.orElseThrow(() -> new CustomException(CustomErrorCodes.INVALID_DETAILS.getErrorMsg(),
						CustomErrorCodes.INVALID_DETAILS.getErrorCode()));
		if (password.equals(verifyPassword)) {
			findById.setPassword(pe.encode(verifyPassword));

			log.info("");
			return true;
		}
		return false;
	}

	@Override
	public boolean deleteUser(String UserEmail) {

		User findByuserEmail = ur.findByuserEmail(UserEmail)
				.orElseThrow(() -> new CustomException(CustomErrorCodes.INVALID_EMAIL.getErrorMsg(),
						CustomErrorCodes.INVALID_EMAIL.getErrorCode()));

		if (findByuserEmail != null) {
			ur.delete(findByuserEmail);

			return true;
		} else {
			return false;
		}

	}

}
