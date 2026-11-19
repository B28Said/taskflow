package kg.megalab.taskflow.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.megalab.taskflow.dto.TaskCreateRequest;
import kg.megalab.taskflow.dto.TaskStatsResponse;
import kg.megalab.taskflow.dto.TaskUpdateRequest;
import kg.megalab.taskflow.entity.TaskStatus;
import kg.megalab.taskflow.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import kg.megalab.taskflow.dto.TaskResponse;

import java.net.URI;
import java.util.List;
import jakarta.validation.Valid;

@Tag(name = "Задачи", description = "Создание, поиск и изменение задач")
@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }



    @Operation(
            summary = "Получить список задач",
            description = "Возвращает список задач с возможностью фильтрации по статусу и поиска по тексту"
    )
    @ApiResponse(responseCode = "200", description = "Список задач получен")
    @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса")
    @GetMapping
    public List<TaskResponse> findAll(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) String search) {

        return taskService.findAll(status, search);
    }

    @Operation(summary = "Создать задачу",
            description = "Статус NEW и дату создания назначает сервер")
    @ApiResponse(responseCode = "201", description = "Задача создана")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации")
    @ApiResponse(responseCode = "409", description = "У исполнителя превышен лимит активных задач")
    @PostMapping
    public ResponseEntity<TaskResponse> create( @Valid @RequestBody TaskCreateRequest request){
        TaskResponse created = taskService.create(request);

    URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(created.id())
            .toUri();
    return ResponseEntity.created(location).body(created);
    }


    @Operation(
            summary = "Получить задачу по ID",
            description = "Возвращает информацию о задаче по её идентификатору"
    )
    @ApiResponse(responseCode = "200", description = "Задача найдена")
    @ApiResponse(responseCode = "404", description = "Задача не найдена")
    @GetMapping("/{id}")
    public TaskResponse findById(@PathVariable Long id) {
        return taskService.findById(id);
    }

    @Operation(
            summary = "Обновить задачу",
            description = "Изменяет данные задачи с проверкой допустимости перехода статуса и лимита активных задач исполнителя"
    )
    @ApiResponse(responseCode = "200", description = "Задача успешно обновлена")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации")
    @ApiResponse(responseCode = "404", description = "Задача не найдена")
    @ApiResponse(responseCode = "409", description = "Нарушение бизнес-правил")
    @PutMapping("/{id}")
    public TaskResponse update(@PathVariable Long id, @Valid @RequestBody TaskUpdateRequest changes) {
        return taskService.update(id, changes);
    }


    @Operation(
            summary = "Удалить задачу",
            description = "Удаляет задачу по её идентификатору"
    )
    @ApiResponse(responseCode = "204", description = "Задача успешно удалена")
    @ApiResponse(responseCode = "404", description = "Задача не найдена")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        taskService.delete(id);
    }


    @Operation(
            summary = "Получить просроченные задачи",
            description = "Возвращает список незавершённых задач, у которых истёк срок выполнения"
    )
    @ApiResponse(responseCode = "200", description = "Список просроченных задач получен")
    @GetMapping("/overdue")
    public List<TaskResponse> findOverdue() {
        return taskService.findOverdue();
    }


    @Operation(
            summary = "Получить статистику задач",
            description = "Возвращает общее количество задач и статистику по статусам"
    )
    @ApiResponse(responseCode = "200", description = "Статистика успешно получена")
    @GetMapping("/stats")
    public TaskStatsResponse getStats() {
        return taskService.getStats();
    }

}
