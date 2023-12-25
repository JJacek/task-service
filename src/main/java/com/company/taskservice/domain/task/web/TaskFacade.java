package com.company.taskservice.domain.task.web;

import com.company.taskservice.domain.task.service.TaskService;
import com.company.taskservice.domain.task.web.dto.CreateTaskDto;
import com.company.taskservice.domain.task.web.dto.TaskDto;
import com.company.taskservice.domain.task.web.dto.TaskIdDto;
import com.company.taskservice.infrastructure.web.dto.PageDto;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class TaskFacade {

    private final TaskService taskService;
    private final ConversionService conversionService;

    public TaskFacade(TaskService taskService, ConversionService conversionService) {
        this.taskService = taskService;
        this.conversionService = conversionService;
    }

    public TaskIdDto createTask(CreateTaskDto createTaskDto) {
        return new TaskIdDto(taskService.createTask(createTaskDto));
    }

    public Page<TaskDto> findTasks(PageDto pageDto) {
        return taskService.findTasks(pageDto).map(task -> conversionService.convert(task, TaskDto.class));
    }

    @Cacheable(value = "completedTaskDto", key = "#id", unless = "#result.status < 1")
    public TaskDto getTask(Long id) {
        return conversionService.convert(taskService.getTask(id), TaskDto.class);
    }


}
