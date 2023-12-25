package com.company.taskservice.domain.task.web;

import com.company.taskservice.domain.task.model.Task;
import com.company.taskservice.domain.task.web.dto.CreateTaskDto;
import com.company.taskservice.domain.task.web.dto.TaskDto;
import com.company.taskservice.domain.task.web.dto.TaskIdDto;
import com.company.taskservice.domain.task.web.dto.TaskResultDto;
import com.company.taskservice.infrastructure.exception.MyEntityNotFoundException;
import com.company.taskservice.infrastructure.web.dto.PageDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.company.taskservice.tools.TaksConstans.*;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TaskController.class)
class TaskControllerTest {

    @MockBean
    private TaskFacade taskFacade;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createTaskShouldReturnId() throws Exception {
        CreateTaskDto createTaskDto = new CreateTaskDto(TASK_INPUT, TASK_PATTERN);
        Mockito.when(taskFacade.createTask(createTaskDto)).thenReturn(new TaskIdDto(TASK_ID));

        mockMvc.perform(post("/api/v1/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTaskDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.taskId").value(TASK_ID));
    }

    @Test
    void createTaskWithInvalidDataShouldReturnBadRequest() throws Exception {
        String inputTooShortComparedToPattern = "AB";
        String pattern = "XYZ";

        CreateTaskDto createTaskDto = new CreateTaskDto(inputTooShortComparedToPattern, pattern);

        mockMvc.perform(post("/api/v1/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTaskDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.type").value("/errors/validation"))
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value("'input' must be greater or equal 'pattern'"))
                .andExpect(jsonPath("$.instance").value("/api/v1/task"));
    }

    @Test
    void findTasksShouldReturnTasks() throws Exception {
        TaskDto taskDtoA = TaskDto.builder()
                .id(TASK_ID)
                .input(TASK_INPUT)
                .pattern(TASK_PATTERN)
                .taskResult(TaskResultDto.builder()
                        .position(TASK_RESULT_POSITION)
                        .typos(TASK_RESULT_TYPOS)
                        .build())
                .status(Task.STATUS_COMPLETED).build();
        TaskDto taskDtoB = TaskDto.builder()
                .id(TASK_ID_2)
                .input(TASK_INPUT_2)
                .pattern(TASK_PATTERN_2)
                .status(Task.STATUS_NOT_STARTED).build();

        PageDto pageDto = new PageDto(0,10);
        Mockito.when(taskFacade.findTasks(pageDto)).thenReturn(
                new PageImpl(List.of(taskDtoB, taskDtoA), PageRequest.of(0, 10), 2));

        mockMvc.perform(post("/api/v1/task/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pageDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].id").value(taskDtoB.getId()))
                .andExpect(jsonPath("$.content[0].input").value(taskDtoB.getInput()))
                .andExpect(jsonPath("$.content[0].taskResult").doesNotExist())
                .andExpect(jsonPath("$.content[0].status").value(taskDtoB.getStatus()))
                .andExpect(jsonPath("$.content[1].id").value(taskDtoA.getId()))
                .andExpect(jsonPath("$.content[1].input").value(taskDtoA.getInput()))
                .andExpect(jsonPath("$.content[1].taskResult").exists())
                .andExpect(jsonPath("$.content[1].taskResult.position").value(taskDtoA.getTaskResult().getPosition()))
                .andExpect(jsonPath("$.content[1].taskResult.typos").value(taskDtoA.getTaskResult().getTypos()))
                .andExpect(jsonPath("$.content[1].status").value(taskDtoA.getStatus()));
    }

    @Test
    void getTaskShouldReturnTask() throws Exception {
        TaskDto taskDto = TaskDto.builder()
                .id(TASK_ID)
                .input(TASK_INPUT)
                .pattern(TASK_PATTERN)
                .taskResult(TaskResultDto.builder()
                        .position(TASK_RESULT_POSITION)
                        .typos(TASK_RESULT_TYPOS)
                        .build())
                .status(Task.STATUS_COMPLETED).build();
        Mockito.when(taskFacade.getTask(TASK_ID)).thenReturn(taskDto);

        mockMvc.perform(get("/api/v1/task/{id}", TASK_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(taskDto.getId()))
                .andExpect(jsonPath("$.input").value(taskDto.getInput()))
                .andExpect(jsonPath("$.taskResult").exists())
                .andExpect(jsonPath("$.taskResult.position").value(taskDto.getTaskResult().getPosition()))
                .andExpect(jsonPath("$.taskResult.typos").value(taskDto.getTaskResult().getTypos()))
                .andExpect(jsonPath("$.status").value(taskDto.getStatus()));
    }

    @Test
    void getTaskShouldReturnNotFound() throws Exception {
        Mockito.when(taskFacade.getTask(TASK_ID)).thenThrow(new MyEntityNotFoundException(TASK_NOT_FOUND_ERROR_MSG));

        mockMvc.perform(get("/api/v1/task/{id}", TASK_ID))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.type").value("/errors/not-found"))
                .andExpect(jsonPath("$.title").value("Not Found"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").value(TASK_NOT_FOUND_ERROR_MSG))
                .andExpect(jsonPath("$.instance").value("/api/v1/task/1"));
    }


}