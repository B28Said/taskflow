package kg.megalab.taskflow.service;

import kg.megalab.taskflow.dto.*;
import kg.megalab.taskflow.entity.Task;
import kg.megalab.taskflow.entity.TaskStatus;
import kg.megalab.taskflow.exception.TaskNotFoundException;
import kg.megalab.taskflow.repository.TaskRepository;
import kg.megalab.taskflow.mapper.TaskMapper;
import kg.megalab.taskflow.exception.BusinessRuleException;
import kg.megalab.taskflow.config.LimitsProperties;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final LimitsProperties limits;

    private static final Logger log =
            LoggerFactory.getLogger(TaskService.class);


    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper, LimitsProperties limits) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.limits = limits;
    }

    private static final List<TaskStatus> ACTIVE =
            List.of(TaskStatus.NEW, TaskStatus.IN_PROGRESS);

    private static final Sort NEWEST_FIRST =
            Sort.by(Sort.Direction.DESC, "createdAt");



    public List<TaskResponse> findAll(TaskStatus status, String search) {
        log.debug("Фильтры: status={}, search={}", status, search);
        boolean hasSearch = search != null && search.isBlank();
        List<Task> tasks;
                if(status != null && hasSearch){
                    tasks = taskRepository.findByStatusAndTitleContainingIgnoreCase(status, search, NEWEST_FIRST);
                } else if (status != null) {
                    tasks = taskRepository.findByStatus(status, NEWEST_FIRST);
                } else if (hasSearch) {
                    tasks = taskRepository
                            .findByTitleContainingIgnoreCase(search, NEWEST_FIRST);
                } else {
                    tasks = taskRepository.findAll(NEWEST_FIRST);
                }

        return tasks.stream()
                .map(taskMapper::toResponse)
                .toList();
    }


    public List<TaskResponse> findOverdue() {

        return taskRepository
                .findByDeadlineBeforeAndStatusInOrderByDeadlineAsc(
                        LocalDate.now(),
                        ACTIVE
                )
                .stream()
                .map(taskMapper::toResponse)
                .toList();
    }



    public TaskResponse create(TaskCreateRequest request) {
        Task task = taskMapper.toEntity(request);

        checkAssigneeLimit(request.assignee(), null);

        task.setStatus(TaskStatus.NEW);
        task.setCreatedAt(LocalDateTime.now());

        Task savedTask = taskRepository.save(task);
        log.info("Создана задача id={}, исполнитель={}",
                savedTask.getId(),
                savedTask.getAssignee());

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

        TaskStatus oldStatus = task.getStatus();
        taskMapper.updateEntity(task, request);

        Task saved = taskRepository.save(task);

        if (oldStatus != task.getStatus()) {
            log.info("Задача id={}: {} -> {}",
                    id, oldStatus, task.getStatus());
        }
        log.info("Обновлена задача id={}", saved.getId());

        return taskMapper.toResponse(saved);
    }



    public void delete(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }

        taskRepository.deleteById(id);
        log.info("Удалена задача id={}", id);
    }



    private Task getTask(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }


    private void checkAssigneeLimit(String assignee, Long excludeTaskId) {

        if (assignee == null || assignee.isBlank()) {
            return;
        }

        long active;

            if (excludeTaskId != null)
            {
                active =taskRepository.countByAssigneeAndStatusIn(assignee, ACTIVE);
            }else
            {
                active = taskRepository.countByAssigneeAndStatusInAndIdNot(assignee, ACTIVE, excludeTaskId);
            }

        if (active >= limits.maxActiveTasksPerAssignee()) {
            throw new BusinessRuleException(
                    "У исполнителя " + assignee
                            + " уже " + active
                            + " активных задач — это максимум"
            );
        }
    }

    public TaskStatsResponse getStats()
    {

        long total = taskRepository.count();

        Map<TaskStatus, Long> byStatus =
                new EnumMap<>(TaskStatus.class);

        for (TaskStatus status : TaskStatus.values()) {
            byStatus.put(status, 0L);
        }

        for (StatusCount item : taskRepository.countByStatusGrouped()) {
            byStatus.put(item.status(), item.count());
        }

        long overdue = taskRepository
                .countByDeadlineBeforeAndStatusIn(
                        LocalDate.now(), ACTIVE
                );

        return new TaskStatsResponse(total, byStatus, overdue);
    }
}
