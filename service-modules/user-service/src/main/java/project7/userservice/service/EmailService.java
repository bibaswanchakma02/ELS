package project7.userservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project7.userservice.model.UserModel;
import project7.userservice.repository.UserRepository;

import java.util.Optional;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;

    public void sendEmail(String toEmail, String verificationLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Email verification - Complete your registration");
        message.setText("Please verify your email by clicking on this link: " + verificationLink);

        mailSender.send(message);
    }

    @Transactional
    public String verifyEmail(String token) {
        Optional<UserModel> userOptional = userRepository.findByVerificationToken(token);

        if(userOptional.isEmpty()){
            throw new RuntimeException("Invalid verification token");
        }

        UserModel user = userOptional.get();

        user.setEmailVerified(true);
        user.setVerificationToken(null);

        userRepository.save(user);

        return "Email verified successfully";
    }
}
