package ru.example.ivtserver.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import ru.example.ivtserver.entities.Teacher;
import ru.example.ivtserver.entities.mapper.request.TeacherRequestDto;
import ru.example.ivtserver.exceptions.NoIdException;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface TeacherService {

    Teacher addTeacher(TeacherRequestDto dto, MultipartFile img) throws IOException;

    Teacher updateTeacher(TeacherRequestDto dto) throws NoIdException;

    String updateImg(MultipartFile img, UUID id) throws IOException, NoIdException;

    void removeTeacher(UUID id) throws IOException, NoIdException;

    List<Teacher> getTeachers(int skip, int size);

    List<Teacher> getTeachers(int skip);

    int updatePosition(int position, UUID id) throws NoIdException;

    Resource getImageTeacher(String filename);
}
