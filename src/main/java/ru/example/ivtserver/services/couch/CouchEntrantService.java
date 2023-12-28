package ru.example.ivtserver.services.couch;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.example.ivtserver.entities.Entrant;
import ru.example.ivtserver.entities.dto.EntrantDto;
import ru.example.ivtserver.entities.request.EntrantRequest;
import ru.example.ivtserver.exceptions.NoIdException;
import ru.example.ivtserver.repositories.EntrantRepository;
import ru.example.ivtserver.services.EntrantService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Реализация интерфейса {@link EntrantService} для работы с информацией абитуриенту используя базу данных Couchbase
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@RequiredArgsConstructor
public class CouchEntrantService implements EntrantService {

    EntrantRepository entrantRepository;

    /**
     * Создает новою информацию абитуриенту по заданному DTO {@link EntrantRequest}
     *
     * @param request DTO-объект, содержащий данные для создания абитуриента
     * @return Созданная информация абитуриенту {@link EntrantDto}
     */
    @Override
    public EntrantDto create(EntrantRequest request) {
        return Optional.of(Entrant.of(request))
                .map(entrantRepository::save)
                .map(EntrantDto::of).orElseThrow();
    }

    /**
     * Обновляет информацию абитуриенту по заданному DTO {@link EntrantRequest}
     *
     * @param request DTO-объект, содержащий данные для обновления
     * @param id идентификатор информации
     * @return Обновленная информация {@link EntrantDto}
     * @throws NoIdException Исключение, которое выбрасывается, если не найден объект по заданному ID
     */
    @Override
    public EntrantDto update(EntrantRequest request, UUID id) throws NoIdException {
        return entrantRepository.findById(id)
                .map(e -> {
                    var items = request.getItems().stream()
                            .map(Entrant.Item::of)
                            .toList();
                    e.setTitle(request.getTitle());
                    e.setItems(items);
                    return e;
                })
                .map(entrantRepository::save)
                .map(EntrantDto::of)
                .orElseThrow(() -> new NoIdException("Идентификатор не найден"));
    }

    /**
     * Удаляет информацию абитуриенту по заданному {@code id}
     *
     * @param id информации, которую нужно удалить
     */
    @Override
    public void delete(UUID id) {
        entrantRepository.findById(id)
                .ifPresent(entrantRepository::delete);
    }

    /**
     * Получает список всей информации абитуриенту
     *
     * @return Список {@link List} абитуриентов {@link EntrantDto}
     */
    @Override
    public List<EntrantDto> getAll() {
        return entrantRepository.findAll().stream()
                .map(EntrantDto::of)
                .toList();
    }
}
