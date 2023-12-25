package com.company.taskservice.domain.task.model;

import com.company.taskservice.util.Preconditions;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.domain.Sort;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@EqualsAndHashCode(of = "id")
public class Task {
    public static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.ASC, "status", "taskResult.typos");
    public static final float STATUS_COMPLETED = 1.0f;
    public static final float STATUS_NOT_STARTED = 0.0f;

    @Id
    @GeneratedValue
    private Long id;
    @NonNull
    private String input;
    @NonNull
    private String pattern;
    @Setter
    @Embedded
    private TaskResult taskResult;
    @Setter
    @Builder.Default
    private float status = STATUS_NOT_STARTED;

    private Task(Long id, @NonNull String input, @NonNull String pattern, TaskResult taskResult, float status) {
        Preconditions.greaterOrEqualsThen(input.length(), pattern.length());

        this.id = id;
        this.input = input;
        this.pattern = pattern;
        this.taskResult = taskResult;
        this.status = status;
    }

}

