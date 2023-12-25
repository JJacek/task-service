package com.company.taskservice.domain.task.web;

import com.company.taskservice.BaseSpringBootTest;
import com.company.taskservice.domain.task.model.Task;
import com.company.taskservice.domain.task.model.TaskResult;
import com.company.taskservice.domain.task.web.dto.TaskDto;
import com.company.taskservice.domain.task.web.dto.TaskResultDto;
import com.company.taskservice.infrastructure.exception.MyEntityNotFoundException;
import com.company.taskservice.infrastructure.web.dto.PageDto;
import com.company.taskservice.tools.TaskPersister;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Page;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;

class TaskFacadeTest extends BaseSpringBootTest {

    @SpyBean
    private TaskFacade taskFacade;
    @Autowired
    private TaskPersister taskPersister;

    @Test
    void findTasksShouldReturnTaskDtos() {
        Task taskA = taskPersister.prepareTaskInDatabase(
                Task.builder().input("ab").pattern("ac").taskResult(new TaskResult(0, 1)).status(1.0f));
        Task taskB = taskPersister.prepareTaskInDatabase(
                Task.builder().input("b").pattern("b").status(0.2f));

        TaskDto taskDtoA = TaskDto.builder()
                .id(taskA.getId())
                .input(taskA.getInput())
                .pattern(taskA.getPattern())
                .taskResult(TaskResultDto.builder()
                        .position(taskA.getTaskResult().getPosition())
                        .typos(taskA.getTaskResult().getTypos())
                        .build())
                .status(taskA.getStatus()).build();
        TaskDto taskDtoB = TaskDto.builder()
                .id(taskB.getId())
                .input(taskB.getInput())
                .pattern(taskB.getPattern())
                .status(taskB.getStatus()).build();

        Page<TaskDto> result = taskFacade.findTasks(new PageDto(0, 10));
        assertThat(result.getContent()).containsExactly(taskDtoB, taskDtoA);
    }

    @Test
    void getTaskShouldReturnTaskDto() {
        Task task = taskPersister.prepareTaskInDatabase(Task.builder()
                .input("a").pattern("a").status(Task.STATUS_COMPLETED).taskResult(new TaskResult(0, 0)));

        TaskDto taskDtoA = TaskDto.builder()
                .id(task.getId())
                .input(task.getInput())
                .pattern(task.getPattern())
                .taskResult(TaskResultDto.builder()
                        .position(task.getTaskResult().getPosition())
                        .typos(task.getTaskResult().getTypos())
                        .build())
                .status(task.getStatus()).build();

        TaskDto result = taskFacade.getTask(task.getId());
        assertThat(result).isEqualTo(taskDtoA);
    }

    @Test
    void getTaskShouldReturnSecondTimeCashedCompletedTaskDto() {
        Task task = taskPersister.prepareTaskInDatabase(Task.builder()
                .input("a").pattern("a").status(Task.STATUS_COMPLETED).taskResult(new TaskResult(0, 0)));

        TaskDto resultFirstTime = taskFacade.getTask(task.getId());
        assertThat(resultFirstTime.getId()).isEqualTo(task.getId());
        TaskDto resultSecondTime = taskFacade.getTask(task.getId());
        assertThat(resultSecondTime.getId()).isEqualTo(task.getId());

        Mockito.verify(taskFacade, times(1)).getTask(task.getId());
    }
    @Test
    void getTaskShouldReturnSecondTimeNotCompletedTaskDtoFromDatabase() {
        Task task = taskPersister.prepareTaskInDatabase(Task.builder()
                .input("a").pattern("a").status(Task.STATUS_NOT_STARTED));

        TaskDto resultFirstTime = taskFacade.getTask(task.getId());
        assertThat(resultFirstTime.getId()).isEqualTo(task.getId());
        TaskDto resultSecondTime = taskFacade.getTask(task.getId());
        assertThat(resultSecondTime.getId()).isEqualTo(task.getId());

        Mockito.verify(taskFacade, times(2)).getTask(task.getId());
    }

    @Test
    void getTaskShouldThrow_MyEntityNotFoundException() {
        Assertions.assertThrows(MyEntityNotFoundException.class, () -> {
            taskFacade.getTask(100L);
        });
    }
}