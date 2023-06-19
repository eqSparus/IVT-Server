package ru.example.ivtserver.services.couch;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.example.ivtserver.entities.Entrant;
import ru.example.ivtserver.entities.mapper.request.EntrantRequestDto;
import ru.example.ivtserver.exceptions.NoIdException;
import ru.example.ivtserver.repositories.EntrantRepository;
import ru.example.ivtserver.services.EntrantService;

import java.util.List;
import java.util.UUID;

/**
 * Реализация интерфейса {@link EntrantService} для работы с информацией абитуриенту используя базу данных Couchbase
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class CouchEntrantService implements EntrantService {

    EntrantRepository entrantRepository;

    @Autowired
    public CouchEntrantService(EntrantRepository entrantRepository) {
        this.entrantRepository = entrantRepository;
    }

    /**
     * Создает новою информацию абитуриенту по заданному DTO {@link EntrantRequestDto}
     *
     * @param dto DTO-объект, содержащий данные для создания абитуриента
     * @return Созданная информация абитуриенту {@link Entrant}
     */
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

    /**
     * Обновляет информацию абитуриенту по заданному DTO {@link EntrantRequestDto}
     *
     * @param dto DTO-объект, содержащий данные для обновления
     * @return Обновленная информация {@link Entrant}
     * @throws NoIdException Исключение, которое выбрасывается, если не найден объект по заданному ID
     */
    @Override
    public Entrant update(EntrantRequestDto dto) throws NoIdException{
        var entrantDb = entrantRepository.findById(dto.getId())
                .orElseThrow(() -> new NoIdException("Идентификатор не найден"));

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

    /**
     * Удаляет информацию абитуриенту по заданному {@code id}
     *
     * @param id информации, которую нужно удалить
     */
    @Override
    public void delete(UUID id) {
        entrantRepository.deleteById(id);
    }

    /**
     * Получает список всей информации абитуриенту
     *
     * @return Список {@link List} абитуриентов {@link Entrant}
     */
    @Override
    public List<Entrant> getAll() {
        return entrantRepository.findAll();
    }
}
