package ru.example.ivtserver.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import ru.example.ivtserver.entities.Teacher;
import ru.example.ivtserver.entities.mapper.request.TeacherRequestDto;
import ru.example.ivtserver.exceptions.NoIdException;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Интерфейс сервиса для работы с преподавателями кафедры
 */
public interface TeacherService {

    /**
     * Создает нового преподавателя на основе данных из объекта {@link TeacherRequestDto} и изображения профиля.
     *
     * @param dto объект с данными для создания преподавателя
     * @param img изображение профиля преподавателя
     * @return созданный объект {@link Teacher}
     * @throws IOException если произошла ошибка при загрузке изображения
     */
    Teacher addTeacher(TeacherRequestDto dto, MultipartFile img) throws IOException;

    /**
     * Обновляет существующего преподавателя на основе данных из объекта {@link TeacherRequestDto}.
     *
     * @param dto объект с данными для обновления преподавателя
     * @return обновленный объект {@link Teacher}
     * @throws NoIdException если преподавателя с указанным идентификатором не найден
     */
    Teacher updateTeacher(TeacherRequestDto dto) throws NoIdException;

    /**
     * Обновляет изображение профиля преподавателя с указанным {@code id}.
     *
     * @param img новое изображение профиля
     * @param id  идентификатор преподавателя
     * @return строка со статусом обновления изображения
     * @throws NoIdException если преподаватель с указанным id не найден
     * @throws IOException если произошла ошибка при загрузке изображения
     */
    String updateImg(MultipartFile img, UUID id) throws IOException, NoIdException;

    /**
     * Удаляет преподавателя с указанным {@code id} и его изображение профиля.
     *
     * @param id идентификатор преподавателя, которого нужно удалить
     * @throws IOException   если произошла ошибка при удалении изображения профиля
     * @throws NoIdException если преподаватель с указанным идентификатором не найден
     */
    void removeTeacher(UUID id) throws IOException, NoIdException;


    /**
     * Возвращает список преподавателей с указанным смещением и количеством.
     *
     * @param skip количество преподавателей, которые нужно пропустить
     * @param size максимальное количество преподавателей, которые нужно вернуть
     * @return список {@link List} преподавателей с указанным смещением и количеством
     */
    List<Teacher> getTeachers(int skip, int size);

    /**
     * Возвращает список преподавателей, которые нужно вернуть, начиная с указанного смещения.
     *
     * @param skip количество преподавателей, которые нужно пропустить
     * @return список {@link List} преподавателей, начиная с указанного смещения
     */
    List<Teacher> getTeachers(int skip);

    /**
     * Обновляет позицию преподавателя с указанным {@code id}.
     *
     * @param position новая позиция преподавателя
     * @param id идентификатор преподавателя
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
    Resource getImageTeacher(String filename) throws IOException;
}
