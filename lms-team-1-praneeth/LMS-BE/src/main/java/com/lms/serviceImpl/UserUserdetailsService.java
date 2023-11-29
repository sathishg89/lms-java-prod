package com.lms.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lms.entity.User;
import com.lms.exception.details.EmailNotFoundException;
import com.lms.repository.UserRepo;

@Service
public class UserUserdetailsService implements UserDetailsService {

	@Autowired
	private UserRepo lur;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Optional<User> findByemail = lur.findByemail(username);

		return findByemail.map(details -> new UserUserDetails(details)).orElseThrow(() -> new EmailNotFoundException("Email Not Found"));
	}

}
