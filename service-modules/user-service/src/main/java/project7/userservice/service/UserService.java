package project7.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project7.common.sharedServices.RegisterService;
import project7.userservice.dto.UserRegistrationDto;
import project7.userservice.model.UserModel;
import project7.userservice.model.UserRoles;
import project7.userservice.repository.UserRepository;
import project7.userservice.repository.UserRolesRepository;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements RegisterService<UserRegistrationDto, String> {


    private final UserRepository userRepository;
    private final UserRolesRepository userRolesRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Transactional
    public String register(UserRegistrationDto userRegistrationDto) {
        Optional<UserModel> userOptional = userRepository.findByUsername(userRegistrationDto.getUsername());

        if(userOptional.isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        Date now = new Date();
        String verificationCode = UUID.randomUUID().toString();

        var user = UserModel.builder()
                .userId(UUID.randomUUID())
                .username(userRegistrationDto.getUsername())
                .password(passwordEncoder.encode(userRegistrationDto.getPassword()))
                .email(userRegistrationDto.getEmail())
                .firstName(userRegistrationDto.getFirstName())
                .lastName(userRegistrationDto.getLastName())
                .profileImage(userRegistrationDto.getProfileImage())
                .createdAt(now)
                .isEmailVerified(false)
                .verificationToken(verificationCode)
                .build();
        userRepository.save(user);


        var userRoles = UserRoles.builder()
                .id(UUID.randomUUID())
                .userId(user.getUserId())
                .role(userRegistrationDto.getRole())
                .build();

        userRolesRepository.save(userRoles);

        String verificationLink = "localhost:8080/api/user/verify-email?token=" + verificationCode;
        emailService.sendEmail(user.getEmail(), verificationLink);


        return "User successfully registered! A verification link has been sent to your email." +
                " Please click on the link to verify your account";

    }

}
