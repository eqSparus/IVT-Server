package ru.example.ivtserver.services;

import ru.example.ivtserver.entities.Direction;
import ru.example.ivtserver.entities.dto.DirectionRequestDto;
import ru.example.ivtserver.exceptions.NoIdException;

import java.util.List;
import java.util.UUID;

public interface DirectionService {


    Direction create(DirectionRequestDto dto);

    List<Direction> getAll();


    Direction update(DirectionRequestDto dto) throws NoIdException;

    void delete(UUID id);

    List<Direction> swapPosition(UUID firstId, UUID lastId) throws NoIdException;

}
