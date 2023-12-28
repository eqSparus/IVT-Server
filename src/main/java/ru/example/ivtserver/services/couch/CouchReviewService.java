package ru.example.ivtserver.services.couch;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.example.ivtserver.entities.Review;
import ru.example.ivtserver.entities.dto.ReviewDto;
import ru.example.ivtserver.entities.request.ReviewRequest;
import ru.example.ivtserver.exceptions.FailedOperationFileException;
import ru.example.ivtserver.exceptions.NoIdException;
import ru.example.ivtserver.repositories.ReviewRepository;
import ru.example.ivtserver.services.ReviewService;
import ru.example.ivtserver.utils.FileUtil;
import ru.example.ivtserver.utils.ImagePathConstant;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Реализация интерфейса {@link ReviewService} для работы с отзывами о кафедре используя базу данных Couchbase
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
@RequiredArgsConstructor
public class CouchReviewService implements ReviewService {

    @Value("${upload.path.reviews}")
    Path basePath;

    final ReviewRepository reviewRepository;

    /**
     * Получает список всех отзывов
     *
     * @return Список {@link List} всех отзывов {@link ReviewDto}
     */
    @Override
    public List<ReviewDto> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(ReviewDto::of)
                .toList();
    }

    /**
     * Добавляет новый отзыв по заданному DTO {@link ReviewRequest}
     *
     * @param request DTO-объект, содержащий данные для добавления нового отзыва
     * @param img     Файл с изображением для нового отзыва
     * @return Добавленный отзыв {@link ReviewDto}
     * @throws FailedOperationFileException Исключение, которое выбрасывается, если произошла ошибка при загрузке изображения
     */
    @Override
    public ReviewDto addReview(ReviewRequest request, MultipartFile img) throws FailedOperationFileException {
        var fileName = UUID.randomUUID() + "." + FileUtil.getExtension(img.getOriginalFilename());
        var path = basePath.resolve(fileName);
        try {
            FileUtil.saveFile(() -> FileUtil.resizeImg(img, 300, 300), path);

            return Optional.of(Review.of(request, fileName))
                    .map(reviewRepository::save)
                    .map(ReviewDto::of)
                    .orElseThrow();
        } catch (IOException e) {
            throw new FailedOperationFileException("Не удалось сохранить файл");
        }
    }

    /**
     * Обновляет отзыв по заданному DTO {@link ReviewRequest}
     *
     * @param reviewRequest DTO-объект, содержащий данные для обновления отзыва
     * @param id идентификатор отзыва
     * @return Обновленный отзыв {@link ReviewDto}
     * @throws NoIdException Исключение, которое выбрасывается, если не найден объект по заданному id
     */
    @Override
    public ReviewDto updateReview(ReviewRequest reviewRequest, UUID id) throws NoIdException {
        return reviewRepository.findById(id)
                .map(r -> {
                    r.setName(reviewRequest.getName());
                    r.setJobTitle(reviewRequest.getJobTitle());
                    r.setComment(reviewRequest.getComment());
                    return r;
                })
                .map(reviewRepository::save)
                .map(ReviewDto::of)
                .orElseThrow(() -> new NoIdException("Идентификатор не найден"));
    }

    /**
     * Удаляет отзыв по указанному {@code id}.
     *
     * @param id идентификатор отзыва, который нужно удалить
     * @throws FailedOperationFileException   если произошла ошибка при удалении отзыва
     * @throws NoIdException если отзыв с указанным идентификатором не найден
     */
    @Override
    public void removeReview(UUID id) throws FailedOperationFileException, NoIdException {
        var review = reviewRepository.findById(id)
                .orElseThrow(() -> new NoIdException("Идентификатор не найден"));
        try {
            FileUtil.deleteFile(basePath.resolve(review.getImgName()));
            reviewRepository.delete(review);
        } catch (IOException e) {
            throw new FailedOperationFileException("Не удалось удалить файл");
        }
    }

    /**
     * Возвращает ресурс с изображением отзыва с указанным именем файла {@code filename}.
     *
     * @param filename Имя файла с изображением отзыва
     * @return Ресурс с изображением отзыва {@link Resource}
     */
    @Override
    public Resource getImageReview(String filename) {
        return new FileSystemResource(basePath.resolve(filename));
    }

    /**
     * Обновляет изображение для отзыва с указанным {@code id}
     *
     * @param img Файл с новым изображением
     * @param id  отзыва, для которого нужно обновить изображение
     * @return url нового изображения
     * @throws FailedOperationFileException   Исключение, которое выбрасывается, если произошла ошибка при загрузке изображения
     * @throws NoIdException Исключение, которое выбрасывается, если не найден объект по заданному id
     */
    @Override
    public String updateImg(MultipartFile img, UUID id) throws FailedOperationFileException, NoIdException {
        var review = reviewRepository.findById(id)
                .orElseThrow(() -> new NoIdException("Идентификатор не найден"));
        var newFileName = UUID.randomUUID() + "." + FileUtil.getExtension(img.getOriginalFilename());

        try {
            FileUtil.deleteFile(basePath.resolve(review.getImgName()));
            FileUtil.saveFile(() -> FileUtil.resizeImg(img, 300, 300), basePath.resolve(newFileName));
            review.setImgName(newFileName);
            reviewRepository.save(review);
            return ImagePathConstant.BASE_REVIEW_PATH.concat("/").concat(review.getImgName());
        } catch (IOException e) {
            throw new FailedOperationFileException("Не удалось сохранить файл");
        }
    }
}
