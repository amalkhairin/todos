package enigma.todolist_app.security;

import enigma.todolist_app.model.UserEntity;
import enigma.todolist_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserSecurity {
    private final UserRepository userRepository;

    public boolean hasUserId(Authentication authentication, Long userId) {
        UserEntity user = userRepository.findByUsername(authentication.getName()).orElse(null);
        Long currentUserId = user.getId();
        return currentUserId.equals(userId);
    }

    public boolean isUser(Authentication authentication, int userId) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        Object principal = authentication.getPrincipal();
        System.out.println(principal instanceof UserEntity);

        if (!(principal instanceof User)) {
            return false;
        }
        UserEntity user = (UserEntity) principal;
        return user.getId() == userId;
    }
}
