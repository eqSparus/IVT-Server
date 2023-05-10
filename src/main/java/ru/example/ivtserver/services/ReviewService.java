package ru.example.ivtserver.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import ru.example.ivtserver.entities.Review;
import ru.example.ivtserver.entities.mapper.request.ReviewRequestDto;
import ru.example.ivtserver.exceptions.NoIdException;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Интерфейс сервиса для работы с отзывами о кафедре
 */
public interface ReviewService {

    /**
     * Получает список всех отзывов
     *
     * @return Список {@link List} всех отзывов {@link Review}
     */
    List<Review> getAllReviews();

    /**
     * Добавляет новый отзыв по заданному DTO {@link ReviewRequestDto}
     *
     * @param reviewRequest DTO-объект, содержащий данные для добавления нового отзыва
     * @param img           Файл с изображением для нового отзыва
     * @return Добавленный отзыв {@link Review}
     * @throws IOException Исключение, которое выбрасывается, если произошла ошибка при загрузке изображения
     */
    Review addReview(ReviewRequestDto reviewRequest, MultipartFile img) throws IOException;

    /**
     * Обновляет отзыв по заданному DTO {@link ReviewRequestDto}
     *
     * @param reviewRequest DTO-объект, содержащий данные для обновления отзыва
     * @return Обновленный отзыв {@link Review}
     * @throws NoIdException Исключение, которое выбрасывается, если не найден объект по заданному id
     */
    Review updateReview(ReviewRequestDto reviewRequest) throws NoIdException;

    /**
     * Удаляет отзыв по указанному {@code id}.
     *
     * @param id идентификатор отзыва, который нужно удалить
     * @throws IOException   если произошла ошибка при удалении отзыва
     * @throws NoIdException если отзыв с указанным идентификатором не найден
     */
    void removeReview(UUID id) throws IOException, NoIdException;

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
     * @throws IOException   Исключение, которое выбрасывается, если произошла ошибка при загрузке изображения
     * @throws NoIdException Исключение, которое выбрасывается, если не найден объект по заданному id
     */
    String updateImg(MultipartFile img, UUID id) throws IOException, NoIdException;
}
