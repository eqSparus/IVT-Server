package ru.example.ivtserver.services.couch;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.example.ivtserver.entities.Teacher;
import ru.example.ivtserver.entities.dto.TeacherRequestDto;
import ru.example.ivtserver.exceptions.NoIdException;
import ru.example.ivtserver.repositories.TeacherRepository;
import ru.example.ivtserver.services.TeacherService;
import ru.example.ivtserver.utils.FileUtil;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@Log4j2
public class CouchTeacherService implements TeacherService {

    // TODO Заменить
    static Path BASE_PATH = Path
            .of("build/resources/main/public/images/teachers");

    TeacherRepository teacherRepository;

    @Autowired
    public CouchTeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @Override
    public Teacher addTeacher(TeacherRequestDto dto, MultipartFile img) throws IOException {
        var fileName = FileUtil.saveFile(img, BASE_PATH);
        var url = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/images/teachers/")
                .path(fileName)
                .toUriString();

        var teacher = Teacher.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .middleName(dto.getMiddleName())
                .post(dto.getPost())
                .scientificDegree(dto.getScientificDegree())
                .urlImg(url)
                .pathImg(BASE_PATH.resolve(fileName))
                .build();
        return teacherRepository.save(teacher);
    }

    @Override
    public Teacher updateTeacher(TeacherRequestDto dto) throws NoIdException {
        var teacher = teacherRepository.findById(dto.getId())
                .orElseThrow(() -> new NoIdException("Идентификатор не найден"));

        teacher.setFirstName(dto.getFirstName());
        teacher.setLastName(dto.getLastName());
        teacher.setMiddleName(dto.getMiddleName());
        teacher.setPost(dto.getPost());
        teacher.setScientificDegree(dto.getScientificDegree());

        return teacherRepository.save(teacher);
    }

    @Override
    public String updateImg(MultipartFile img, UUID id) throws IOException, NoIdException {
        var teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new NoIdException("Идентификатор не найден"));

        FileUtil.deleteFile(teacher.getPathImg());
        var fileName = FileUtil.saveFile(img, BASE_PATH);
        var newUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/images/teachers/")
                .path(fileName)
                .toUriString();

        teacher.setPathImg(BASE_PATH.resolve(fileName));
        teacher.setUrlImg(newUrl);
        teacherRepository.save(teacher);
        return newUrl;
    }

    @Override
    public void removeTeacher(UUID id) throws IOException, NoIdException {
        var teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new NoIdException("Идентификатор не найден"));
        FileUtil.deleteFile(teacher.getPathImg());
        teacherRepository.delete(teacher);
    }

    @Override
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }
}
