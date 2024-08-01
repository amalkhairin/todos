package enigma.todolist_app.utils.specification;

import enigma.todolist_app.model.TodoItem;
import enigma.todolist_app.model.UserEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TodoSpecification {
    public static Specification<TodoItem> getSpecification(String status, String sortBy, String order, Long userId) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (status != null && !status.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            if (userId != null) {
                predicates.add(criteriaBuilder.equal(root.get("user").get("id"), userId));
            }

            query.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));

            if (sortBy != null && !sortBy.isEmpty()) {
                if (order != null && order.equalsIgnoreCase("desc")) {
                    query.orderBy(criteriaBuilder.desc(root.get(sortBy)));
                } else {
                    query.orderBy(criteriaBuilder.asc(root.get(sortBy)));
                }
            }

            return query.getRestriction();
        };
    }
}
