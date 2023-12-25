package com.company.taskservice.tools;

import com.company.taskservice.domain.task.model.Task;
import com.company.taskservice.domain.task.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskPersister {

    @Autowired
    private TaskRepository taskRepository;

    public Task prepareNotStartedTask(String input, String pattern) {
        return prepareTaskInDatabase(Task.builder().input(input).pattern(pattern));
    }

    public Task prepareTaskInDatabase(Task.TaskBuilder taskBuilder) {
        return taskRepository.save(taskBuilder.build());
    }

}
