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

    @Override
    public Teacher addTeacher(TeacherRequestDto dto, MultipartFile img) throws IOException {
        FileUtil.isExistDir(basePath);
        var fileName = UUID.randomUUID() + "." + FileUtil.getExtension(img.getOriginalFilename());
        var path = basePath.resolve(fileName);
        FileUtil.saveFile(img, path);
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

    @Override
    public String updateImg(MultipartFile img, UUID id) throws IOException, NoIdException {
        var teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new NoIdException("Идентификатор не найден"));

        FileUtil.replace(img, teacher.getPathImg());
        return teacher.getUrlImg();
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

    @Override
    public int updatePosition(int position, UUID id) throws NoIdException {
        var teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new NoIdException("Идентификатор не найден"));
        teacher.setPosition(position);
        return teacherRepository.save(teacher).getPosition();
    }

    @Override
    public Resource getImageTeacher(String name) throws IOException {
        return new FileSystemResource(basePath.resolve(name));
    }
}
