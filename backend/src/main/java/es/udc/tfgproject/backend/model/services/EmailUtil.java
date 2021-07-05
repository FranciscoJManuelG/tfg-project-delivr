package es.udc.tfgproject.backend.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailUtil {

	@Autowired
	private JavaMailSender mailSender;

	public void sendMail(String to, String subject, String content) {
		SimpleMailMessage email = new SimpleMailMessage();

		email.setTo(to);
		email.setSubject(subject);
		email.setText(content);

		mailSender.send(email);
	}

}
