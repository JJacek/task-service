package com.company.taskservice.domain.task.web.dto;

import com.company.taskservice.infrastructure.validator.GreaterOrEqualsThanTarget;
import jakarta.validation.constraints.NotEmpty;

@GreaterOrEqualsThanTarget(fieldGreaterOrEqual = "input", fieldTarget = "pattern")
public record CreateTaskDto(@NotEmpty String input, @NotEmpty String pattern) {}
