
package com.lms.serviceImpl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lms.constants.CustomErrorCodes;
import com.lms.entity.User;
import com.lms.exception.details.CustomException;
import com.lms.repository.UserRepo;
import com.lms.service.AdminService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AdminServiceImpl implements AdminService {

	@Autowired
	private UserRepo ur;

	@Autowired
	private PasswordEncoder pe;

	@Override
	public User saveUser(User lu) {
		return null;
	}

	@Override
	public boolean userImport(MultipartFile mp) throws Exception {

		Iterable<CSVRecord> records;
		Iterator<CSVRecord> iterator;
		BufferedReader reader = new BufferedReader(new InputStreamReader(mp.getInputStream()));

		records = CSVFormat.DEFAULT.parse(reader);
		iterator = records.iterator();
		if (iterator.hasNext()) {
			iterator.next();
		}

		log.info("");
		List<User> userlist = new LinkedList<>();

		for (CSVRecord record : records) {
			String userName = record.get(0);
			String userEmail = record.get(1);

			boolean existsByemail = ur.existsByemail(userEmail);
			boolean isValidEmail = userEmail.matches("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$");

			if (!existsByemail) {

				if (isValidEmail & !userName.equals(null)) {
					User user = User.builder().name(userName).email(userEmail).password(pe.encode("welcome@123"))
							.isActive(true).roles("user").build();

					userlist.add(user);

				} else {
					throw new CustomException(CustomErrorCodes.MISSING_USER_NAME.getErrorMsg(),
							CustomErrorCodes.INVALID_EMAIL.getErrorMsg());
				}

			} else {
				throw new CustomException(CustomErrorCodes.EMAIL_ALREADY_EXIST.getErrorMsg(),
						CustomErrorCodes.EMAIL_ALREADY_EXIST.getErrorCode());
			}

		}

		if (!userlist.isEmpty()) {
			ur.saveAll(userlist);

			return true;
		}

		return false;
	}

}
