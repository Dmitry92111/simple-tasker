package com.karfidov.simpletasker.backend.task.dto.response;

import com.karfidov.simpletasker.backend.task.model.TaskStatus;

import java.time.Instant;

public record TaskShortDto(long id,
                           String title,
                           TaskStatus status,
                           Instant createdAt) {
}
