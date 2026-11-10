package kg.megalab.taskflow.service;

import kg.megalab.taskflow.dto.TaskCreateRequest;
import kg.megalab.taskflow.dto.TaskResponse;
import kg.megalab.taskflow.entity.Task;
import kg.megalab.taskflow.entity.TaskStatus;
import kg.megalab.taskflow.exception.TaskNotFoundException;
import kg.megalab.taskflow.repository.TaskRepository;
import org.springframework.stereotype.Service;
import kg.megalab.taskflow.mapper.TaskMapper;
import kg.megalab.taskflow.dto.TaskUpdateRequest;
import kg.megalab.taskflow.exception.BusinessRuleException;
import kg.megalab.taskflow.config.LimitsProperties;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final LimitsProperties limits;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper, LimitsProperties limits) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.limits = limits;
    }



    public List<TaskResponse> findAll(TaskStatus status, String search) {
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
                .map(taskMapper::toResponse)
                .toList();
    }


    public TaskResponse create(TaskCreateRequest request) {
        Task task = taskMapper.toEntity(request);

        checkAssigneeLimit(request.assignee(), null);

        task.setStatus(TaskStatus.NEW);
        task.setCreatedAt(LocalDateTime.now());

        Task savedTask = taskRepository.save(task);

        return taskMapper.toResponse(savedTask);
    }

    public TaskResponse findById(Long id) {
        return taskMapper.toResponse(getTask(id));
    }



    public TaskResponse update(Long id, TaskUpdateRequest request) {

        Task task = getTask(id);

        if (task.getStatus().isFinal()) {
            throw new BusinessRuleException(
                    "Задачу в статусе " + task.getStatus()
                            + " нельзя изменить"
            );
        }

        if (!task.getStatus().canTransitionTo(request.status())) {
            throw new BusinessRuleException(
                    "Недопустимый переход статуса: "
                            + task.getStatus() + " → " + request.status()
            );
        }

        checkAssigneeLimit(request.assignee(), id);

        taskMapper.updateEntity(task, request);

        return taskMapper.toResponse(taskRepository.save(task));
    }



    public void delete(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }

        taskRepository.deleteById(id);
    }



    private Task getTask(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }


    private void checkAssigneeLimit(String assignee, Long excludeTaskId) {

        if (assignee == null || assignee.isBlank()) {
            return;
        }

        long active = taskRepository.findAll().stream()
                .filter(t -> assignee.equals(t.getAssignee()))
                .filter(t -> !t.getStatus().isFinal())
                .filter(t -> !t.getId().equals(excludeTaskId))
                .count();

        if (active >= limits.maxActiveTasksPerAssignee()) {
            throw new BusinessRuleException(
                    "У исполнителя " + assignee
                            + " уже " + active
                            + " активных задач — это максимум"
            );
        }
    }

}
