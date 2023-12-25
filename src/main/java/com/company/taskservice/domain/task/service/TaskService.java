package com.company.taskservice.domain.task.service;

import com.company.taskservice.domain.task.web.dto.CreateTaskDto;
import com.company.taskservice.domain.task.model.Task;
import com.company.taskservice.infrastructure.web.dto.PageDto;
import org.springframework.data.domain.Page;

public interface TaskService {

    Long createTask(CreateTaskDto createTaskDto);

    Page<Task> findTasks(PageDto pageDto);

    Task getTask(Long id);
}
