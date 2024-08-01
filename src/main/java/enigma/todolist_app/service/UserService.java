package enigma.todolist_app.service;

import enigma.todolist_app.model.UserEntity;
import enigma.todolist_app.utils.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserEntity create(UserDTO req);
    UserEntity createSuperAdmin(UserDTO req);
    Page<UserEntity> getAllAdmin(Pageable pageable);
    UserEntity getByUsername(String username);
    UserEntity getByEmail(String email);
    boolean isUserExistByUsername(String username);
    boolean isUserExistByEmail(String email);
    UserEntity getOneAdmin(Long id);
    UserEntity updateRoleAdmin(Long id, UserDTO req);
}
