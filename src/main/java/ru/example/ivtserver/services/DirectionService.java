package ru.example.ivtserver.services;

import org.springframework.lang.NonNull;
import ru.example.ivtserver.entities.Direction;
import ru.example.ivtserver.entities.dto.DirectionRequestDto;

import java.util.List;
import java.util.UUID;

public interface DirectionService {

    @NonNull
    Direction create(@NonNull DirectionRequestDto dto);

    List<Direction> getAll();

    @NonNull
    Direction update(@NonNull DirectionRequestDto dto, @NonNull UUID id);

    void delete(@NonNull UUID id);

}
