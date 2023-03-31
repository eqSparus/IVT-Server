package ru.example.ivtserver.services.couch;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.example.ivtserver.entities.Partner;
import ru.example.ivtserver.entities.dto.PartnerRequestDto;
import ru.example.ivtserver.exceptions.NoIdException;
import ru.example.ivtserver.repositories.PartnerRepository;
import ru.example.ivtserver.services.PartnerService;
import ru.example.ivtserver.utils.FileUtil;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@Log4j2
public class CouchPartnerService implements PartnerService {

    // TODO заменить
    static Path BASE_PATH = Path
            .of("build/resources/main/public/images/partners");

    PartnerRepository partnerRepository;

    @Autowired
    public CouchPartnerService(PartnerRepository partnerRepository) {
        this.partnerRepository = partnerRepository;
    }

    @Override
    public Partner addPartner(PartnerRequestDto dto, MultipartFile img) throws IOException {
        var fileName = FileUtil.saveFile(img, BASE_PATH);
        var url = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/images/partners/")
                .path(fileName)
                .toUriString();

        var partner = Partner.builder()
                .href(dto.getHref())
                .urlImg(url)
                .pathImg(BASE_PATH.resolve(fileName))
                .build();

        log.info("{}", partner);

        return partnerRepository.save(partner);
    }

    @Override
    public Partner updatePartner(PartnerRequestDto dto) throws NoIdException {
        var partner = partnerRepository.findById(dto.getId())
                .orElseThrow(() -> new NoIdException("Идентификатор не найден"));
        partner.setHref(dto.getHref());
        return partnerRepository.save(partner);
    }

    @Override
    public String updateImg(MultipartFile img, UUID id) throws IOException, NoIdException {
        var partner = partnerRepository.findById(id)
                .orElseThrow(() -> new NoIdException("Идентификатор не найден"));

        FileUtil.deleteFile(partner.getPathImg());
        var fileName = FileUtil.saveFile(img, BASE_PATH);
        var newUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/images/partners/")
                .path(fileName)
                .toUriString();

        partner.setPathImg(BASE_PATH.resolve(fileName));
        partner.setUrlImg(newUrl);
        partnerRepository.save(partner);
        return newUrl;
    }

    @Override
    public void removePartner(UUID id) throws IOException {
        var partner = partnerRepository.findById(id)
                .orElseThrow(() -> new NoIdException("Идентификатор не найден"));
        FileUtil.deleteFile(partner.getPathImg());
        partnerRepository.delete(partner);
    }

    @Override
    public List<Partner> getAllPartners() {
        return partnerRepository.findAll();
    }
}
