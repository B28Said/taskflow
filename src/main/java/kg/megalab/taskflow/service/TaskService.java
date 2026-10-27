package kg.megalab.taskflow.service;

import kg.megalab.taskflow.model.Task;
import kg.megalab.taskflow.model.TaskStatus;
import kg.megalab.taskflow.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }



    public List<Task> findAll(TaskStatus status, String search) {
        return taskRepository.findAll()
                .stream()
                .filter(task -> status == null
                        || task.getStatus() == status)
                .filter(task -> search == null
                        || search.isBlank()
                        || (task.getTitle() != null
                        && task.getTitle().toLowerCase()
                        .contains(search.toLowerCase())))
                .sorted(Comparator.comparing(Task::getCreatedAt).reversed())
                .toList();
    }


    public Task create(Task task) {
        task.setStatus(TaskStatus.NEW);
        task.setCreatedAt(LocalDateTime.now());
        return taskRepository.save(task);
    }

    public Task findById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }


    public Task update(Long id, Task changes){
        Task task = findById(id);
        task.setTitle(changes.getTitle());
        task.setDescription(changes.getDescription());
        task.setStatus(changes.getStatus());
        return taskRepository.save(task);
    }

    public void delete(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        taskRepository.deleteById(id);
    }

}
