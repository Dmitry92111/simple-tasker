package com.karfidov.simpletasker.backend.task.dto.response;

import com.karfidov.simpletasker.backend.task.model.TaskStatus;

import java.time.Instant;

public record TaskFullDto(Long id,
                          String title,
                          String description,
                          TaskStatus status,
                          Instant createdAt) {
}
