package com.company.taskservice.domain.task.web.converter;

import com.company.taskservice.domain.task.model.Task;
import com.company.taskservice.domain.task.web.dto.TaskDto;
import com.company.taskservice.domain.task.web.dto.TaskResultDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TaskToDtoConverter implements Converter<Task, TaskDto> {

    @Override
    public TaskDto convert(Task source) {
        TaskDto.TaskDtoBuilder taskDtoBuilder = TaskDto.builder()
                .id(source.getId())
                .input(source.getInput())
                .pattern(source.getPattern())
                .status(source.getStatus());
        Optional.ofNullable(source.getTaskResult()).ifPresent(taskResult -> {
            taskDtoBuilder.taskResult(TaskResultDto.builder()
                    .position(taskResult.getPosition())
                    .typos(taskResult.getTypos())
                    .build());
        });

        return taskDtoBuilder.build();
    }

}
