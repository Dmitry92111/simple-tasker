package com.karfidov.simpletasker.backend.task.mapper;

import com.karfidov.simpletasker.backend.task.dto.request.TaskRequestDto;
import com.karfidov.simpletasker.backend.task.dto.response.TaskFullDto;
import com.karfidov.simpletasker.backend.task.dto.response.TaskShortDto;
import com.karfidov.simpletasker.backend.task.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Task fromTaskRequestDto(TaskRequestDto dto);

    TaskFullDto toFullDto(Task task);
    TaskShortDto toShortDto(Task task);

    List<TaskShortDto> toShortDtos(List<Task> tasks);
}
