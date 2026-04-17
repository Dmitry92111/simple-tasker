package com.karfidov.simpletasker.backend.common.web.request.param;

import lombok.Getter;
import org.springframework.data.domain.Sort;

@Getter
public enum SortMode {
    ASC(Sort.Direction.ASC),
    DESC(Sort.Direction.DESC);

    private final Sort.Direction direction;

    SortMode(Sort.Direction direction) {
        this.direction = direction;
    }
}
