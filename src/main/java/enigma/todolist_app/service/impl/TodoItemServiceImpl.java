package enigma.todolist_app.service.impl;

import enigma.todolist_app.model.TodoItem;
import enigma.todolist_app.model.TodoStatus;
import enigma.todolist_app.model.UserEntity;
import enigma.todolist_app.repository.TodoItemRepository;
import enigma.todolist_app.repository.UserRepository;
import enigma.todolist_app.service.TodoItemService;
import enigma.todolist_app.service.UserService;
import enigma.todolist_app.utils.dto.TodoItemDTO;
import enigma.todolist_app.utils.specification.TodoSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TodoItemServiceImpl implements TodoItemService {
    private final TodoItemRepository todoItemRepository;
    private final UserService userService;

    @Override
    public TodoItem create(TodoItemDTO req, Authentication auth) {
        System.out.println(req.getDueDate());
        UserEntity user = userService.getByUsername(auth.getName());
        TodoItem todo = TodoItem.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .status(TodoStatus.PENDING)
                .dueDate(req.getDueDate())
                .user(user)
                .build();
        return todoItemRepository.save(todo);
    }

    @Override
    public Page<TodoItem> getAll(Pageable pageable, String status, String sortBy, String order, Authentication auth) {
        UserEntity user = userService.getByUsername(auth.getName());
        if (auth.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN") || r.getAuthority().equals("ROLE_SUPER_ADMIN"))) {
            Specification<TodoItem> spec = TodoSpecification.getSpecification(status, sortBy, order, null);
            return todoItemRepository.findAll(spec, pageable);
        } else {
            Specification<TodoItem> spec = TodoSpecification.getSpecification(status, sortBy, order, user.getId());
            return todoItemRepository.findAll(spec, pageable);
        }

    }

    @Override
    public TodoItem getOne(Long id, Authentication auth) {
        TodoItem todo = todoItemRepository.findById(id).orElseThrow(() -> new RuntimeException("Todo item not found"));
        if (auth.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN") || r.getAuthority().equals("ROLE_SUPER_ADMIN"))) {
            return todo;
        } else {
            UserEntity user = userService.getByUsername(auth.getName());
            if (todo.getUser().getId().equals(user.getId())) {
                return todo;
            } else {
                throw new RuntimeException("Todo item not found");
            }
        }

    }

    @Override
    public TodoItem update(Long id, TodoItemDTO req, Authentication auth) {
        TodoItem todo = this.getOne(id, auth);
        todo.setTitle(req.getTitle() != null ? req.getTitle() : todo.getTitle());
        todo.setDescription(req.getDescription() != null ? req.getDescription() : todo.getDescription());
        todo.setStatus(req.getStatus() != null ? req.getStatus() : todo.getStatus());
        return todoItemRepository.save(todo);
    }

    @Override
    public void deleteOne(Long id, Authentication auth) {
        TodoItem todo = this.getOne(id, auth);
        todoItemRepository.delete(todo);
    }
}
