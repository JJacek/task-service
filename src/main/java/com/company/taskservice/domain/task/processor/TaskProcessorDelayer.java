package com.company.taskservice.domain.task.processor;

public interface TaskProcessorDelayer {

    void delayIteration(long delayInMillis);

}
