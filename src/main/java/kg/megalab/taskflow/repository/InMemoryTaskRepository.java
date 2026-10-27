package kg.megalab.taskflow.repository;

import kg.megalab.taskflow.model.Task;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryTaskRepository implements TaskRepository {

    private final List<Task> tasks = new ArrayList<>();
    private long nextId = 1L;

    @Override
    public Task save(Task task) {
        if (task.getId() == null) {
            task.setId(nextId++);
            tasks.add(task);
        }

        return task;
    }

    @Override
    public Optional<Task> findById(Long id) {
        return tasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Task> findAll() {
        return new ArrayList<>(tasks);
    }

    @Override
    public boolean existsById(Long id) {
        return tasks.stream()
                .anyMatch(task -> task.getId().equals(id));
    }

    @Override
    public void deleteById(Long id) {
        tasks.removeIf(task -> task.getId().equals(id));
    }
}
