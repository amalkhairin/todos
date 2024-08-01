package enigma.todolist_app.controller;

import enigma.todolist_app.service.AuthService;
import enigma.todolist_app.utils.dto.AuthDTO;
import enigma.todolist_app.utils.dto.RefreshTokenDTO;
import enigma.todolist_app.utils.dto.RegisterDTO;
import enigma.todolist_app.utils.response.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthDTO req) {
        return Response.renderJSON(
                authService.login(req)
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenDTO req) {
        return Response.renderJSON(
                authService.refreshToken(req)
        );
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDTO req) {
        return Response.renderJSON(
                authService.register(req)
        );
    }
}
