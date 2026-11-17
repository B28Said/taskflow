
package kg.megalab.taskflow.repository;

import kg.megalab.taskflow.dto.StatusCount;
import kg.megalab.taskflow.entity.Task;
import kg.megalab.taskflow.entity.TaskStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByStatus(TaskStatus status, Sort sort);

    List<Task> findByTitleContainingIgnoreCase(
            String search, Sort sort
    );

    List<Task> findByStatusAndTitleContainingIgnoreCase(
            TaskStatus status,
            String search,
            Sort sort
    );

    long countByAssigneeAndStatusIn(
            String assignee,
            Collection<TaskStatus> statuses
    );

    long countByAssigneeAndStatusInAndIdNot(
            String assignee,
            Collection<TaskStatus> statuses,
            Long id
    );

    List<Task> findByDeadlineBeforeAndStatusInOrderByDeadlineAsc(
            LocalDate date,
            Collection<TaskStatus> statuses
    );


    @Query("""
    select new kg.megalab.taskflow.dto.StatusCount(
        t.status, count(t)
    )
    from Task t
    group by t.status
    """)
    List<StatusCount> countByStatusGrouped();


    long countByDeadlineBeforeAndStatusIn(
            LocalDate date,
            Collection<TaskStatus> statuses
    );

}
