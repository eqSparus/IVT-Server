package ru.example.ivtserver.services;

import ru.example.ivtserver.entities.Entrant;
import ru.example.ivtserver.entities.dto.EntrantRequestDto;

import java.util.List;
import java.util.UUID;

public interface EntrantService {

    Entrant create(EntrantRequestDto dto);

    Entrant update(EntrantRequestDto dto, UUID id);

    void delete(UUID id);

    List<Entrant> getAll();

}
