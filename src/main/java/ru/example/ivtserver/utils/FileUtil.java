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

/**
 * Класс помощник для работы с файлами.
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileUtil {


    private FileUtil() {
    }

    /**
     * Записывает файл в указанный путь.
     *
     * @param img  Файл для записи.
     * @param path Путь для записи файла.
     * @throws IOException Если произошла ошибка ввода-вывода.
     */
    public static void saveFile(MultipartFile img, Path path) throws IOException {
        Files.write(Path.of("").resolve(path), img.getBytes());
    }

    /**
     * Записывает данные, возвращаемые поставщиком, в указанный путь.
     *
     * @param bytes Поставщик байтов для записи.
     * @param path  Путь для записи файла.
     * @throws IOException Если произошла ошибка ввода-вывода.
     */
    public static void saveFile(Supplier<byte[]> bytes, Path path) throws IOException {
        Files.write(path, bytes.get());
    }

    /**
     * Удаляет файл по указанному пути.
     *
     * @param path Путь к файлу.
     * @throws IOException Если произошла ошибка ввода-вывода.
     */
    public static void deleteFile(Path path) throws IOException {
        Files.delete(path);
    }

    /**
     * Заменяет файл в указанном пути заданным файлом.
     *
     * @param img  Заменяющий файл.
     * @param path Путь к файлу, который нужно заменить.
     * @throws IOException Если произошла ошибка ввода-вывода.
     */
    public static void replace(MultipartFile img, Path path) throws IOException {
        Files.copy(new ByteArrayInputStream(img.getBytes()),
                path, StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Заменяет файл в указанном пути данными, возвращаемыми поставщиком.
     *
     * @param bytes Поставщик байтов для замены содержимого файла.
     * @param path  Путь к заменяемому файлу.
     * @throws IOException Если произойдет ошибка ввода-вывода.
     */
    public static void replace(Supplier<byte[]> bytes, Path path) throws IOException {
        Files.copy(new ByteArrayInputStream(bytes.get()),
                path, StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Возвращает расширение файла из указанного пути.
     *
     * @param path Путь к файлу.
     * @return Расширение файла или если не удалось найти расширение то пустую строку.
     */
    public static String getExtension(String path) {
        return getExtension(path, ".");
    }

    /**
     * Возвращает расширение файла
     *
     * @param path      название файла
     * @param delimiter разделитель
     * @return расширение файла
     */
    public static String getExtension(String path, String delimiter) {
        var index = path.lastIndexOf(delimiter);
        if (index == -1) {
            return "";
        }
        return path.substring(index + 1);
    }

    /**
     * Проверяет существует ли директория и если нет создает её
     *
     * @param path путь к директории
     */
    public static void existDir(Path path) throws IOException {
        if (Files.notExists(path)) {
            Files.createDirectory(path);
        }
    }

    /**
     * Изменяет размер изображения в соответствии с указанными шириной и высотой.
     *
     * @param img    Исходное изображение в формате {@link MultipartFile}.
     * @param width  Новая ширина изображения.
     * @param height Новая высота изображения.
     * @return Байтовый массив измененного изображения.
     * @throws RuntimeException Если произойдет ошибка с файлом.
     */
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
