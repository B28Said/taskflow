
package kg.megalab.taskflow.dto;

import kg.megalab.taskflow.entity.TaskStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TaskResponse(
        Long id,
        String title,
        String description,
        TaskStatus status,
        LocalDateTime createdAt,
        LocalDate deadline,
        String assignee
) {
}
