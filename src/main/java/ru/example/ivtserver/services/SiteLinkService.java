package ru.example.ivtserver.services;


import ru.example.ivtserver.entities.SiteLink;
import ru.example.ivtserver.entities.dto.SiteLinkRequestDto;
import ru.example.ivtserver.exceptions.NoIdException;

import java.util.List;
import java.util.UUID;

public interface SiteLinkService {


    SiteLink createLink(SiteLinkRequestDto dto);


    SiteLink updateLink(SiteLinkRequestDto dto) throws NoIdException;


    List<SiteLink> getAllLink();

    void deleteLink(UUID id);

}
