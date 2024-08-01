package enigma.todolist_app.utils.dto;


import enigma.todolist_app.model.TodoStatus;
import lombok.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class TodoItemDTO {
    private Long id;
    private String title;
    private String description;
    private TodoStatus status;
    private LocalDate dueDate;
    private LocalDate createdAt;
}
