package ru.example.ivtserver.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import ru.example.ivtserver.entities.Partner;
import ru.example.ivtserver.entities.mapper.request.PartnerRequestDto;
import ru.example.ivtserver.exceptions.NoIdException;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface PartnerService {

    Partner addPartner(PartnerRequestDto dto, MultipartFile img) throws IOException;

    Partner updatePartner(PartnerRequestDto dto) throws NoIdException;

    String updateImg(MultipartFile img, UUID id) throws IOException, NoIdException;

    void removePartner(UUID id) throws IOException;

    List<Partner> getAllPartners();

    Resource getLogoPartner(String filename) throws IOException;
}
