package enigma.todolist_app.service.impl;

import enigma.todolist_app.model.Role;
import enigma.todolist_app.model.UserEntity;
import enigma.todolist_app.repository.UserRepository;
import enigma.todolist_app.security.JwtService;
import enigma.todolist_app.service.AuthService;
import enigma.todolist_app.utils.dto.AuthDTO;
import enigma.todolist_app.utils.dto.RefreshTokenDTO;
import enigma.todolist_app.utils.dto.RegisterDTO;
import enigma.todolist_app.utils.response.AccessTokenResponse;
import enigma.todolist_app.utils.response.AuthResponse;
import enigma.todolist_app.utils.response.RegisterResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public AuthResponse login(AuthDTO req) {
        try {
            UserEntity user = userRepository.findByEmail(req.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), req.getPassword())
            );

            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            String accessToken = jwtService.generateAccessToken(userDetails);
            String refreshToken = jwtService.generateRefreshToken(userDetails);
            return AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred during authentication");
        }
    }

    @Override
    public RegisterResponse register(RegisterDTO req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new RuntimeException("Username is already taken");
        }

        List<String> passwordErrors = validatePassword(req.getPassword());
        if (!passwordErrors.isEmpty()) {
            throw new IllegalArgumentException("Password does not meet the requirements : " + String.join(", ", passwordErrors));
        }

        Role role = Role.USER;

        UserEntity user = userRepository.save(UserEntity.builder()
                .username(req.getUsername())
                .email(req.getEmail())
                .role(role)
                .password(passwordEncoder.encode(req.getPassword()))
                .build());

        UserDetails userDetails = User.withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(role.name())
                .build();

        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return RegisterResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    @Override
    public AccessTokenResponse refreshToken(RefreshTokenDTO req) {
        if (jwtService.validateRefreshToken(req.getRefreshToken())) {
            String username = jwtService.extractUsername(req.getRefreshToken());
            UserDetails userDetails = userRepository.findByUsername(username)
                    .map(user -> User.withUsername(user.getUsername())
                            .password(user.getPassword())
                            .authorities(user.getRole().name())
                            .build())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            String accessToken = jwtService.generateAccessToken(userDetails);
            return AccessTokenResponse.builder()
                    .accessToken(accessToken)
                    .build();
        } else {
            throw new RuntimeException("Invalid refresh token");
        }
    }

    private List<String> validatePassword(String password) {
        List<String> errors = new ArrayList<>();

        if (password.length() < 8) {
            errors.add("Password must be at least " + 8 + " characters long");
        }
//        if (!password.matches(".*[A-Z].*")) {
//            errors.add("Password must contain at least one uppercase letter");
//        }
//        if (!password.matches(".*[a-z].*")) {
//            errors.add("Password must contain at least one lowercase letter");
//        }
//        if (!password.matches(".*\\d.*")) {
//            errors.add("Password must contain at least one digit");
//        }
//        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
//            errors.add("Password must contain at least one special character");
//        }

        return errors;
    }
}
