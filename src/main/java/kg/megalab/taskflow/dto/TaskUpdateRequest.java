
package kg.megalab.taskflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kg.megalab.taskflow.entity.TaskStatus;
import kg.megalab.taskflow.validation.AssigneeRequiredForDone;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@AssigneeRequiredForDone
public record TaskUpdateRequest(

        @NotBlank(message = "Название не может быть пустым")
        @Size(min = 3, max = 100, message = "Название должно содержать от {min} до {max} символов")
        @Schema(description = "Название задачи", example = "Изучить Spring Security")
        String title,

        @Size(max = 1000,
                message = "Описание не длиннее {max} символов")
        @Schema(description = "Описание задачи", example = "Добавить авторизацию в проект")
        String description,

        @NotNull(message = "Статус обязателен")
        @Schema(description = "Новый статус задачи", example = "IN_PROGRESS")
        TaskStatus status,

        @Schema(description = "Срок выполнения задачи", example = "2026-12-30")
        LocalDate deadline,

        @Size(max = 100, message = "Имя исполнителя не длиннее {max} символов")
        @Schema(description = "Исполнитель задачи", example = "Alex")
        String assignee
) { }
