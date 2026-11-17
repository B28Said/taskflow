
package kg.megalab.taskflow.dto;

import kg.megalab.taskflow.entity.TaskStatus;

public record StatusCount(
        TaskStatus status,
        long count
) {
}
