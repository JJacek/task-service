package com.company.taskservice.domain.task.service.impl;

import com.company.taskservice.domain.task.model.Task;
import com.company.taskservice.domain.task.processor.TaskProcessor;
import com.company.taskservice.domain.task.repository.TaskRepository;
import com.company.taskservice.domain.task.service.AsyncTaskProcessorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AsyncTaskProcessorServiceImpl implements AsyncTaskProcessorService {

    private final TaskProcessor taskProcessor;
    private final TaskRepository taskRepository;

    public AsyncTaskProcessorServiceImpl(TaskProcessor taskProcessor, TaskRepository taskRepository) {
        this.taskProcessor = taskProcessor;
        this.taskRepository = taskRepository;
    }

    @Async
    @Override
    public void processTask(Task task) {
        log.debug("Starting processing task with id {}", task.getId());
        taskProcessor.processTask(task, this::taskStatusChanged);
        log.debug("Ending processing task with id {}", task.getId());
    }

    private void taskStatusChanged(Task task) {
        log.debug("saving task after status has changed: {}", task);
        taskRepository.save(task);
    }

}
