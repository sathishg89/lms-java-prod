package com.lms.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lms.constants.CustomErrorCodes;
import com.lms.entity.User;
import com.lms.exception.details.EmailNotFoundException;
import com.lms.repository.UserRepo;

@Service
public class UserUserdetailsService implements UserDetailsService {

	@Autowired
	private UserRepo ur;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Optional<User> findByemail = ur.findByuserEmail(username);

		return findByemail.map(details -> new UserUserDetails(details))
				.orElseThrow(() -> new EmailNotFoundException(CustomErrorCodes.MISSING_EMAIL_ID.getErrorMsg(),
						CustomErrorCodes.MISSING_EMAIL_ID.getErrorCode()));
	}

}
