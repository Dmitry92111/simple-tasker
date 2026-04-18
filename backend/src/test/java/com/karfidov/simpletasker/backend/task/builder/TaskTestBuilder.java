package com.karfidov.simpletasker.backend.task.builder;


import com.karfidov.simpletasker.backend.support.TestTime;
import com.karfidov.simpletasker.backend.task.model.Task;
import com.karfidov.simpletasker.backend.task.model.TaskStatus;

import java.time.Instant;

public final class TaskTestBuilder {
    private TaskTestBuilder() {
    }

    private Long id = 1L;
    private String title = "DEFAULT_TITLE";
    private String description = "DEFAULT_DESCRIPTION";
    private TaskStatus status = TaskStatus.NEW;
    private Instant createdAt = TestTime.DEFAULT_INSTANT;

    public TaskTestBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public TaskTestBuilder withoutId() {
        this.id = null;
        return this;
    }

    public TaskTestBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public TaskTestBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public TaskTestBuilder withStatus(TaskStatus status) {
        this.status = status;
        return this;
    }

    public TaskTestBuilder withCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Task build() {
        Task task = new Task();
        task.setId(id);
        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(status);
        task.setCreatedAt(createdAt);
        return task;
    }

    public static TaskTestBuilder aTask() {
        return new TaskTestBuilder();
    }
}
