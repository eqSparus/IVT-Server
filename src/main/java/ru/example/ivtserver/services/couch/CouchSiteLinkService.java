package ru.example.ivtserver.services.couch;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.example.ivtserver.entities.SiteLink;
import ru.example.ivtserver.entities.dto.SiteLinkRequestDto;
import ru.example.ivtserver.repositories.SiteLinkRepository;
import ru.example.ivtserver.services.SiteLinkService;

import java.util.List;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Log4j2
@Service
public class CouchSiteLinkService implements SiteLinkService {

    SiteLinkRepository siteLinkRepository;

    @Autowired
    public CouchSiteLinkService(SiteLinkRepository siteLinkRepository) {
        this.siteLinkRepository = siteLinkRepository;
    }


    @Override
    public SiteLink createLink(SiteLinkRequestDto dto) {

        var siteLink = SiteLink.builder()
                .href(dto.getHref())
                .icon(dto.getIcon())
                .build();

        log.info("Новая ссылка {}", siteLink);

        return siteLinkRepository.save(siteLink);
    }


    @Override
    public SiteLink updateLink(SiteLinkRequestDto dto, UUID id) {

        var link = siteLinkRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);

        log.debug("Идентификатор направления {}", id);
        log.debug("Новая информация о ссылке {}", link);

        link.setHref(dto.getHref());
        link.setIcon(dto.getIcon());

        log.debug("Старая информация о ссылке {}", link);

        return siteLinkRepository.save(link);
    }


    @Override
    public List<SiteLink> getAllLink() {
        return siteLinkRepository.findAll();
    }


    @Override
    public void deleteLink(UUID id) {
        log.info("Удаление ссылки {}", id);
        siteLinkRepository.deleteById(id);
    }
}
