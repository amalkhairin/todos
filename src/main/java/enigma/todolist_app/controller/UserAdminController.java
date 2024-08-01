package enigma.todolist_app.controller;

import enigma.todolist_app.service.UserService;
import enigma.todolist_app.utils.dto.UserDTO;
import enigma.todolist_app.utils.response.Response;
import enigma.todolist_app.utils.response.UserPageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class UserAdminController {
    private final UserService userService;

    @Value("${superadmin.secret}")
    private String superAdminSecretKey;
    @Value("${admin.secret}")
    private String adminSecretKey;

    @PostMapping("/users")
    public ResponseEntity<?> createAdmin(@Valid @RequestBody UserDTO req) {
        System.out.println("aaaaaa");
        return Response.renderJSON(
                userService.create(req),
                "User created successfully",
                HttpStatus.CREATED
        );
    }

    @PostMapping("/super-admin")
    public ResponseEntity<?> createSuperAdmin(@Valid @RequestBody UserDTO req, @RequestHeader(value = "X-Super-Admin-Secret-Key") String key) {
        if (key == null || !key.equals(superAdminSecretKey)) {
            return Response.renderJSON(
                    null,
                    "Invalid Super Admin Secret Key",
                    HttpStatus.UNAUTHORIZED
            );
        }
        return Response.renderJSON(
                userService.createSuperAdmin(req),
                "User created successfully",
                HttpStatus.CREATED
        );
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return Response.renderJSON(
                new UserPageResponse<>(userService.getAllAdmin(pageable))
        );
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getOneUser(@PathVariable Long id) {
        return Response.renderJSON(
                userService.getOneAdmin(id)
        );
    }

    @PutMapping("/users/{id}/role")
    public ResponseEntity<?> updateRole(@PathVariable Long id, @Valid @RequestBody UserDTO req, @RequestHeader(value = "X-Admin-Secret-Key") String key) {
        if (key == null || !key.equals(adminSecretKey)) {
            return Response.renderJSON(
                    null,
                    "Invalid Admin Secret Key",
                    HttpStatus.UNAUTHORIZED
            );
        }
        return Response.renderJSON(
                userService.updateRoleAdmin(id, req)
        );
    }


}
