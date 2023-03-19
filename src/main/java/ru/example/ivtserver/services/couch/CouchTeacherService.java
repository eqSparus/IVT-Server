package ru.example.ivtserver.services.couch;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.example.ivtserver.entities.Teacher;
import ru.example.ivtserver.entities.dto.TeacherDto;
import ru.example.ivtserver.repositories.TeacherRepository;
import ru.example.ivtserver.services.TeacherService;

import java.io.IOException;
import java.nio.file.Files;
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
    public Teacher addTeacher(TeacherDto dto, MultipartFile img) throws IOException {
        var urlImg = saveFile(img);

        var teacher = Teacher.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .middleName(dto.getMiddleName())
                .post(dto.getPost())
                .scientificDegree(dto.getScientificDegree())
                .pathImg(urlImg)
                .build();

        return teacherRepository.save(teacher);
    }

    @Override
    public Teacher updateTeacher(TeacherDto dto, UUID id) {
        var teacher = teacherRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);

        teacher.setFirstName(dto.getFirstName());
        teacher.setLastName(dto.getLastName());
        teacher.setMiddleName(dto.getMiddleName());
        teacher.setPost(dto.getPost());
        teacher.setScientificDegree(dto.getScientificDegree());

        return teacherRepository.save(teacher);
    }

    @Override
    public String updateImg(MultipartFile img, UUID id) throws IOException {
        var teacher = teacherRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);

        var nameForDelete = teacher.getPathImg();
        var pathDelete = nameForDelete.substring(nameForDelete.lastIndexOf("/") + 1);

        deleteFile(pathDelete);
        var urlImg = saveFile(img);
        teacher.setPathImg(urlImg);
        teacherRepository.save(teacher);
        return urlImg;
    }

    @Override
    public void removeTeacher(UUID id) throws IOException {
        var teacher = teacherRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        var nameForDelete = teacher.getPathImg();
        var pathDelete = nameForDelete.substring(nameForDelete.lastIndexOf("/") + 1);

        deleteFile(pathDelete);
        teacherRepository.delete(teacher);
    }

    @Override
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    private String saveFile(MultipartFile img) throws IOException {
        log.info(img.getOriginalFilename());
        var extensionImg = img.getOriginalFilename()
                .substring(img.getOriginalFilename().lastIndexOf("."));
        var imgName = UUID.randomUUID() + extensionImg;
        var pathImg = BASE_PATH.resolve(imgName);
        Files.write(pathImg, img.getBytes());

        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/images/teachers/")
                .path(imgName)
                .toUriString();
    }

    private void deleteFile(String nameFile) throws IOException {
        var pathDelete = BASE_PATH.resolve(nameFile);
        Files.delete(pathDelete);
    }
}
