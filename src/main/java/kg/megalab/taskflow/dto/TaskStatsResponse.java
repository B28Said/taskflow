
package kg.megalab.taskflow.dto;

import kg.megalab.taskflow.entity.TaskStatus;
import java.util.Map;

public record TaskStatsResponse(
        long total,
        Map<TaskStatus, Long> byStatus,
        long overdue
) {
}
