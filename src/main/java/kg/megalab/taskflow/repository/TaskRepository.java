package kg.megalab.taskflow.repository;

import kg.megalab.taskflow.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    Task save(Task task);

    Optional<Task> findById(Long id);

    List<Task> findAll();

    boolean existsById(Long id);

    void deleteById(Long id);


}
