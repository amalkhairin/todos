package enigma.todolist_app.utils.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RefreshTokenDTO {
    @NotBlank
    private String refreshToken;
}
