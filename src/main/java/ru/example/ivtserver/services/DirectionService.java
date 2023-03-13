package ru.example.ivtserver.services;

import ru.example.ivtserver.entities.Direction;
import ru.example.ivtserver.entities.dto.DirectionRequestDto;

import java.util.List;
import java.util.UUID;

public interface DirectionService {


    Direction create(DirectionRequestDto dto);

    List<Direction> getAll();


    Direction update(DirectionRequestDto dto, UUID id);

    void delete(UUID id);

}
