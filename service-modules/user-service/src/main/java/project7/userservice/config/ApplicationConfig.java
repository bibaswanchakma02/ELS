package project7.userservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import project7.userservice.model.UserModel;
import project7.userservice.model.UserRoles;
import project7.userservice.repository.UserRepository;
import project7.userservice.repository.UserRolesRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {


    private  UserRepository userRepository;
    private  UserRolesRepository userRolesRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            UserModel user = userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("User not found"));

            Optional<UserRoles> roles = userRolesRepository.findByUserId(user.getUserId());
            Collection<? extends GrantedAuthority> authorities = roles.stream()
                    .map(role->new SimpleGrantedAuthority(role.getRole().name()))
                    .collect(Collectors.toList());

            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    authorities
            );
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();

    }
}
