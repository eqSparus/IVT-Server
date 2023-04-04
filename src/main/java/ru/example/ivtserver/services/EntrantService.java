package ru.example.ivtserver.services;

import ru.example.ivtserver.entities.Entrant;
import ru.example.ivtserver.entities.mapper.request.EntrantRequestDto;
import ru.example.ivtserver.exceptions.NoIdException;

import java.util.List;
import java.util.UUID;

public interface EntrantService {

    Entrant create(EntrantRequestDto dto);

    Entrant update(EntrantRequestDto dto) throws NoIdException;

    void delete(UUID id);

    List<Entrant> getAll();

}
