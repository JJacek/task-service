package com.company.taskservice;

import de.cronn.testutils.h2.H2Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseSpringBootTest {

    @Autowired
    private H2Util h2Util;

    @BeforeEach
    void cleanupDatabase() {
        h2Util.resetDatabase();
    }

}
