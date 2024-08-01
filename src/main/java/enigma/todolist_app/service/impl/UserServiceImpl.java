package enigma.todolist_app.service.impl;

import enigma.todolist_app.model.Role;
import enigma.todolist_app.model.UserEntity;
import enigma.todolist_app.repository.UserRepository;
import enigma.todolist_app.service.UserService;
import enigma.todolist_app.utils.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserEntity create(UserDTO req) {
        System.out.println("bbbb");
        UserEntity user = UserEntity.builder()
                .username(req.getUsername())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(Role.ADMIN)
                .build();

        return userRepository.save(user);
    }

    @Override
    public UserEntity createSuperAdmin(UserDTO req) {
        UserEntity user = UserEntity.builder()
                .username(req.getUsername())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(Role.SUPER_ADMIN)
                .build();
        return userRepository.save(user);
    }

    @Override
    public Page<UserEntity> getAllAdmin(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public UserEntity getByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public UserEntity getByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public boolean isUserExistByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean isUserExistByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public UserEntity getOneAdmin(Long id) {
        return userRepository.findById(Math.toIntExact(id)).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public UserEntity updateRoleAdmin(Long id, UserDTO req) {
        UserEntity user = this.getOneAdmin(id);
        user.setRole(req.getRole());
        return userRepository.save(user);
    }
}
