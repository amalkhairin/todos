package enigma.todolist_app.utils.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class AuthDTO {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String password;
}
