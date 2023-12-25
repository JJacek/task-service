package com.company.taskservice.configuration;

import de.cronn.testutils.h2.H2Util;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfiguration {

    @Bean
    public H2Util h2Util() {
        return new H2Util();
    }

}
