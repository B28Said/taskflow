
package kg.megalab.taskflow.config;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "taskflow.limits")
public record LimitsProperties(

        @Min(value = 1, message = "Лимит задач должен быть не меньше 1")
        @Max(value = 100, message = "Лимит задач должен быть не больше 100")
        int maxActiveTasksPerAssignee

) {}
