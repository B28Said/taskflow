
package kg.megalab.taskflow.mapper;

import kg.megalab.taskflow.dto.TaskCreateRequest;
import kg.megalab.taskflow.dto.TaskUpdateRequest;
import kg.megalab.taskflow.dto.TaskResponse;
import kg.megalab.taskflow.entity.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public Task toEntity(TaskCreateRequest request) {
        Task task = new Task();

        task.setTitle(request.title());
        task.setDescription(request.description());

        return task;
    }

    public void updateEntity(Task task, TaskUpdateRequest request) {
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setStatus(request.status());
    }

    public TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getCreatedAt()
        );
    }
}
