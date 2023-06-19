package ru.example.ivtserver.services.couch;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.example.ivtserver.entities.Partner;
import ru.example.ivtserver.entities.mapper.request.PartnerRequestDto;
import ru.example.ivtserver.exceptions.NoIdException;
import ru.example.ivtserver.repositories.PartnerRepository;
import ru.example.ivtserver.services.PartnerService;
import ru.example.ivtserver.utils.FileUtil;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

/**
 * Реализация интерфейса {@link PartnerService} для работы с партерами кафедры используя базу данных Couchbase
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class CouchPartnerService implements PartnerService {

    @Value("${upload.path.partners}")
    Path basePath;

    final PartnerRepository partnerRepository;

    @Autowired
    public CouchPartnerService(PartnerRepository partnerRepository) {
        this.partnerRepository = partnerRepository;
    }

    /**
     * Добавляет нового партнера кафедры по заданному DTO {@link PartnerRequestDto}
     *
     * @param dto DTO-объект, содержащий данные для добавления нового партнера
     * @param img Файл с изображением партнера
     * @return Добавленный партнер {@link Partner}
     * @throws IOException Исключение, которое выбрасывается, если произошла ошибка при загрузке изображения
     */
    @Override
    public Partner addPartner(PartnerRequestDto dto, MultipartFile img) throws IOException {
        var fileName = UUID.randomUUID() + "." + FileUtil.getExtension(img.getOriginalFilename());
        var path = basePath.resolve(fileName);
        FileUtil.saveFile(() -> FileUtil.resizeImg(img, 300, 100), path);

        var url = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/partner/image/")
                .path(fileName)
                .toUriString();

        var partner = Partner.builder()
                .href(dto.getHref())
                .urlImg(url)
                .build();

        return partnerRepository.save(partner);
    }

    /**
     * Обновляет партнера по заданному DTO {@link PartnerRequestDto}
     *
     * @param dto DTO-объект, содержащий данные для обновления партнера
     * @return Обновленный партнер {@link Partner}
     * @throws NoIdException Исключение, которое выбрасывается, если не найден объект по заданному id
     */
    @Override
    public Partner updatePartner(PartnerRequestDto dto) throws NoIdException {
        var partner = partnerRepository.findById(dto.getId())
                .orElseThrow(() -> new NoIdException("Идентификатор не найден"));
        partner.setHref(dto.getHref());
        return partnerRepository.save(partner);
    }

    /**
     * Обновляет изображение для партнера с указанным {@code id}
     *
     * @param img Файл с новым изображением
     * @param id  партнера, для которого нужно обновить изображение
     * @return Строка с url путем изображения
     * @throws IOException   Исключение, которое выбрасывается, если произошла ошибка при загрузке изображения
     * @throws NoIdException Исключение, которое выбрасывается, если не найден объект по заданному id
     */
    @Override
    public String updateImg(MultipartFile img, UUID id) throws IOException, NoIdException {
        var partner = partnerRepository.findById(id)
                .orElseThrow(() -> new NoIdException("Идентификатор не найден"));
        FileUtil.replace(() -> FileUtil.resizeImg(img, 300, 100),
                basePath.resolve(FileUtil.getExtension(partner.getUrlImg(), "/")));
        return partner.getUrlImg();
    }

    /**
     * Удаляет партнера с указанным {@code id}
     *
     * @param id партнера, который нужно удалить
     * @throws IOException Исключение, которое выбрасывается, если произошла ошибка при удалении файла с изображением партнера
     */
    @Override
    public void removePartner(UUID id) throws IOException {
        var partner = partnerRepository.findById(id)
                .orElseThrow(() -> new NoIdException("Идентификатор не найден"));
        FileUtil.deleteFile(basePath.resolve(FileUtil.getExtension(partner.getUrlImg(), "/")));
        partnerRepository.delete(partner);
    }

    /**
     * Получает список всех партнеров
     *
     * @return Список {@link List} всех партнеров {@link Partner}
     */
    @Override
    public List<Partner> getAllPartners() {
        return partnerRepository.findAll();
    }

    /**
     * Возвращает ресурс с изображением партнера с указанным именем файла {@code filename}.
     *
     * @param filename Имя файла с изображением партнера
     * @return Ресурс с изображением партнера {@link Resource}
     * @throws IOException если произошла ошибка с чтением файла
     */
    @Override
    public Resource getLogoPartner(String filename) {
        return new FileSystemResource(basePath.resolve(filename));
    }
}
