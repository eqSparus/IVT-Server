package ru.example.ivtserver.services.couch;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.example.ivtserver.entities.Direction;
import ru.example.ivtserver.entities.mapper.request.DirectionRequestDto;
import ru.example.ivtserver.exceptions.NoIdException;
import ru.example.ivtserver.repositories.DirectionRepository;
import ru.example.ivtserver.services.DirectionService;

import java.util.List;
import java.util.UUID;

/**
 * Реализация интерфейса {@link DirectionService} для работы с направлениями кафедры используя базу данных Couchbase
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Log4j2
@Service
public class CouchDirectionService implements DirectionService {

    DirectionRepository directionRepository;

    @Autowired
    public CouchDirectionService(DirectionRepository directionRepository) {
        this.directionRepository = directionRepository;
    }

    /**
     * Создает новое направление на кафедре по заданному DTO {@link DirectionRequestDto}.
     *
     * @param dto DTO-объект, содержащий данные для создания направления
     * @return Созданное направление {@link Direction}
     */
    @Override
    public Direction create(DirectionRequestDto dto) {

        var directionDb = directionRepository.findDirectionLastPosition()
                .orElseGet(() -> Direction.builder().position(-1).build());

        var direction = Direction.builder()
                .title(dto.getTitle())
                .degree(dto.getDegree())
                .form(dto.getForm())
                .duration(dto.getDuration())
                .position(directionDb.getPosition() + 1)
                .build();

        log.info("Новое направление {}", direction);


        return directionRepository.save(direction);
    }

    /**
     * Получает список всех направлений
     *
     * @return Список {@link List} направлений {@link Direction}
     */
    @Override
    public List<Direction> getAll() {
        return directionRepository.findAll();
    }

    /**
     * Обновляет направление по заданному DTO {@link DirectionRequestDto}
     *
     * @param dto DTO-объект, содержащий данные для обновления
     * @return Обновленное направление {@link Direction}
     * @throws NoIdException Исключение, которое выбрасывается, если не найден объект по заданному {@code ID}
     */
    @Override
    public Direction update(DirectionRequestDto dto) throws NoIdException {

        var direction = directionRepository.findById(dto.getId())
                .orElseThrow(() -> new NoIdException("Идентификатор не найден"));

        log.debug("Идентификатор направления {}", dto.getId());
        log.debug("Старая информация о направлении {}", direction);

        direction.setTitle(dto.getTitle());
        direction.setDegree(dto.getDegree());
        direction.setForm(dto.getForm());
        direction.setDuration(dto.getDuration());

        log.debug("Новая информация о направлении {}", direction);

        return directionRepository.save(direction);
    }

    /**
     * Удаляет направление по заданному {@code id}
     *
     * @param id направления, которое нужно удалить
     */
    @Override
    public void delete(UUID id) {
        log.info("Удаление направления {}", id);
        directionRepository.deleteById(id);
    }

    /**
     * Меняет позицию двух направлений местами
     *
     * @param firstId id первого направления
     * @param lastId  id второго направления
     * @return Список {@link List} направлений {@link Direction}
     * @throws NoIdException Исключение, которое выбрасывается, если не найден объект по заданному id
     */
    @Override
    public List<Direction> swapPosition(UUID firstId, UUID lastId) throws NoIdException {
        var directionFirst = directionRepository.findById(firstId)
                .orElseThrow(() -> new NoIdException("Идентификатор не найден"));

        var directionLast = directionRepository.findById(lastId)
                .orElseThrow(() -> new NoIdException("Идентификатор не найден"));

        var firstPos = directionFirst.getPosition();
        var lastPos = directionLast.getPosition();

        directionFirst.setPosition(lastPos);
        directionLast.setPosition(firstPos);

        return List.of(
                directionRepository.save(directionFirst),
                directionRepository.save(directionLast)
        );
    }
}
