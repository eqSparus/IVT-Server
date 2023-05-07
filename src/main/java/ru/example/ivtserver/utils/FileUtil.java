package ru.example.ivtserver.utils;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.function.Supplier;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileUtil {

    private FileUtil() {
    }

    public static void saveFile(MultipartFile img, Path path) throws IOException {
        Files.write(path, img.getBytes());
    }

    public static void saveFile(Supplier<byte[]> bytes, Path path) throws IOException {
        Files.write(path, bytes.get());
    }

    public static void deleteFile(Path path) throws IOException {
        Files.delete(path);
    }

    public static void replace(MultipartFile img, Path path) throws IOException {
        Files.copy(new ByteArrayInputStream(img.getBytes()),
                path, StandardCopyOption.REPLACE_EXISTING);
    }

    public static void replace(Supplier<byte[]> bytes, Path path) throws IOException {
        Files.copy(new ByteArrayInputStream(bytes.get()),
                path, StandardCopyOption.REPLACE_EXISTING);
    }

    public static void isExistDir(Path dir) throws IOException {
        if (Files.notExists(dir)) {
            Files.createDirectory(dir);
        }
    }

    public static String getExtension(String path) {
        var index = path.lastIndexOf(".");
        if (index == -1) {
            return "";
        }
        return path.substring(index + 1);
    }

    public static byte[] resizeImg(MultipartFile img, int width, int height) {
        try {
            var originalImg = ImageIO.read(img.getInputStream());
            if (originalImg.getWidth() > width || originalImg.getHeight() > height) {
                var outFile = new ByteArrayOutputStream();
                Thumbnails.of(img.getInputStream())
                        .size(width, height)
                        .outputQuality(1)
                        .toOutputStream(outFile);
                return outFile.toByteArray();
            }
            return img.getBytes();
        } catch (IOException e) {
            throw new RuntimeException("Ошибка с файлом");
        }
    }
}
