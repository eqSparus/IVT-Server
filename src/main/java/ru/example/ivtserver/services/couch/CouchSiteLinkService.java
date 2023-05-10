package ru.example.ivtserver.services.couch;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.example.ivtserver.entities.SiteLink;
import ru.example.ivtserver.entities.mapper.request.SiteLinkRequestDto;
import ru.example.ivtserver.exceptions.NoIdException;
import ru.example.ivtserver.repositories.SiteLinkRepository;
import ru.example.ivtserver.services.SiteLinkService;

import java.util.List;
import java.util.UUID;

/**
 * Реализация интерфейса {@link SiteLinkService} для работы со ссылками на сайт кафедры используя базу данных Couchbase
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Log4j2
@Service
public class CouchSiteLinkService implements SiteLinkService {

    SiteLinkRepository siteLinkRepository;

    @Autowired
    public CouchSiteLinkService(SiteLinkRepository siteLinkRepository) {
        this.siteLinkRepository = siteLinkRepository;
    }

    /**
     * Создает новую сайтовую ссылку на основе данных из объекта {@link SiteLinkRequestDto}.
     *
     * @param dto объект с данными для создания сайтовой ссылки
     * @return созданный объект {@link SiteLink}
     */
    @Override
    public SiteLink createLink(SiteLinkRequestDto dto) {

        var siteLink = SiteLink.builder()
                .href(dto.getHref())
                .icon(dto.getIcon())
                .build();

        log.info("Новая ссылка {}", siteLink);

        return siteLinkRepository.save(siteLink);
    }

    /**
     * Обновляет существующую сайтовую ссылку на основе данных из объекта {@link SiteLinkRequestDto}.
     *
     * @param dto объект с данными для обновления сайтовой ссылки
     * @return обновленный объект {@link SiteLink}
     * @throws NoIdException если ссылка с указанным id не найдена
     */
    @Override
    public SiteLink updateLink(SiteLinkRequestDto dto) throws NoIdException{

        var link = siteLinkRepository.findById(dto.getId())
                .orElseThrow(() -> new NoIdException("Идентификатор не найден"));

        log.debug("Идентификатор направления {}", dto.getId());
        log.debug("Новая информация о ссылке {}", link);

        link.setHref(dto.getHref());
        link.setIcon(dto.getIcon());

        log.debug("Старая информация о ссылке {}", link);

        return siteLinkRepository.save(link);
    }

    /**
     * Возвращает список всех сайтовых ссылок.
     *
     * @return список {@link List} объектов {@link SiteLink}
     */
    @Override
    public List<SiteLink> getAllLink() {
        return siteLinkRepository.findAll();
    }

    /**
     * Удаляет сайтовую ссылку с указанным {@code id}.
     *
     * @param id сайтовой ссылки, которую нужно удалить
     */
    @Override
    public void deleteLink(UUID id) {
        log.info("Удаление ссылки {}", id);
        siteLinkRepository.deleteById(id);
    }
}
