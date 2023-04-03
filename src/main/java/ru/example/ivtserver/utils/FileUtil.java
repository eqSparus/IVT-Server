package ru.example.ivtserver.utils;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileUtil {

    private FileUtil() {
    }

    public static void saveFile(MultipartFile img, Path path) throws IOException {
        Files.write(path, img.getBytes());
    }

    public static void deleteFile(Path path) throws IOException {
        Files.delete(path);
    }

    public static void replace(MultipartFile img, Path path) throws IOException {
        Files.copy(new ByteArrayInputStream(img.getBytes()),
                path, StandardCopyOption.REPLACE_EXISTING);
    }

    public static String getExtension(String path) {
        var index = path.lastIndexOf(".");
        if (index == -1) {
            return "";
        }
        return path.substring(index + 1);
    }

}
