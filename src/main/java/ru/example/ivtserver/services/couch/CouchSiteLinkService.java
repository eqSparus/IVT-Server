package ru.example.ivtserver.services.couch;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.example.ivtserver.entities.SiteLink;
import ru.example.ivtserver.entities.dto.SiteLinkDto;
import ru.example.ivtserver.entities.request.SiteLinkRequest;
import ru.example.ivtserver.exceptions.NoIdException;
import ru.example.ivtserver.repositories.SiteLinkRepository;
import ru.example.ivtserver.services.SiteLinkService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Реализация интерфейса {@link SiteLinkService} для работы со ссылками на сайт кафедры используя базу данных Couchbase
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@RequiredArgsConstructor
public class CouchSiteLinkService implements SiteLinkService {

    SiteLinkRepository siteLinkRepository;

    /**
     * Создает новую сайтовую ссылку на основе данных из объекта {@link SiteLinkRequest}.
     *
     * @param request объект с данными для создания сайтовой ссылки
     * @return созданный объект {@link SiteLinkDto}
     */
    @Override
    public SiteLinkDto createLink(SiteLinkRequest request) {
        return Optional.of(request)
                .map(SiteLink::of)
                .map(siteLinkRepository::save)
                .map(SiteLinkDto::of).orElseThrow();
    }

    /**
     * Обновляет существующую сайтовую ссылку на основе данных из объекта {@link SiteLinkRequest}.
     *
     * @param request объект с данными для обновления сайтовой ссылки
     * @param id идентификатор ссылки
     * @return обновленный объект {@link SiteLinkDto}
     * @throws NoIdException если ссылка с указанным id не найдена
     */
    @Override
    public SiteLinkDto updateLink(SiteLinkRequest request, UUID id) throws NoIdException {
        return siteLinkRepository.findById(id)
                .map(l -> {
                    l.setHref(request.getHref());
                    l.setIcon(request.getIcon());
                    return l;
                })
                .map(siteLinkRepository::save)
                .map(SiteLinkDto::of)
                .orElseThrow(() -> new NoIdException("Идентификатор не найден"));
    }

    /**
     * Возвращает список всех сайтовых ссылок.
     *
     * @return список {@link List} объектов {@link SiteLinkDto}
     */
    @Override
    public List<SiteLinkDto> getAllLink() {
        return siteLinkRepository.findAll().stream()
                .map(SiteLinkDto::of)
                .toList();
    }

    /**
     * Удаляет сайтовую ссылку с указанным {@code id}.
     *
     * @param id сайтовой ссылки, которую нужно удалить
     */
    @Override
    public void deleteLink(UUID id) {
        siteLinkRepository.findById(id)
                .ifPresent(siteLinkRepository::delete);
    }
}
