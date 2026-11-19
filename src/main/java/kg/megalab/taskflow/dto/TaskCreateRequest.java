
package kg.megalab.taskflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.FutureOrPresent;
import java.time.LocalDate;

public record TaskCreateRequest(

        @NotBlank(message = "Название не может быть пустым")
        @Size(min = 3, max = 100, message = "Название должно содержать от {min} до {max} символов")
        @Schema(description = "Название задачи", example = "Изучить Spring Boot")
        String title,

        @Size(max = 1000, message = "Описание не длиннее {max} символов")
        @Schema(description = "Описание задачи", example = "Изучить основы REST API")
        String description,

        @Schema(description = "Срок выполнения", example = "2026-12-25")
        @FutureOrPresent(message = "Дедлайн не может быть в прошлом")
        LocalDate deadline,

        @Schema(description = "Исполнитель задачи", example = "Alex")
        @Size(max = 100, message = "Имя исполнителя не длиннее {max} символов")
        String assignee
) {
}
