package kg.megalab.taskflow.service;

import kg.megalab.taskflow.model.Task;
import kg.megalab.taskflow.model.TaskStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {

    private final List<Task> tasks = new ArrayList<>();
    private long nextId = 1L;


    public List<Task> findAll() {
        return tasks;
    }

    public Task create(Task task) {
        task.setId(nextId++);
        task.setStatus(TaskStatus.NEW);
        task.setCreatedAt(LocalDateTime.now());
        tasks.add(task);

        return task;
    }
}
