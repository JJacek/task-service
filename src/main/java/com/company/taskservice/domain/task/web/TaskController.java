package com.company.taskservice.domain.task.web;

import com.company.taskservice.domain.task.web.dto.CreateTaskDto;
import com.company.taskservice.domain.task.web.dto.TaskDto;
import com.company.taskservice.domain.task.web.dto.TaskIdDto;
import com.company.taskservice.infrastructure.web.dto.PageDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/task")
public class TaskController {

    private final TaskFacade taskFacade;

    public TaskController(TaskFacade taskFacade) {
        this.taskFacade = taskFacade;
    }

    @Operation(summary = "Creates a task and returns it's id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Task has been created",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TaskIdDto.class)) }),
        @ApiResponse(responseCode = "400", description = "Invalid input and/or pattern",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)) })
    })
    @PostMapping
    public ResponseEntity<TaskIdDto> createTask(
            @Parameter(description = "Input and pattern can not be empty. Input must be equal or longer than pattern.")
            @RequestBody @Valid CreateTaskDto createTaskDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskFacade.createTask(createTaskDto));
    }

    /**
     * This is POST and not GET request because in real case input dto will have another fields like filters
     * and this kind of data I prefer to send in request body compared as URI's parameters.
     *
     * @param pageDto page definition
     * @return page of TaskDto
     */
    @Operation(summary = "Returns page of tasks")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Find page of tasks"),
            @ApiResponse(responseCode = "400", description = "Invalid page and/or pageSize",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)) })
    })
    @PostMapping("/search")
    public ResponseEntity<Page<TaskDto>> findTasks(
            @Parameter(description = "0 indexed page.")
            @RequestBody @Valid PageDto pageDto) {
        return ResponseEntity.status(HttpStatus.OK).body(taskFacade.findTasks(pageDto));
    }

    @Operation(summary = "Returns a task with the given id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get task with the given id",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Not found task with the given id",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)) })
    })
    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTask(
            @Parameter(description = "Task's id")
            @PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(taskFacade.getTask(id));
    }

}
