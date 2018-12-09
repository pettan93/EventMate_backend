package gr.tei.erasmus.pp.eventmate.backend.resources;

import gr.tei.erasmus.pp.eventmate.backend.models.Event;
import gr.tei.erasmus.pp.eventmate.backend.models.Task;
import gr.tei.erasmus.pp.eventmate.backend.repository.EventRepository;
import gr.tei.erasmus.pp.eventmate.backend.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.ws.rs.NotFoundException;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class EventResource {

    private final EventRepository eventRepository;

    private final TaskRepository taskRepository;

    @Autowired
    public EventResource(EventRepository eventRepository,
                         TaskRepository taskRepository) {
        this.eventRepository = eventRepository;
        this.taskRepository = taskRepository;
    }

    @GetMapping("/event")
    public List<Event> retrieveAllEvents() {
        return eventRepository.findAll();
    }

    @GetMapping("/event/{id}")
    public Event retrieveEvent(@PathVariable long id) {
        Optional<Event> event = eventRepository.findById(id);

        if (event.isEmpty())
            throw new NotFoundException("event id " + id + " not found");

        return event.get();
    }

    @PostMapping("/event")
    public ResponseEntity<Object> createEvent(@RequestBody Event event) {
        Event savedEvent = eventRepository.save(event);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedEvent.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PostMapping("/event/{id}/task")
    public ResponseEntity<Object> addEventTask(@RequestBody Task task,@PathVariable long id) {

        Optional<Event> eventOptional = eventRepository.findById(id);

        if (eventOptional.isEmpty())
            return ResponseEntity.notFound().build();

        Task savedTask = taskRepository.save(task);

        eventOptional.get().getTasks().add(savedTask);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(eventOptional.get().getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/event/{id}")
    public ResponseEntity<Object> updateEvent(@RequestBody Event event, @PathVariable long id) {

        Optional<Event> eventOptional = eventRepository.findById(id);
        if (eventOptional.isEmpty())
            return ResponseEntity.notFound().build();
        event.setId(id);
        eventRepository.save(event);

        return ResponseEntity.noContent().build();
    }


}
