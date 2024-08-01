package enigma.todolist_app.service;

import enigma.todolist_app.model.TodoItem;
import enigma.todolist_app.utils.dto.TodoItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface TodoItemService {
    TodoItem create(TodoItemDTO req, Authentication authentication);
    Page<TodoItem> getAll(Pageable pageable, String status, String sortBy, String order, Authentication authentication);
    TodoItem getOne(Long id);
    TodoItem update(Long id, TodoItemDTO req);
    void deleteOne(Long id);


}
