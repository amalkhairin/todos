package enigma.todolist_app.service;

import enigma.todolist_app.utils.dto.AuthDTO;
import enigma.todolist_app.utils.dto.RefreshTokenDTO;
import enigma.todolist_app.utils.dto.RegisterDTO;
import enigma.todolist_app.utils.response.AccessTokenResponse;
import enigma.todolist_app.utils.response.AuthResponse;
import enigma.todolist_app.utils.response.RegisterResponse;

public interface AuthService {
    AuthResponse login(AuthDTO req);
    RegisterResponse register(RegisterDTO req);
    AccessTokenResponse refreshToken(RefreshTokenDTO req);
}
