package com.company.taskservice.domain.task.service.impl;

import com.company.taskservice.domain.task.model.Task;
import com.company.taskservice.domain.task.repository.TaskRepository;
import com.company.taskservice.domain.task.service.AsyncTaskProcessorService;
import com.company.taskservice.domain.task.service.TaskService;
import com.company.taskservice.domain.task.web.dto.CreateTaskDto;
import com.company.taskservice.infrastructure.exception.MyEntityNotFoundException;
import com.company.taskservice.infrastructure.web.dto.PageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final AsyncTaskProcessorService asyncTaskProcessorService;

    public TaskServiceImpl(TaskRepository taskRepository, AsyncTaskProcessorService asyncTaskProcessorService) {
        this.taskRepository = taskRepository;
        this.asyncTaskProcessorService = asyncTaskProcessorService;
    }

    @Override
    public Long createTask(CreateTaskDto createTaskDto) {
        Task persistedTask = taskRepository.save(Task.builder()
                .input(createTaskDto.input())
                .pattern(createTaskDto.pattern())
                .build());
        log.info("task created: {}", persistedTask);

        asyncTaskProcessorService.processTask(persistedTask);

        return persistedTask.getId();
    }

    @Override
    public Page<Task> findTasks(PageDto pageDto) {
        return taskRepository.findAll(
                PageRequest.of(pageDto.page(), pageDto.pageSize(), Task.DEFAULT_SORT));
    }
    @Override
    public Task getTask(Long id) {
        return taskRepository.findById(id).orElseThrow(() ->
                new MyEntityNotFoundException(Task.class.getSimpleName() + " with id " + id + " not found"));
    }

}
