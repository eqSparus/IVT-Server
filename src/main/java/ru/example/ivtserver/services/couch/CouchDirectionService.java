package ru.example.ivtserver.services.couch;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.example.ivtserver.entities.Direction;
import ru.example.ivtserver.entities.dto.DirectionDto;
import ru.example.ivtserver.entities.request.DirectionRequest;
import ru.example.ivtserver.exceptions.DirectionQuantityLimitException;
import ru.example.ivtserver.exceptions.NoIdException;
import ru.example.ivtserver.repositories.DirectionRepository;
import ru.example.ivtserver.services.DirectionService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Реализация интерфейса {@link DirectionService} для работы с направлениями кафедры используя базу данных Couchbase
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@RequiredArgsConstructor
public class CouchDirectionService implements DirectionService {

    DirectionRepository directionRepository;


    /**
     * Создает новое направление на кафедре по заданному DTO {@link DirectionRequest}.
     *
     * @param request DTO-объект, содержащий данные для создания направления
     * @return Созданное направление {@link DirectionDto}
     * @throws DirectionQuantityLimitException выбрасывается если количество направлений превышает 4
     */
    @Override
    public DirectionDto create(DirectionRequest request) throws DirectionQuantityLimitException {
        if (directionRepository.count() < 4) {
            var directionDb = directionRepository.findDirectionLastPosition()
                    .orElseGet(() -> Direction.builder().position(-1).build());

            return Optional.of(Direction.of(request, directionDb.getPosition() + 1))
                    .map(directionRepository::save)
                    .map(DirectionDto::of)
                    .orElseThrow();
        }
        throw new DirectionQuantityLimitException("Количество направлений не может превышать 4");
    }

    /**
     * Получает список всех направлений
     *
     * @return Список {@link List} направлений {@link DirectionDto}
     */
    @Override
    public List<DirectionDto> getAll() {
        return directionRepository.findAll().stream()
                .map(DirectionDto::of)
                .toList();
    }

    /**
     * Обновляет направление по заданному DTO {@link DirectionRequest}
     *
     * @param request DTO-объект, содержащий данные для обновления
     * @param id идентификатор направления
     * @return Обновленное направление {@link DirectionDto}
     * @throws NoIdException Исключение, которое выбрасывается, если не найден объект по заданному {@code ID}
     */
    @Override
    public DirectionDto update(DirectionRequest request, UUID id) throws NoIdException {
        return directionRepository.findById(id)
                .map(d -> {
                    d.setTitle(request.getTitle());
                    d.setDegree(request.getDegree());
                    d.setForm(request.getForm());
                    d.setDuration(request.getDuration());
                    return d;
                })
                .map(directionRepository::save)
                .map(DirectionDto::of)
                .orElseThrow(() -> new NoIdException("Идентификатор не найден"));
    }

    /**
     * Удаляет направление по заданному {@code id}
     *
     * @param id направления, которое нужно удалить
     */
    @Override
    public void delete(UUID id) {
        directionRepository.findById(id)
                .ifPresent(directionRepository::delete);
    }

    /**
     * Меняет позицию двух направлений местами
     *
     * @param firstId id первого направления
     * @param lastId  id второго направления
     * @return Список {@link List} направлений {@link DirectionDto}
     * @throws NoIdException Исключение, которое выбрасывается, если не найден объект по заданному id
     */
    @Override
    public List<DirectionDto> swapPosition(UUID firstId, UUID lastId) throws NoIdException {
        var directionFirst = directionRepository.findById(firstId)
                .orElseThrow(() -> new NoIdException("Идентификатор не найден"));

        var directionLast = directionRepository.findById(lastId)
                .orElseThrow(() -> new NoIdException("Идентификатор не найден"));

        var firstPos = directionFirst.getPosition();
        var lastPos = directionLast.getPosition();

        directionFirst.setPosition(lastPos);
        directionLast.setPosition(firstPos);

        return Stream.of(
                        directionRepository.save(directionFirst),
                        directionRepository.save(directionLast)
                ).map(DirectionDto::of)
                .toList();
    }
}
