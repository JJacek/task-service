package com.company.taskservice.domain.task.web.converter;

import com.company.taskservice.domain.task.model.Task;
import com.company.taskservice.domain.task.model.TaskResult;
import com.company.taskservice.domain.task.web.dto.TaskDto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskToDtoConverterTest {

    @Test
    void convert() {
        Long id = 1L;
        String input = "ABCD";
        String pattern = "BCD";
        int position = 1;
        int typos = 0;
        TaskResult taskResult = new TaskResult(position, typos);

        Task task = Task.builder()
                .id(id)
                .input(input)
                .pattern(pattern)
                .taskResult(taskResult)
                .status(Task.STATUS_COMPLETED).build();

        TaskDto result = new TaskToDtoConverter().convert(task);
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getInput()).isEqualTo(input);
        assertThat(result.getPattern()).isEqualTo(pattern);
        assertThat(result.getTaskResult()).isNotNull();
        assertThat(result.getTaskResult().getPosition()).isEqualTo(position);
        assertThat(result.getTaskResult().getTypos()).isEqualTo(typos);
        assertThat(result.getStatus()).isEqualTo(Task.STATUS_COMPLETED);
    }

}