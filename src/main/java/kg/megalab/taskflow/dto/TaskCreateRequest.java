
package kg.megalab.taskflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.FutureOrPresent;
import java.time.LocalDate;

public record TaskCreateRequest(

        @NotBlank(message = "Название не может быть пустым")
        @Size(min = 3, max = 100,
                message = "Название должно содержать от {min} до {max} символов")
        String title,

        @Size(max = 1000,
                message = "Описание не длиннее {max} символов")
        String description,

        @FutureOrPresent(message = "Дедлайн не может быть в прошлом")
        LocalDate deadline,

        @Size(max = 100,
                message = "Имя исполнителя не длиннее {max} символов")
        String assignee
) {
}
