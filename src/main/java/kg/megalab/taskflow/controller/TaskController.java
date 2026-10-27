package kg.megalab.taskflow.controller;

import kg.megalab.taskflow.model.Task;
import kg.megalab.taskflow.model.TaskStatus;
import kg.megalab.taskflow.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }


    @GetMapping
    public List<Task> findAll(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) String search) {

        return taskService.findAll(status, search);
    }


    @PostMapping
    public ResponseEntity<Task> create(@RequestBody Task task){
    Task created = taskService.create(task);

    URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(created.getId())
            .toUri();
    return ResponseEntity.created(location).body(created);
    }

    @GetMapping("/{id}")
    public Task findById(@PathVariable Long id) {
        return taskService.findById(id);
    }


    @PutMapping("/{id}")
    public Task update(@PathVariable Long id, @RequestBody Task changes) {
        return taskService.update(id, changes);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        taskService.delete(id);
    }

}
