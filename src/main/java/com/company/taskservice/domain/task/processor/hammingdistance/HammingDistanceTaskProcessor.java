package com.company.taskservice.domain.task.processor.hammingdistance;

import com.company.taskservice.domain.task.model.Task;
import com.company.taskservice.domain.task.model.TaskResult;
import com.company.taskservice.domain.task.processor.TaskProcessor;
import com.company.taskservice.domain.task.processor.TaskProcessorDelayer;

import java.util.function.Consumer;
public class HammingDistanceTaskProcessor implements TaskProcessor {

    private final HammingDistance hammingDistance;
    private final TaskProcessorDelayer taskProcessorDelayer;
    private final long delayInMillis;

    public HammingDistanceTaskProcessor(HammingDistance hammingDistance,
                                        TaskProcessorDelayer taskProcessorDelayer,
                                        long delayInMillis) {
        this.hammingDistance = hammingDistance;
        this.taskProcessorDelayer = taskProcessorDelayer;
        this.delayInMillis = delayInMillis;
    }

    @Override
    public void processTask(Task task, Consumer<Task> statusChangedConsumer) {
        if (Task.STATUS_NOT_STARTED != task.getStatus()) {
            throw new IllegalArgumentException("Only just created task with status zero can be processed and " +
                    "the given task has status " + task.getStatus());
        }
        String input = task.getInput();
        String pattern = task.getPattern();
        int patternLength = pattern.length();
        int maxIndexForExtractingPatternFromInput = input.length() - patternLength;
        int extractedPatternsFromInputToCheck = maxIndexForExtractingPatternFromInput + 1;

        Result result = new Result(0, patternLength);
        for (int i = 0; i <= maxIndexForExtractingPatternFromInput; i++) {
            taskProcessorDelayer.delayIteration(delayInMillis);

            String extractedPatternFromInput = input.substring(i, i + patternLength);
            int typos = hammingDistance.countDistance(extractedPatternFromInput, pattern);
            if (typos < result.typos()) {
                result = new Result(i, typos);
                if (typos == 0) {
                    break;
                }
            }
            float status = (i + 1.0f) / extractedPatternsFromInputToCheck;
            if (status != 1) {
                changeTaskStatus(task, status, statusChangedConsumer);
            }
        }
        setTaskResult(task, result, statusChangedConsumer);
    }

    private void changeTaskStatus(Task task, float status, Consumer<Task> statusChangedConsumer) {
        task.setStatus(status);
        statusChangedConsumer.accept(task);
    }

    private void setTaskResult(Task task, Result result, Consumer<Task> statusChangedConsumer) {
        task.setTaskResult(new TaskResult(result.position(), result.typos()));
        changeTaskStatus(task, Task.STATUS_COMPLETED, statusChangedConsumer);
    }

    private record Result(int position, int typos) {}

}
