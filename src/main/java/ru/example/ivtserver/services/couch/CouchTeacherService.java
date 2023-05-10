package ru.example.ivtserver.services.couch;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.example.ivtserver.entities.Teacher;
import ru.example.ivtserver.entities.mapper.request.TeacherRequestDto;
import ru.example.ivtserver.exceptions.NoIdException;
import ru.example.ivtserver.repositories.TeacherRepository;
import ru.example.ivtserver.services.TeacherService;
import ru.example.ivtserver.utils.FileUtil;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

/**
 * Реализация интерфейса {@link TeacherService} для работы с преподавателями кафедры используя базу данных Couchbase
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
@Log4j2
public class CouchTeacherService implements TeacherService {

    @Value("${upload.path.teachers}")
    Path basePath;

    final TeacherRepository teacherRepository;

    @Autowired
    public CouchTeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    /**
     * Создает нового преподавателя на основе данных из объекта {@link TeacherRequestDto} и изображения профиля.
     *
     * @param dto объект с данными для создания преподавателя
     * @param img изображение профиля преподавателя
     * @return созданный объект {@link Teacher}
     * @throws IOException если произошла ошибка при загрузке изображения
     */
    @Override
    public Teacher addTeacher(TeacherRequestDto dto, MultipartFile img) throws IOException {
        FileUtil.isExistDir(basePath);
        var fileName = UUID.randomUUID() + "." + FileUtil.getExtension(img.getOriginalFilename());
        var path = basePath.resolve(fileName);
        FileUtil.saveFile(() -> FileUtil.resizeImg(img, 500, 500), path);
        var url = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/teacher/image/")
                .path(fileName)
                .toUriString();

        var teacherDb = teacherRepository.findTeacherLastPosition()
                .orElseGet(() -> Teacher.builder().position(0).build());

        var teacher = Teacher.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .middleName(dto.getMiddleName())
                .postDepartment(dto.getPostDepartment())
                .postTeacher(dto.getPostTeacher())
                .postAdditional(dto.getPostAdditional())
                .urlImg(url)
                .pathImg(path)
                .position(teacherDb.getPosition() + 1)
                .build();
        return teacherRepository.save(teacher);
    }

    /**
     * Обновляет существующего преподавателя на основе данных из объекта {@link TeacherRequestDto}.
     *
     * @param dto объект с данными для обновления преподавателя
     * @return обновленный объект {@link Teacher}
     * @throws NoIdException если преподавателя с указанным идентификатором не найден
     */
    @Override
    public Teacher updateTeacher(TeacherRequestDto dto) throws NoIdException {
        var teacher = teacherRepository.findById(dto.getId())
                .orElseThrow(() -> new NoIdException("Идентификатор не найден"));

        teacher.setFirstName(dto.getFirstName());
        teacher.setLastName(dto.getLastName());
        teacher.setMiddleName(dto.getMiddleName());
        teacher.setPostDepartment(dto.getPostDepartment());
        teacher.setPostTeacher(dto.getPostTeacher());
        teacher.setPostAdditional(dto.getPostAdditional());

        return teacherRepository.save(teacher);
    }

    /**
     * Обновляет изображение профиля преподавателя с указанным {@code id}.
     *
     * @param img новое изображение профиля
     * @param id  идентификатор преподавателя
     * @return строка со статусом обновления изображения
     * @throws NoIdException если преподаватель с указанным id не найден
     * @throws IOException если произошла ошибка при загрузке изображения
     */
    @Override
    public String updateImg(MultipartFile img, UUID id) throws IOException, NoIdException {
        var teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new NoIdException("Идентификатор не найден"));

        FileUtil.replace(() -> FileUtil.resizeImg(img, 500, 500), teacher.getPathImg());
        return teacher.getUrlImg();
    }

    /**
     * Удаляет преподавателя с указанным {@code id} и его изображение профиля.
     *
     * @param id идентификатор преподавателя, которого нужно удалить
     * @throws IOException   если произошла ошибка при удалении изображения профиля
     * @throws NoIdException если преподаватель с указанным идентификатором не найден
     */
    @Override
    public void removeTeacher(UUID id) throws IOException, NoIdException {
        var teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new NoIdException("Идентификатор не найден"));
        FileUtil.deleteFile(teacher.getPathImg());
        teacherRepository.delete(teacher);
    }

    /**
     * Возвращает список преподавателей с указанным смещением и количеством.
     *
     * @param skip количество преподавателей, которые нужно пропустить
     * @param size максимальное количество преподавателей, которые нужно вернуть
     * @return список {@link List} преподавателей с указанным смещением и количеством
     */
    @Override
    public List<Teacher> getTeachers(int skip, int size) {
        return teacherRepository.findAllByOrderByPosition(skip, size);
    }

    /**
     * Возвращает список преподавателей, которые нужно вернуть, начиная с указанного смещения.
     *
     * @param skip количество преподавателей, которые нужно пропустить
     * @return список {@link List} преподавателей, начиная с указанного смещения
     */
    @Override
    public List<Teacher> getTeachers(int skip) {
        var size = teacherRepository.count();
        return getTeachers(skip, (int) size);
    }

    /**
     * Обновляет позицию преподавателя с указанным {@code id}.
     *
     * @param position новая позиция преподавателя
     * @param id идентификатор преподавателя
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
