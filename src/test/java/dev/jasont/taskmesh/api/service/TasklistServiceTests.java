package dev.jasont.taskmesh.api.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("dev")
public class TasklistServiceTests {

    private TasklistService tasklistService;

    public TasklistServiceTests(@Autowired TasklistService tasklistService) {
        this.tasklistService = tasklistService;
    }

    @Test
    public void isInjectible() {
        Assertions.assertNotNull(tasklistService);
    }
}
