package com.karfidov.simpletasker.backend.task.web.request.param;

import lombok.Getter;

@Getter
public enum TaskSortField {
    CREATED_AT("createdAt");

    private final String property;

    TaskSortField(String property) {
        this.property = property;
    }
}
