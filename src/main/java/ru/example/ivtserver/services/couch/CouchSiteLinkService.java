package ru.example.ivtserver.services.couch;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.example.ivtserver.entities.SiteLink;
import ru.example.ivtserver.entities.dto.SiteLinkRequestDto;
import ru.example.ivtserver.repositories.SiteLinkRepository;
import ru.example.ivtserver.services.SiteLinkService;

import java.util.List;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class CouchSiteLinkService implements SiteLinkService {

    SiteLinkRepository siteLinkRepository;

    @Autowired
    public CouchSiteLinkService(SiteLinkRepository siteLinkRepository) {
        this.siteLinkRepository = siteLinkRepository;
    }

    @NonNull
    @Override
    public SiteLink createLink(@NonNull SiteLinkRequestDto dto) {

        var siteLink = SiteLink.builder()
                .href(dto.getHref())
                .icon(dto.getIcon())
                .build();
        siteLinkRepository.save(siteLink);

        return siteLink;
    }

    @NonNull
    @Override
    public SiteLink updateLink(@NonNull SiteLinkRequestDto dto, @NonNull UUID id) {

        var link = siteLinkRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);

        link.setHref(dto.getHref());
        link.setIcon(dto.getIcon());
        siteLinkRepository.save(link);

        return link;
    }

    @NonNull
    @Override
    public List<SiteLink> getAllLink() {
        return siteLinkRepository.findAll();
    }

    @NonNull
    @Override
    public void deleteLink(@NonNull UUID id) {
        siteLinkRepository.deleteById(id);
    }
}
