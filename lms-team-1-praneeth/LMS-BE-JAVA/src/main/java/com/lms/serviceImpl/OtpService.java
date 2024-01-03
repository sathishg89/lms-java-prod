package com.lms.serviceImpl;

import java.util.Random;

import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.Setter;

@Service
@Getter
@Setter
public class OtpService {

	public String generateOtp() {
		Random r = new Random();
		int randomnum = r.nextInt(10001, 99999);
		String output = Integer.toString(randomnum);
		return output;
	}

}
