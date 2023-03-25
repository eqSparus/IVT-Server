package ru.example.ivtserver.services.couch;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.example.ivtserver.entities.Direction;
import ru.example.ivtserver.entities.dto.DirectionRequestDto;
import ru.example.ivtserver.exceptions.NoIdException;
import ru.example.ivtserver.repositories.DirectionRepository;
import ru.example.ivtserver.services.DirectionService;

import java.util.List;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Log4j2
@Service
public class CouchDirectionService implements DirectionService {

    DirectionRepository directionRepository;

    @Autowired
    public CouchDirectionService(DirectionRepository directionRepository) {
        this.directionRepository = directionRepository;
    }


    @Override
    public Direction create(DirectionRequestDto dto) {

        var direction = Direction.builder()
                .title(dto.getTitle())
                .degree(dto.getDegree())
                .form(dto.getForm())
                .duration(dto.getDuration())
                .build();

        log.info("Новое направление {}", direction);


        return directionRepository.save(direction);
    }

    @Override
    public List<Direction> getAll() {
        return directionRepository.findAll();
    }


    @Override
    public Direction update(DirectionRequestDto dto) throws NoIdException{

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

    @Override
    public void delete(UUID id) {
        log.info("Удаление направления {}", id);
        directionRepository.deleteById(id);
    }
}
