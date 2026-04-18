package com.karfidov.simpletasker.backend.task.builder;

import com.karfidov.simpletasker.backend.task.dto.request.TaskRequestDto;

public class TaskRequestDtoTestBuilder {
    private TaskRequestDtoTestBuilder() {
    }

    private String title = "DEFAULT_TITLE";
    private String description = "DEFAULT_DESCRIPTION";

    public static TaskRequestDtoTestBuilder aRequestDto() {
        return new TaskRequestDtoTestBuilder();
    }

    public TaskRequestDtoTestBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public TaskRequestDtoTestBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public TaskRequestDto build() {
        TaskRequestDto dto = new TaskRequestDto();
        dto.setTitle(title);
        dto.setDescription(description);
        return dto;
    }
}
