package ru.example.ivtserver.services.couch;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.example.ivtserver.entities.Entrant;
import ru.example.ivtserver.entities.dto.EntrantRequestDto;
import ru.example.ivtserver.exceptions.NoIdException;
import ru.example.ivtserver.repositories.EntrantRepository;
import ru.example.ivtserver.services.EntrantService;

import java.util.List;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@Log4j2
public class CouchEntrantService implements EntrantService {

    EntrantRepository entrantRepository;

    @Autowired
    public CouchEntrantService(EntrantRepository entrantRepository) {
        this.entrantRepository = entrantRepository;
    }

    @Override
    public Entrant create(EntrantRequestDto dto) {
        var items = dto.getItems().stream()
                .map(d -> {
                    var points = d.getPoints().stream()
                            .map(p -> Entrant.ItemPoint.builder()
                                    .point(p.getPoint())
                                    .build()).toList();
                    return Entrant.Item.builder()
                            .name(d.getName())
                            .points(points)
                            .build();
                }).toList();

        var newEntrant = Entrant.builder()
                .title(dto.getTitle())
                .items(items)
                .build();

        return entrantRepository.save(newEntrant);
    }

    @Override
    public Entrant update(EntrantRequestDto dto) throws NoIdException{
        var entrantDb = entrantRepository.findById(dto.getId())
                .orElseThrow(() -> new NoIdException("Идентификатор не найден"));

        log.info("{}", dto);
        var items = dto.getItems().stream()
                .map(d -> {
                    var points = d.getPoints().stream()
                            .map(p -> Entrant.ItemPoint.builder()
                                    .point(p.getPoint())
                                    .build()).toList();
                    return Entrant.Item.builder()
                            .name(d.getName())
                            .points(points)
                            .build();
                }).toList();

        entrantDb.setTitle(dto.getTitle());
        entrantDb.setItems(items);

        return entrantRepository.save(entrantDb);
    }

    @Override
    public void delete(UUID id) {
        entrantRepository.deleteById(id);
    }

    @Override
    public List<Entrant> getAll() {
        return entrantRepository.findAll();
    }
}
