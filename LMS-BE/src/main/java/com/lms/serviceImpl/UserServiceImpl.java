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
	public Optional<User> fingbyemail(String email) {

		Optional<User> findByemail;
		try {
			findByemail = ur.findByuserEmail(email);
			return findByemail;
		} catch (Exception e) {
			throw new EmailNotFoundException(CustomErrorCodes.INVALID_EMAIL.getErrorMsg(),
					CustomErrorCodes.INVALID_EMAIL.getErrorCode());
		}
	}

	@Override
	public User getUser(User lu) {

		Optional<User> findByemail = ur.findByuserEmail(lu.getUserEmail());
		if (findByemail.isEmpty()) {
			throw new EmailNotFoundException(CustomErrorCodes.INVALID_EMAIL.getErrorMsg(),
					CustomErrorCodes.INVALID_EMAIL.getErrorCode());
		} else {

			return findByemail.get();
		}
	}

	@Override
	public String saveImg(MultipartFile mp, String userEmail) throws Exception {

		User op = ur.findByuserEmail(userEmail)
				.orElseThrow(() -> new EmailNotFoundException(CustomErrorCodes.INVALID_EMAIL.getErrorMsg(),
						CustomErrorCodes.INVALID_EMAIL.getErrorCode()));
		try {
			op.setImg(mp.getBytes());
			ur.save(op);
			return "Image File Uploaded Successfully :" + mp.getOriginalFilename().toLowerCase();
		} catch (IOException e) {
			throw new CustomException(CustomErrorCodes.MISSING_IMAGE.getErrorMsg(),
					CustomErrorCodes.MISSING_IMAGE.getErrorCode());
		}

	}

	@Override
	public byte[] downloadImage(String email) throws IOException, DataFormatException {

		User img = ur.findByuserEmail(email)
				.orElseThrow(() -> new EmailNotFoundException(CustomErrorCodes.INVALID_EMAIL.getErrorMsg(),
						CustomErrorCodes.INVALID_EMAIL.getErrorCode()));

		if (img.getImg() != null) {
			return img.getImg();
		} else {
			return img.getImg();
		}

	}

	@Override
	public User userUpdate(User lu, String userEmail) {

		User lu1;
		if (lu.getUserEmail() == null && lu.getImg() == null && lu.getUserName() == null && lu.getPassword() == null) {
			throw new CustomException(CustomErrorCodes.INVALID_DETAILS.getErrorMsg(),
					CustomErrorCodes.INVALID_DETAILS.getErrorCode());

		} else {
			lu1 = ur.findByuserEmail(userEmail)
					.orElseThrow(() -> new EmailNotFoundException(CustomErrorCodes.INVALID_EMAIL.getErrorMsg(),
							CustomErrorCodes.INVALID_EMAIL.getErrorCode()));
		}

		if (lu.getUserEmail() != null && !lu.getUserEmail().isEmpty()) {
			lu1.setUserEmail(lu.getUserEmail());
		}
		if (lu.getPassword() != null && !lu.getPassword().isEmpty()) {
			lu1.setPassword(pe.encode(lu.getPassword()));
		}
		if (lu.getUserName() != null && !lu.getUserName().isEmpty()) {
			lu1.setUserName(lu.getUserName());
		}
		if (lu.getImg() != null && lu.getImg().length != 0) {
			lu1.setImg(lu.getImg());
		}

		return ur.save(lu1);

	}

	@Override
	public boolean saveotp(UserVerifyDto uvt) {

		if (!or.save(uvt).equals(null)) {
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
	public boolean resetPassword(String password, String verifypassword, long id) {

		User findById = ur.findById(id)
				.orElseThrow(() -> new CustomException(CustomErrorCodes.INVALID_DETAILS.getErrorMsg(),
						CustomErrorCodes.INVALID_DETAILS.getErrorCode()));
		if (password.equals(verifypassword)) {
			findById.setPassword(pe.encode(verifypassword));

			log.info("");
			return true;
		}
		return false;
	}

	@Override
	public boolean deleteUser(String UserEmail) {

		User findByuserEmail = ur.findByuserEmail(UserEmail).orElseThrow(null);

		if (findByuserEmail != null) {
			ur.delete(findByuserEmail);

			return true;
		} else {
			return false;
		}

	}

}
