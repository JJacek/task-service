package com.company.taskservice.domain.task.service;

import com.company.taskservice.domain.task.model.Task;
import com.company.taskservice.domain.task.repository.TaskRepository;
import com.company.taskservice.tools.TaskPersister;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AsyncTaskProcessorServiceImplTest {

    @Autowired
    private AsyncTaskProcessorService asyncTaskProcessorServiceImpl;
    @Autowired
    private TaskPersister taskPersister;
    @Autowired
    private TaskRepository taskRepository;

    @Test
    void processTask() throws InterruptedException {
        Task task = taskPersister.prepareNotStartedTask("ABCDEFG", "CFG");

        asyncTaskProcessorServiceImpl.processTask(task);
        Thread.sleep(200); // wait to end processing of task in another thread

        Task reloadedTask = taskRepository.findById(task.getId()).get();
        assertThat(reloadedTask.getStatus()).isEqualTo(Task.STATUS_COMPLETED);
        assertThat(reloadedTask.getTaskResult().getPosition()).isEqualTo(4);
        assertThat(reloadedTask.getTaskResult().getTypos()).isEqualTo(1);
    }

}