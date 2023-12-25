package com.company.taskservice.domain.task.model;

import jakarta.persistence.Embeddable;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
@Getter
@Embeddable
@ToString
public class TaskResult {

    @NonNull
    private Integer position;
    @NonNull
    private Integer typos;

}
