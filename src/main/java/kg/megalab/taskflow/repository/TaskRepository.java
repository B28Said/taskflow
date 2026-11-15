
package kg.megalab.taskflow.repository;

import kg.megalab.taskflow.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
