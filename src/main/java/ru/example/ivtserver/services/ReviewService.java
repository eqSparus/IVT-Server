package ru.example.ivtserver.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import ru.example.ivtserver.entities.dto.ReviewDto;
import ru.example.ivtserver.entities.request.ReviewRequest;
import ru.example.ivtserver.exceptions.FailedOperationFileException;
import ru.example.ivtserver.exceptions.NoIdException;

import java.util.List;
import java.util.UUID;

/**
 * Интерфейс сервиса для работы с отзывами о кафедре
 */
public interface ReviewService {

    /**
     * Получает список всех отзывов
     *
     * @return Список {@link List} всех отзывов {@link ReviewDto}
     */
    List<ReviewDto> getAllReviews();

    /**
     * Добавляет новый отзыв по заданному DTO {@link ReviewRequest}
     *
     * @param request DTO-объект, содержащий данные для добавления нового отзыва
     * @param img           Файл с изображением для нового отзыва
     * @return Добавленный отзыв {@link ReviewDto}
     * @throws FailedOperationFileException Исключение, которое выбрасывается, если произошла ошибка при загрузке изображения
     */
    ReviewDto addReview(ReviewRequest request, MultipartFile img) throws FailedOperationFileException;

    /**
     * Обновляет отзыв по заданному DTO {@link ReviewRequest}
     *
     * @param request DTO-объект, содержащий данные для обновления отзыва
     * @param id идентификатор отзыва
     * @return Обновленный отзыв {@link ReviewDto}
     * @throws NoIdException Исключение, которое выбрасывается, если не найден объект по заданному id
     */
    ReviewDto updateReview(ReviewRequest request, UUID id) throws NoIdException;

    /**
     * Удаляет отзыв по указанному {@code id}.
     *
     * @param id идентификатор отзыва, который нужно удалить
     * @throws FailedOperationFileException   если произошла ошибка при удалении отзыва
     * @throws NoIdException если отзыв с указанным идентификатором не найден
     */
    void removeReview(UUID id) throws FailedOperationFileException, NoIdException;

    /**
     * Возвращает ресурс с изображением отзыва с указанным именем файла {@code filename}.
     *
     * @param filename Имя файла с изображением отзыва
     * @return Ресурс с изображением отзыва {@link Resource}
     */
    Resource getImageReview(String filename);

    /**
     * Обновляет изображение для отзыва с указанным {@code id}
     *
     * @param img Файл с новым изображением
     * @param id  отзыва, для которого нужно обновить изображение
     * @return url нового изображения
     * @throws FailedOperationFileException   Исключение, которое выбрасывается, если произошла ошибка при загрузке изображения
     * @throws NoIdException Исключение, которое выбрасывается, если не найден объект по заданному id
     */
    String updateImg(MultipartFile img, UUID id) throws FailedOperationFileException, NoIdException;
}
