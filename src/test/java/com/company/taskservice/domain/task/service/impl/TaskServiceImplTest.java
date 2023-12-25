package com.company.taskservice.domain.task.service.impl;

import com.company.taskservice.BaseSpringBootTest;
import com.company.taskservice.domain.task.model.Task;
import com.company.taskservice.domain.task.model.TaskResult;
import com.company.taskservice.domain.task.repository.TaskRepository;
import com.company.taskservice.domain.task.web.dto.CreateTaskDto;
import com.company.taskservice.infrastructure.exception.MyEntityNotFoundException;
import com.company.taskservice.infrastructure.web.dto.PageDto;
import com.company.taskservice.tools.TaskPersister;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import static org.assertj.core.api.Assertions.assertThat;

class TaskServiceImplTest extends BaseSpringBootTest {

    @Autowired
    private TaskServiceImpl taskServiceImpl;
    @Autowired
    private TaskPersister taskPersister;
    @Autowired
    private TaskRepository taskRepository;

    @Test
    void createTask() throws InterruptedException {
        String input = "ABCDEFG";
        String pattern = "CFG";
        Long taskId = taskServiceImpl.createTask(new CreateTaskDto(input, pattern));
        Thread.sleep(200); // wait to end processing of task in another thread

        Task task = taskRepository.findById(taskId).get();
        assertThat(task.getInput()).isEqualTo(input);
        assertThat(task.getPattern()).isEqualTo(pattern);
        assertThat(task.getStatus()).isEqualTo(Task.STATUS_COMPLETED);
        assertThat(task.getTaskResult().getPosition()).isEqualTo(4);
        assertThat(task.getTaskResult().getTypos()).isEqualTo(1);
    }

    @Test
    void findTasks() {
        Task taskA = taskPersister.prepareTaskInDatabase(Task.builder()
                .input("a").pattern("a").status(Task.STATUS_COMPLETED).taskResult(new TaskResult(0, 0)));
        Task taskB = taskPersister.prepareTaskInDatabase(Task.builder()
                .input("ab").pattern("ac").status(Task.STATUS_COMPLETED).taskResult(new TaskResult(0, 1)));
        Task taskC = taskPersister.prepareTaskInDatabase(
                Task.builder().input("c").pattern("c").status(0.2f));
        Task taskD = taskPersister.prepareTaskInDatabase(
                Task.builder().input("d").pattern("d").status(0.5f));

        Page<Task> result = taskServiceImpl.findTasks(new PageDto(0, 10));
        assertThat(result.getContent()).containsExactly(taskC, taskD, taskA, taskB);
    }

    @Test
    void getTaskFound() {
        Task task = taskPersister.prepareTaskInDatabase(Task.builder()
                .input("a").pattern("a").status(Task.STATUS_COMPLETED).taskResult(new TaskResult(0, 0)));

        Task result = taskServiceImpl.getTask(task.getId());
        assertThat(result).isEqualTo(task);
    }

    @Test
    void getTaskShouldThrow_MyEntityNotFoundException() {
        Assertions.assertThrows(MyEntityNotFoundException.class, () -> {
            taskServiceImpl.getTask(100L);
        });
    }

}