package com.company.taskservice.domain.task.processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SimpleTaskProcessorDelayer implements TaskProcessorDelayer {

    @Override
    public void delayIteration(long delayInMillis) {
        if (delayInMillis < 0) {
            throw new IllegalArgumentException("Delay must be grater or equal zero and is " + delayInMillis);
        }

        if (delayInMillis != 0) {
            log.debug("delay task processor iteration by sleep by {} millis", delayInMillis);
            try {
                Thread.sleep(delayInMillis);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
