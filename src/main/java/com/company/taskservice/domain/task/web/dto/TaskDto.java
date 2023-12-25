package com.company.taskservice.domain.task.web.dto;

import lombok.*;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class TaskDto {
    @NonNull
    private final Long id;
    @NonNull
    private final String input;
    @NonNull
    private final String pattern;
    private final TaskResultDto taskResult;
    @NonNull
    private final Float status;

}
