package kg.megalab.taskflow.repository;

import kg.megalab.taskflow.entity.Task;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryTaskRepository implements TaskRepository {


    private final Map<Long, Task> tasks = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);


    @Override
    public Task save(Task task) {
        if (task.getId() == null) {
            task.setId(nextId.getAndIncrement());
        }
        tasks.put(task.getId(), task);

        return task;
    }

    @Override
    public Optional<Task> findById(Long id) {
        return Optional.ofNullable(tasks.get(id));

    }

    @Override
    public List<Task> findAll() {
        return new ArrayList<>(tasks.values());

    }

    @Override
    public boolean existsById(Long id) {
        return tasks.containsKey(id);
    }

    @Override
    public void deleteById(Long id) {
        tasks.remove(id);
    }
}
