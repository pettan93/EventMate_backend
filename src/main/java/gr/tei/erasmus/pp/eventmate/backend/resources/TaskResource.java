package gr.tei.erasmus.pp.eventmate.backend.resources;

import gr.tei.erasmus.pp.eventmate.backend.models.Task;
import gr.tei.erasmus.pp.eventmate.backend.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;

@RestController
public class TaskResource {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskResource(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping("/task")
    public List<Task> retrieveAllTasks() {
        return taskRepository.findAll();
    }

    @GetMapping("/task/{id}")
    public Task retrieveTask(@PathVariable long id) {
        Optional<Task> task = taskRepository.findById(id);

        if (task.isEmpty())
            throw new NotFoundException("event id " + id + " not found");

        return task.get();
    }

    @PutMapping("/task/{id}")
    public ResponseEntity<Object> updateTask(@RequestBody Task task, @PathVariable long id) {

        Optional<Task> taskOptional = taskRepository.findById(id);

        if (taskOptional.isEmpty())
            return ResponseEntity.notFound().build();

        task.setId(id);

        taskRepository.save(task);

        return ResponseEntity.noContent().build();
    }


}
