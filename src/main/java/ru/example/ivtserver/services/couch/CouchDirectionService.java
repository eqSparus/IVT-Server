package ru.example.ivtserver.services.couch;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.example.ivtserver.entities.Direction;
import ru.example.ivtserver.entities.dto.DirectionRequestDto;
import ru.example.ivtserver.repositories.DirectionRepository;
import ru.example.ivtserver.services.DirectionService;

import java.util.List;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class CouchDirectionService implements DirectionService {

    DirectionRepository directionRepository;

    @Autowired
    public CouchDirectionService(DirectionRepository directionRepository) {
        this.directionRepository = directionRepository;
    }

    @NonNull
    @Override
    public Direction create(@NonNull DirectionRequestDto dto) {

        var direction = Direction.builder()
                .title(dto.getTitle())
                .degree(dto.getDegree())
                .form(dto.getForm())
                .duration(dto.getDuration())
                .build();
        directionRepository.save(direction);

        return direction;
    }

    @Override
    public List<Direction> getAll() {
        return directionRepository.findAll();
    }

    @NonNull
    @Override
    public Direction update(@NonNull DirectionRequestDto dto, @NonNull UUID id) {

        var direction = directionRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);

        direction.setTitle(dto.getTitle());
        direction.setDegree(dto.getDegree());
        direction.setForm(dto.getForm());
        direction.setDuration(dto.getDuration());

        directionRepository.save(direction);

        return direction;
    }

    @Override
    public void delete(@NonNull UUID id) {
        directionRepository.deleteById(id);
    }
}
