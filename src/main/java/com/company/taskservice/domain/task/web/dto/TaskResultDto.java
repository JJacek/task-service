package com.company.taskservice.domain.task.web.dto;

import lombok.*;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class TaskResultDto {

    @NonNull
    private final Integer position;
    @NonNull
    private final Integer typos;

}
