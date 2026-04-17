package com.karfidov.simpletasker.backend.task.integration;

import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;
import com.karfidov.simpletasker.backend.support.AbstractIntegrationTest;
import com.karfidov.simpletasker.backend.task.dto.request.TaskRequestDto;
import com.karfidov.simpletasker.backend.task.fixture.TaskRequestDtoTestBuilder;
import com.karfidov.simpletasker.backend.task.model.Task;
import com.karfidov.simpletasker.backend.task.model.TaskStatus;
import com.karfidov.simpletasker.backend.task.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.assertj.core.api.Assertions.*;



@AutoConfigureMockMvc
@Transactional
public class TaskControllerIT extends AbstractIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void create_shouldReturnCreatedTaskAndPersistIt_whenRequestIsValid() throws Exception{
        TaskRequestDto validRequestDto = TaskRequestDtoTestBuilder.aRequestDto().build();
        String expectedTitle = validRequestDto.getTitle();
        String expectedDescription = validRequestDto.getDescription();

        mockMvc.perform(post("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.title").value(expectedTitle))
                .andExpect(jsonPath("$.description").value(expectedDescription))
                .andExpect(jsonPath("$.status").value(TaskStatus.NEW.name()));

        List<Task> tasks = taskRepository.findAll();
        assertThat(tasks).hasSize(1);

        Task savedTask = tasks.getFirst();

        assertThat(savedTask.getTitle()).isEqualTo(expectedTitle);
        assertThat(savedTask.getDescription()).isEqualTo(expectedDescription);
        assertThat(savedTask.getStatus()).isEqualTo(TaskStatus.NEW);
    }
}
