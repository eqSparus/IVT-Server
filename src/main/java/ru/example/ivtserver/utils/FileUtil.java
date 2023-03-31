package ru.example.ivtserver.utils;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileUtil {

    private FileUtil() {
    }

    public static String saveFile(MultipartFile img, Path basePath) throws IOException {
        var imgName = UUID.randomUUID() + "." + getExtension(img.getOriginalFilename());
        var pathImg = basePath.resolve(imgName);
        Files.write(pathImg, img.getBytes());
        return imgName;
    }

    public static void deleteFile(Path path) throws IOException {
        Files.delete(path);
    }

    public static String getExtension(String path) {
        var index = path.lastIndexOf(".");
        if (index == -1) {
            return "";
        }
        return path.substring(index + 1);
    }

}
