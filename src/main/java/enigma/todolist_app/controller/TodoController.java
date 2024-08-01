package enigma.todolist_app.controller;

import enigma.todolist_app.service.TodoItemService;
import enigma.todolist_app.utils.dto.TodoItemDTO;
import enigma.todolist_app.utils.response.Response;
import enigma.todolist_app.utils.response.TodoPageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class TodoController {
    private final TodoItemService todoItemService;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody TodoItemDTO req, Authentication auth) {
        return Response.renderJSON(
                todoItemService.create(req, auth),
                "Todo item created successfully",
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String order,
            Authentication authentication
    ) {
        return Response.renderJSON(
                new TodoPageResponse<>(todoItemService.getAll(pageable, status, sortBy, order, authentication))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Long id) {
        return Response.renderJSON(
                todoItemService.getOne(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody TodoItemDTO req) {
        return Response.renderJSON(
                todoItemService.update(id, req)
        );
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @Valid @RequestBody TodoItemDTO req) {
        return Response.renderJSON(
                todoItemService.update(id, req)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOne(@PathVariable Long id) {
        todoItemService.deleteOne(id);
        return Response.renderJSON(
                null,
                "Todo item deleted successfully",
                HttpStatus.OK
        );
    }
}
