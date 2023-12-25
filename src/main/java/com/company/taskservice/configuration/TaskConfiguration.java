package com.company.taskservice.configuration;


import com.company.taskservice.domain.task.processor.TaskProcessor;
import com.company.taskservice.domain.task.processor.TaskProcessorDelayer;
import com.company.taskservice.domain.task.processor.hammingdistance.HammingDistanceTaskProcessor;
import com.company.taskservice.domain.task.processor.hammingdistance.SimpleHammingDistance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Slf4j
@Configuration
public class TaskConfiguration {

    @Bean
    public TaskProcessor taskProcessor(TaskProcessorDelayer taskProcessorDelayer,
                                       @Value("${task.processor.delay-iteration:0}") long delayInMillis) {
        log.info("task.processor.delay-iteration is set to {} millis", delayInMillis);
        return new HammingDistanceTaskProcessor(new SimpleHammingDistance(), taskProcessorDelayer, delayInMillis);
    }

}
