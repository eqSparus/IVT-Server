package ru.example.ivtserver.services;


import org.springframework.lang.NonNull;
import ru.example.ivtserver.entities.SiteLink;
import ru.example.ivtserver.entities.dto.SiteLinkRequestDto;

import java.util.List;
import java.util.UUID;

public interface SiteLinkService {

    @NonNull
    SiteLink createLink(@NonNull SiteLinkRequestDto dto);

    @NonNull
    SiteLink updateLink(@NonNull SiteLinkRequestDto dto, @NonNull UUID id);

    @NonNull
    List<SiteLink> getAllLink();

    void deleteLink(@NonNull UUID id);

}
