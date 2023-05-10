package ru.example.ivtserver.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import ru.example.ivtserver.entities.Partner;
import ru.example.ivtserver.entities.mapper.request.PartnerRequestDto;
import ru.example.ivtserver.exceptions.NoIdException;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Интерфейс сервиса для работы с партнерами кафедры
 */
public interface PartnerService {

    /**
     * Добавляет нового партнера кафедры по заданному DTO {@link PartnerRequestDto}
     * @param dto DTO-объект, содержащий данные для добавления нового партнера
     * @param img Файл с изображением партнера
     * @return Добавленный партнер {@link Partner}
     * @throws IOException Исключение, которое выбрасывается, если произошла ошибка при загрузке изображения
     */
    Partner addPartner(PartnerRequestDto dto, MultipartFile img) throws IOException;

    /**
     * Обновляет партнера по заданному DTO {@link PartnerRequestDto}
     * @param dto DTO-объект, содержащий данные для обновления партнера
     * @return Обновленный партнер {@link Partner}
     * @throws NoIdException Исключение, которое выбрасывается, если не найден объект по заданному id
     */
    Partner updatePartner(PartnerRequestDto dto) throws NoIdException;

    /**
     * Обновляет изображение для партнера с указанным {@code id}
     * @param img Файл с новым изображением
     * @param id партнера, для которого нужно обновить изображение
     * @return Строка с url путем изображения
     * @throws IOException Исключение, которое выбрасывается, если произошла ошибка при загрузке изображения
     * @throws NoIdException Исключение, которое выбрасывается, если не найден объект по заданному id
     */
    String updateImg(MultipartFile img, UUID id) throws IOException, NoIdException;

    /**
     * Удаляет партнера с указанным {@code id}
     * @param id партнера, который нужно удалить
     * @throws IOException Исключение, которое выбрасывается, если произошла ошибка при удалении файла с изображением партнера
     */
    void removePartner(UUID id) throws IOException;

    /**
     * Получает список всех партнеров
     * @return Список {@link List} всех партнеров {@link Partner}
     */
    List<Partner> getAllPartners();

    /**
     * Возвращает ресурс с изображением партнера с указанным именем файла {@code filename}.
     * @param filename Имя файла с изображением партнера
     * @return Ресурс с изображением партнера {@link Resource}
     * @throws IOException если произошла ошибка с чтением файла
     */
    Resource getLogoPartner(String filename) throws IOException;
}
