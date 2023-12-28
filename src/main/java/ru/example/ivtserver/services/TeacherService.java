package ru.example.ivtserver.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import ru.example.ivtserver.entities.dto.TeacherDto;
import ru.example.ivtserver.entities.request.TeacherRequest;
import ru.example.ivtserver.exceptions.FailedOperationFileException;
import ru.example.ivtserver.exceptions.NoIdException;

import java.util.List;
import java.util.UUID;

/**
 * Интерфейс сервиса для работы с преподавателями кафедры
 */
public interface TeacherService {

    /**
     * Создает нового преподавателя на основе данных из объекта {@link TeacherRequest} и изображения профиля.
     *
     * @param request объект с данными для создания преподавателя
     * @param img     изображение профиля преподавателя
     * @return созданный объект {@link TeacherDto}
     * @throws FailedOperationFileException если произошла ошибка при загрузке изображения
     */
    TeacherDto addTeacher(TeacherRequest request, MultipartFile img) throws FailedOperationFileException;

    /**
     * Обновляет существующего преподавателя на основе данных из объекта {@link TeacherRequest}.
     *
     * @param request объект с данными для обновления преподавателя
     * @param  id идентификатор преподавателя
     * @return обновленный объект {@link TeacherDto}
     * @throws NoIdException если преподавателя с указанным идентификатором не найден
     */
    TeacherDto updateTeacher(TeacherRequest request, UUID id) throws NoIdException;

    /**
     * Обновляет изображение профиля преподавателя с указанным {@code id}.
     *
     * @param img новое изображение профиля
     * @param id  идентификатор преподавателя
     * @return строка со статусом обновления изображения
     * @throws NoIdException если преподаватель с указанным id не найден
     * @throws FailedOperationFileException   если произошла ошибка при загрузке изображения
     */
    String updateImg(MultipartFile img, UUID id) throws FailedOperationFileException, NoIdException;

    /**
     * Удаляет преподавателя с указанным {@code id} и его изображение профиля.
     *
     * @param id идентификатор преподавателя, которого нужно удалить
     * @throws FailedOperationFileException   если произошла ошибка при удалении изображения профиля
     * @throws NoIdException если преподаватель с указанным идентификатором не найден
     */
    void removeTeacher(UUID id) throws FailedOperationFileException, NoIdException;


    /**
     * Возвращает список преподавателей с указанным смещением и количеством.
     *
     * @param skip количество преподавателей, которые нужно пропустить
     * @param size максимальное количество преподавателей, которые нужно вернуть
     * @return список {@link List} преподавателей с указанным смещением и количеством
     */
    List<TeacherDto> getTeachers(int skip, int size);

    /**
     * Возвращает список преподавателей, которые нужно вернуть, начиная с указанного смещения.
     *
     * @param skip количество преподавателей, которые нужно пропустить
     * @return список {@link List} преподавателей, начиная с указанного смещения
     */
    List<TeacherDto> getTeachers(int skip);

    /**
     * Обновляет позицию преподавателя с указанным {@code id}.
     *
     * @param position новая позиция преподавателя
     * @param id       идентификатор преподавателя
     * @return новая позиция преподавателя
     * @throws NoIdException если преподаватель с указанным идентификатором не найден
     */
    int updatePosition(int position, UUID id) throws NoIdException;

    /**
     * Получает изображение профиля преподавателя с указанным именем файла {@code filename}.
     *
     * @param filename имя файла изображения преподавателя
     * @return объект {@link Resource}, представляющий изображение профиля преподавателя
     */
    Resource getImageTeacher(String filename);
}
