package com.company.taskservice.domain.task.processor;

import com.company.taskservice.domain.task.model.Task;

import java.util.function.Consumer;

public interface TaskProcessor {

    void processTask(Task task, Consumer<Task> statusChangedConsumer);

}
