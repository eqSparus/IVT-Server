package ru.example.ivtserver.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import ru.example.ivtserver.entities.dto.PartnerDto;
import ru.example.ivtserver.entities.request.PartnerRequest;
import ru.example.ivtserver.exceptions.FailedOperationFileException;
import ru.example.ivtserver.exceptions.NoIdException;

import java.util.List;
import java.util.UUID;

/**
 * Интерфейс сервиса для работы с партнерами кафедры
 */
public interface PartnerService {

    /**
     * Добавляет нового партнера кафедры по заданному DTO {@link PartnerRequest}
     *
     * @param request DTO-объект, содержащий данные для добавления нового партнера
     * @param img     Файл с изображением партнера
     * @return Добавленный партнер {@link PartnerDto}
     * @throws FailedOperationFileException Исключение, которое выбрасывается, если произошла ошибка при загрузке изображения
     */
    PartnerDto addPartner(PartnerRequest request, MultipartFile img) throws FailedOperationFileException;

    /**
     * Обновляет партнера по заданному DTO {@link PartnerRequest}
     *
     * @param request DTO-объект, содержащий данные для обновления партнера
     * @param id      идентификатор партнера
     * @return Обновленный партнер {@link PartnerDto}
     * @throws NoIdException Исключение, которое выбрасывается, если не найден объект по заданному id
     */
    PartnerDto updatePartner(PartnerRequest request, UUID id) throws NoIdException;

    /**
     * Обновляет изображение для партнера с указанным {@code id}
     *
     * @param img Файл с новым изображением
     * @param id  партнера, для которого нужно обновить изображение
     * @return Строка с url путем изображения
     * @throws FailedOperationFileException Исключение, которое выбрасывается, если произошла ошибка при загрузке изображения
     * @throws NoIdException                Исключение, которое выбрасывается, если не найден объект по заданному id
     */
    String updateImg(MultipartFile img, UUID id) throws FailedOperationFileException, NoIdException;

    /**
     * Удаляет партнера с указанным {@code id}
     *
     * @param id партнера, который нужно удалить
     * @throws FailedOperationFileException Исключение, которое выбрасывается, если произошла ошибка при удалении файла с изображением партнера
     */
    void removePartner(UUID id) throws FailedOperationFileException;

    /**
     * Получает список всех партнеров
     *
     * @return Список {@link List} всех партнеров {@link PartnerDto}
     */
    List<PartnerDto> getAllPartners();

    /**
     * Возвращает ресурс с изображением партнера с указанным именем файла {@code filename}.
     *
     * @param filename Имя файла с изображением партнера
     * @return Ресурс с изображением партнера {@link Resource}
     */
    Resource getLogoPartner(String filename);
}
