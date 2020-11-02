package org.briarheart.orchestra.controller;

import org.briarheart.orchestra.config.PermitAllSecurityConfig;
import org.briarheart.orchestra.model.TaskList;
import org.briarheart.orchestra.service.TaskListService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Locale;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockAuthentication;

/**
 * @author Roman Chigvintsev
 */
@ExtendWith(SpringExtension.class)
@WebFluxTest(TaskListController.class)
@Import(PermitAllSecurityConfig.class)
class TaskListControllerTest {
    private static final Locale DEFAULT_LOCALE = Locale.getDefault();

    @Autowired
    private WebTestClient testClient;

    @MockBean
    private TaskListService taskListService;

    @BeforeAll
    static void beforeAll() {
        Locale.setDefault(Locale.ENGLISH);
    }

    @AfterAll
    static void afterAll() {
        Locale.setDefault(DEFAULT_LOCALE);
    }

    @Test
    void shouldReturnAllUncompletedTaskLists() {
        Authentication authenticationMock = mock(Authentication.class);
        when(authenticationMock.getName()).thenReturn("alice");

        TaskList taskList = TaskList.builder()
                .id(1L)
                .name("Test task list")
                .author(authenticationMock.getName())
                .build();
        when(taskListService.getUncompletedTaskLists(eq(authenticationMock.getName()))).thenReturn(Flux.just(taskList));

        testClient.mutateWith(mockAuthentication(authenticationMock)).get()
                .uri("/task-lists/uncompleted").exchange()
                .expectStatus().isOk()
                .expectBody(TaskList[].class).isEqualTo(new TaskList[] {taskList});
    }

    @Test
    void shouldCreateTaskList() {
        Authentication authenticationMock = mock(Authentication.class);
        when(authenticationMock.getName()).thenReturn("alice");

        TaskList taskList = TaskList.builder().name("New task list").build();
        TaskList savedTaskList = TaskList.builder()
                .id(1L)
                .name(taskList.getName())
                .author(authenticationMock.getName())
                .build();

        when(taskListService.createTaskList(taskList, authenticationMock.getName()))
                .thenReturn(Mono.just(savedTaskList));

        testClient.mutateWith(mockAuthentication(authenticationMock))
                .mutateWith(csrf()).post()
                .uri("/task-lists")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(taskList)
                .exchange()

                .expectStatus().isCreated()
                .expectHeader().valueEquals("Location", "/task-lists/" + savedTaskList.getId())
                .expectBody(TaskList.class).isEqualTo(savedTaskList);
    }

    @Test
    void shouldRejectTaskListCreationWhenNameIsNull() {
        Authentication authenticationMock = mock(Authentication.class);
        when(authenticationMock.getName()).thenReturn("alice");

        TaskList taskList = TaskList.builder().build();

        testClient.mutateWith(mockAuthentication(authenticationMock))
                .mutateWith(csrf()).post()
                .uri("/task-lists")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(taskList)
                .exchange()

                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.fieldErrors").exists()
                .jsonPath("$.fieldErrors.name").isEqualTo("Value must not be blank");
    }

    @Test
    void shouldRejectTaskListCreationWhenNameIsBlank() {
        Authentication authenticationMock = mock(Authentication.class);
        when(authenticationMock.getName()).thenReturn("alice");

        TaskList taskList = TaskList.builder().name(" ").build();

        testClient.mutateWith(mockAuthentication(authenticationMock))
                .mutateWith(csrf()).post()
                .uri("/task-lists")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(taskList)
                .exchange()

                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.fieldErrors").exists()
                .jsonPath("$.fieldErrors.name").isEqualTo("Value must not be blank");
    }

    @Test
    void shouldDeleteTaskList() {
        Authentication authenticationMock = mock(Authentication.class);
        when(authenticationMock.getName()).thenReturn("alice");

        long taskListId = 1L;
        Mockito.when(taskListService.deleteTaskList(taskListId, authenticationMock.getName())).thenReturn(Mono.empty());

        testClient.mutateWith(mockAuthentication(authenticationMock)).mutateWith(csrf()).delete()
                .uri("/task-lists/" + taskListId).exchange()
                .expectStatus().isNoContent();
    }
}
