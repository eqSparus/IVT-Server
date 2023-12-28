package ru.example.ivtserver.services.couch;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.example.ivtserver.entities.Teacher;
import ru.example.ivtserver.entities.dto.TeacherDto;
import ru.example.ivtserver.entities.request.TeacherRequest;
import ru.example.ivtserver.exceptions.FailedOperationFileException;
import ru.example.ivtserver.exceptions.NoIdException;
import ru.example.ivtserver.repositories.TeacherRepository;
import ru.example.ivtserver.services.TeacherService;
import ru.example.ivtserver.utils.FileUtil;
import ru.example.ivtserver.utils.ImagePathConstant;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Реализация интерфейса {@link TeacherService} для работы с преподавателями кафедры используя базу данных Couchbase
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
@RequiredArgsConstructor
public class CouchTeacherService implements TeacherService {

    @Value("${upload.path.teachers}")
    Path basePath;

    final TeacherRepository teacherRepository;

    /**
     * Создает нового преподавателя на основе данных из объекта {@link TeacherRequest} и изображения профиля.
     *
     * @param request объект с данными для создания преподавателя
     * @param img     изображение профиля преподавателя
     * @return созданный объект {@link TeacherDto}
     * @throws FailedOperationFileException если произошла ошибка при загрузке изображения
     */
    @Override
    public TeacherDto addTeacher(TeacherRequest request, MultipartFile img) throws FailedOperationFileException {
        var fileName = UUID.randomUUID() + "." + FileUtil.getExtension(img.getOriginalFilename());
        var path = basePath.resolve(fileName);

        try {
            FileUtil.saveFile(() -> FileUtil.resizeImg(img, 500, 500), path);

            var teacherDb = teacherRepository.findTeacherLastPosition()
                    .orElseGet(() -> Teacher.builder().position(0).build());

            return Optional.of(Teacher.of(request, fileName, teacherDb.getPosition() + 1))
                    .map(teacherRepository::save)
                    .map(TeacherDto::of)
                    .orElseThrow();
        } catch (IOException e) {
            throw new FailedOperationFileException("Не удалось сохранить файл");
        }
    }

    /**
     * Обновляет существующего преподавателя на основе данных из объекта {@link TeacherRequest}.
     *
     * @param request объект с данными для обновления преподавателя
     * @param id      идентификатор преподавателя
     * @return обновленный объект {@link TeacherDto}
     * @throws NoIdException если преподавателя с указанным идентификатором не найден
     */
    @Override
    public TeacherDto updateTeacher(TeacherRequest request, UUID id) throws NoIdException {
        return teacherRepository.findById(id)
                .map(t -> {
                    t.setFirstName(request.getFirstName());
                    t.setLastName(request.getLastName());
                    t.setMiddleName(request.getMiddleName());
                    t.setPostDepartment(request.getPostDepartment());
                    t.setPostTeacher(request.getPostTeacher());
                    t.setPostAdditional(request.getPostAdditional());
                    return t;
                })
                .map(teacherRepository::save)
                .map(TeacherDto::of)
                .orElseThrow(() -> new NoIdException("Идентификатор не найден"));
    }

    /**
     * Обновляет изображение профиля преподавателя с указанным {@code id}.
     *
     * @param img новое изображение профиля
     * @param id  идентификатор преподавателя
     * @return строка со статусом обновления изображения
     * @throws NoIdException                если преподаватель с указанным id не найден
     * @throws FailedOperationFileException если произошла ошибка при загрузке изображения
     */
    @Override
    public String updateImg(MultipartFile img, UUID id) throws FailedOperationFileException, NoIdException {
        var teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new NoIdException("Идентификатор не найден"));
        var newFileName = UUID.randomUUID() + "." + FileUtil.getExtension(img.getOriginalFilename());

        try {
            FileUtil.deleteFile(basePath.resolve(teacher.getImgName()));
            FileUtil.saveFile(() -> FileUtil.resizeImg(img, 500, 500), basePath.resolve(newFileName));

            teacher.setImgName(newFileName);
            teacherRepository.save(teacher);
            return ImagePathConstant.BASE_TEACHER_PATH.concat("/").concat(teacher.getImgName());
        } catch (IOException e) {
            throw new FailedOperationFileException("Не удалось сохранить файл");
        }
    }

    /**
     * Удаляет преподавателя с указанным {@code id} и его изображение профиля.
     *
     * @param id идентификатор преподавателя, которого нужно удалить
     * @throws FailedOperationFileException если произошла ошибка при удалении изображения профиля
     * @throws NoIdException                если преподаватель с указанным идентификатором не найден
     */

    @Override
    public void removeTeacher(UUID id) throws FailedOperationFileException, NoIdException {
        var teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new NoIdException("Идентификатор не найден"));
        try {
            FileUtil.deleteFile(basePath.resolve(teacher.getImgName()));
            teacherRepository.delete(teacher);
        } catch (IOException e) {
            throw new FailedOperationFileException("Не удалось удалить файл");
        }
    }

    /**
     * Возвращает список преподавателей с указанным смещением и количеством.
     *
     * @param skip количество преподавателей, которые нужно пропустить
     * @param size максимальное количество преподавателей, которые нужно вернуть
     * @return список {@link List} преподавателей с указанным смещением и количеством
     */
    @Override
    public List<TeacherDto> getTeachers(int skip, int size) {
        return teacherRepository.findAllByOrderByPosition(skip, size)
                .map(TeacherDto::of)
                .toList();
    }

    /**
     * Возвращает список преподавателей, которые нужно вернуть, начиная с указанного смещения.
     *
     * @param skip количество преподавателей, которые нужно пропустить
     * @return список {@link List} преподавателей, начиная с указанного смещения
     */
    @Override
    public List<TeacherDto> getTeachers(int skip) {
        var size = teacherRepository.count();
        return getTeachers(skip, (int) size);
    }

    /**
     * Обновляет позицию преподавателя с указанным {@code id}.
     *
     * @param position новая позиция преподавателя
     * @param id       идентификатор преподавателя
     * @return новая позиция преподавателя
     * @throws NoIdException если преподаватель с указанным идентификатором не найден
     */
    @Override
    public int updatePosition(int position, UUID id) throws NoIdException {
        var teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new NoIdException("Идентификатор не найден"));
        teacher.setPosition(position);
        return teacherRepository.save(teacher).getPosition();
    }

    /**
     * Получает изображение профиля преподавателя с указанным именем файла {@code filename}.
     *
     * @param filename имя файла изображения преподавателя
     * @return объект {@link Resource}, представляющий изображение профиля преподавателя
     */
    @Override
    public Resource getImageTeacher(String filename) {
        return new FileSystemResource(basePath.resolve(filename));
    }
}
