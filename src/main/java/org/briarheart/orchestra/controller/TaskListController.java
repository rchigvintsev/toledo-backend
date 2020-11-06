package org.briarheart.orchestra.controller;

import lombok.RequiredArgsConstructor;
import org.briarheart.orchestra.model.TaskList;
import org.briarheart.orchestra.service.TaskListService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;

/**
 * REST-controller for task list managing.
 *
 * @author Roman Chigvintsev
 */
@RestController
@RequestMapping("/task-lists")
@RequiredArgsConstructor
public class TaskListController {
    private final TaskListService taskListService;

    @GetMapping("/uncompleted")
    public Flux<TaskList> getUncompletedTaskLists(Principal user) {
        return taskListService.getUncompletedTaskLists(user.getName());
    }

    @PostMapping
    public Mono<ResponseEntity<TaskList>> createTaskList(@Valid @RequestBody TaskList taskList,
                                                         Principal user,
                                                         ServerHttpRequest request) {
        return taskListService.createTaskList(taskList, user.getName()).map(createdTaskList -> {
            URI taskListLocation = UriComponentsBuilder.fromHttpRequest(request)
                    .path("/{id}")
                    .buildAndExpand(createdTaskList.getId())
                    .toUri();
            return ResponseEntity.created(taskListLocation).body(createdTaskList);
        });
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteTaskList(@PathVariable Long id, Principal user) {
        return taskListService.deleteTaskList(id, user.getName());
    }
}