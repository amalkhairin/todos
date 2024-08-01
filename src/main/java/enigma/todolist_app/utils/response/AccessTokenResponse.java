package enigma.todolist_app.utils.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class AccessTokenResponse {
    private String accessToken;
}
