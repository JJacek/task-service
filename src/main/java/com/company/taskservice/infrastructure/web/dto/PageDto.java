package com.company.taskservice.infrastructure.web.dto;


import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * @param page 0 base indexed page
 * @param pageSize max number of elements per page
 */
public record PageDto(@PositiveOrZero int page, @Positive int pageSize) {}
