package com.company.taskservice.domain.task.service;

import com.company.taskservice.domain.task.model.Task;


public interface AsyncTaskProcessorService {

    public void processTask(Task task);
}
