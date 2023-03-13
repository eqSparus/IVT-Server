package ru.example.ivtserver.services;


import ru.example.ivtserver.entities.SiteLink;
import ru.example.ivtserver.entities.dto.SiteLinkRequestDto;

import java.util.List;
import java.util.UUID;

public interface SiteLinkService {


    SiteLink createLink(SiteLinkRequestDto dto);


    SiteLink updateLink(SiteLinkRequestDto dto, UUID id);


    List<SiteLink> getAllLink();

    void deleteLink(UUID id);

}
