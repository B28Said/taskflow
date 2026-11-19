
package kg.megalab.taskflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kg.megalab.taskflow.entity.TaskStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;


public record TaskResponse(
        @Schema(description = "Уникальный ID задачи", example = "1")
        Long id,

        @Schema(description = "Название задачи", example = "Изучить Spring Boot")
        String title,

        @Schema(description = "Описание задачи", example = "Изучить REST API")
        String description,

        @Schema(description = "Текущий статус задачи", example = "IN_PROGRESS")
        TaskStatus status,

        @Schema(description = "Дата и время создания задачи", example = "2026-11-19T15:30:00")
        LocalDateTime createdAt,

        @Schema(description = "Срок выполнения задачи", example = "2026-12-25")
        LocalDate deadline,

        @Schema(description = "Исполнитель задачи", example = "Alex")
        String assignee

) {}
