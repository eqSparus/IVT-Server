package ru.example.ivtserver.services.couch;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.example.ivtserver.entities.Partner;
import ru.example.ivtserver.entities.dto.PartnerDto;
import ru.example.ivtserver.entities.request.PartnerRequest;
import ru.example.ivtserver.exceptions.FailedOperationFileException;
import ru.example.ivtserver.exceptions.NoIdException;
import ru.example.ivtserver.repositories.PartnerRepository;
import ru.example.ivtserver.services.PartnerService;
import ru.example.ivtserver.utils.FileUtil;
import ru.example.ivtserver.utils.ImagePathConstant;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Реализация интерфейса {@link PartnerService} для работы с партерами кафедры используя базу данных Couchbase
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
@RequiredArgsConstructor
public class CouchPartnerService implements PartnerService {

    @Value("${upload.path.partners}")
    Path basePath;

    final PartnerRepository partnerRepository;

    /**
     * Добавляет нового партнера кафедры по заданному DTO {@link PartnerRequest}
     *
     * @param request DTO-объект, содержащий данные для добавления нового партнера
     * @param img     Файл с изображением партнера
     * @return Добавленный партнер {@link PartnerDto}
     * @throws FailedOperationFileException Исключение, которое выбрасывается, если произошла ошибка при загрузке изображения
     */
    @Override
    public PartnerDto addPartner(PartnerRequest request, MultipartFile img) throws FailedOperationFileException {
        var fileName = UUID.randomUUID() + "." + FileUtil.getExtension(img.getOriginalFilename());
        var path = basePath.resolve(fileName);
        try {
            FileUtil.saveFile(() -> FileUtil.resizeImg(img, 300, 100), path);

            return Optional.of(Partner.of(request, fileName))
                    .map(partnerRepository::save)
                    .map(PartnerDto::of)
                    .orElseThrow();

        } catch (IOException e) {
            throw new FailedOperationFileException("Не удалось сохранить файл");
        }
    }

    /**
     * Обновляет партнера по заданному DTO {@link PartnerRequest}
     *
     * @param request DTO-объект, содержащий данные для обновления партнера
     * @param id идентификатор партнера
     * @return Обновленный партнер {@link PartnerDto}
     * @throws NoIdException Исключение, которое выбрасывается, если не найден объект по заданному id
     */
    @Override
    public PartnerDto updatePartner(PartnerRequest request, UUID id) throws NoIdException {
        return partnerRepository.findById(id)
                .map(p->{
                    p.setHref(request.getHref());
                    return p;
                })
                .map(partnerRepository::save)
                .map(PartnerDto::of)
                .orElseThrow(() -> new NoIdException("Идентификатор не найден"));
    }

    /**
     * Обновляет изображение для партнера с указанным {@code id}
     *
     * @param img Файл с новым изображением
     * @param id  партнера, для которого нужно обновить изображение
     * @return Строка с url путем изображения
     * @throws FailedOperationFileException   Исключение, которое выбрасывается, если произошла ошибка при загрузке изображения
     * @throws NoIdException Исключение, которое выбрасывается, если не найден объект по заданному id
     */
    @Override
    public String updateImg(MultipartFile img, UUID id) throws FailedOperationFileException, NoIdException {
        var partner = partnerRepository.findById(id)
                .orElseThrow(() -> new NoIdException("Идентификатор не найден"));
        var newFileName = UUID.randomUUID() + "." + FileUtil.getExtension(img.getOriginalFilename());

        try {
            FileUtil.deleteFile(basePath.resolve(partner.getImgName()));
            FileUtil.saveFile(() -> FileUtil.resizeImg(img, 300, 300), basePath.resolve(newFileName));
            partner.setImgName(newFileName);
            partnerRepository.save(partner);
            return ImagePathConstant.BASE_PARTNER_PATH.concat("/").concat(partner.getImgName());
        } catch (IOException e) {
            throw new FailedOperationFileException("Не удалось сохранить файл");
        }
    }

    /**
     * Удаляет партнера с указанным {@code id}
     *
     * @param id партнера, который нужно удалить
     * @throws FailedOperationFileException Исключение, которое выбрасывается, если произошла ошибка при удалении файла с изображением партнера
     */
    @Override
    public void removePartner(UUID id) throws FailedOperationFileException {
        var partner = partnerRepository.findById(id)
                .orElseThrow(() -> new NoIdException("Идентификатор не найден"));
        try {
            FileUtil.deleteFile(basePath.resolve(partner.getImgName()));
            partnerRepository.delete(partner);
        } catch (IOException e) {
            throw new FailedOperationFileException("Не удалось удалить файл");
        }
    }

    /**
     * Получает список всех партнеров
     *
     * @return Список {@link List} всех партнеров {@link PartnerDto}
     */
    @Override
    public List<PartnerDto> getAllPartners() {
        return partnerRepository.findAll().stream()
                .map(PartnerDto::of)
                .toList();
    }

    /**
     * Возвращает ресурс с изображением партнера с указанным именем файла {@code filename}.
     *
     * @param filename Имя файла с изображением партнера
     * @return Ресурс с изображением партнера {@link Resource}
     */
    @Override
    public Resource getLogoPartner(String filename) {
        return new FileSystemResource(basePath.resolve(filename));
    }
}
