package enigma.todolist_app.repository;

import enigma.todolist_app.model.TodoItem;
import enigma.todolist_app.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TodoItemRepository extends JpaRepository<TodoItem, Long>, JpaSpecificationExecutor<TodoItem> {
    Optional<TodoItem> findByUserAndId(UserEntity user, Long id);
}
