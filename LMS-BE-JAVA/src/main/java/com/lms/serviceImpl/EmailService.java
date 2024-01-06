package com.lms.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender jms;

	@Value("${myapp.url1}")
	private String url1;

	public void sendOtpEmail(String userEmail, String otp) throws MessagingException {
		MimeMessage mimeMessage = jms.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
		mimeMessageHelper.setTo(userEmail);
		mimeMessageHelper.setSubject("Verify OTP");

		mimeMessageHelper.setText("""
				<div>
				  <a href= "%s/user/verifyacc?email=%s&otp=%s" target="_blank">click link to verify</a>
				</div>
				""".formatted(url1, userEmail, otp), true);

		jms.send(mimeMessage);
	}

}
