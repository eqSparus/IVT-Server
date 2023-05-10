package ru.example.ivtserver.services.couch;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.example.ivtserver.entities.Review;
import ru.example.ivtserver.entities.mapper.request.ReviewRequestDto;
import ru.example.ivtserver.exceptions.NoIdException;
import ru.example.ivtserver.repositories.ReviewRepository;
import ru.example.ivtserver.services.ReviewService;
import ru.example.ivtserver.utils.FileUtil;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

/**
 * Реализация интерфейса {@link ReviewService} для работы с отзывами о кафедре используя базу данных Couchbase
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class CouchReviewService implements ReviewService {

    @Value("${upload.path.reviews}")
    Path basePath;

    final ReviewRepository reviewRepository;

    @Autowired
    public CouchReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    /**
     * Получает список всех отзывов
     *
     * @return Список {@link List} всех отзывов {@link Review}
     */
    @Override
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    /**
     * Добавляет новый отзыв по заданному DTO {@link ReviewRequestDto}
     *
     * @param reviewRequest DTO-объект, содержащий данные для добавления нового отзыва
     * @param img           Файл с изображением для нового отзыва
     * @return Добавленный отзыв {@link Review}
     * @throws IOException Исключение, которое выбрасывается, если произошла ошибка при загрузке изображения
     */
    @Override
    public Review addReview(ReviewRequestDto reviewRequest, MultipartFile img) throws IOException {
        FileUtil.isExistDir(basePath);
        var fileName = UUID.randomUUID() + "." + FileUtil.getExtension(img.getOriginalFilename());
        var path = basePath.resolve(fileName);
        FileUtil.saveFile(img, path);
        var url = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/review/image/")
                .path(fileName)
                .toUriString();

        var review = Review.builder()
                .name(reviewRequest.getName())
                .jobTitle(reviewRequest.getJobTitle())
                .comment(reviewRequest.getComment())
                .urlImg(url)
                .pathImg(path)
                .build();
        return reviewRepository.save(review);
    }

    /**
     * Обновляет отзыв по заданному DTO {@link ReviewRequestDto}
     *
     * @param reviewRequest DTO-объект, содержащий данные для обновления отзыва
     * @return Обновленный отзыв {@link Review}
     * @throws NoIdException Исключение, которое выбрасывается, если не найден объект по заданному id
     */
    @Override
    public Review updateReview(ReviewRequestDto reviewRequest) throws NoIdException {
        var review = reviewRepository.findById(reviewRequest.getId())
                .orElseThrow(() -> new NoIdException("Идентификатор не найден"));

        review.setName(review.getName());
        review.setJobTitle(review.getJobTitle());
        review.setComment(review.getComment());

        return reviewRepository.save(review);
    }

    /**
     * Удаляет отзыв по указанному {@code id}.
     *
     * @param id идентификатор отзыва, который нужно удалить
     * @throws IOException   если произошла ошибка при удалении отзыва
     * @throws NoIdException если отзыв с указанным идентификатором не найден
     */
    @Override
    public void removeReview(UUID id) throws IOException, NoIdException {
        var review = reviewRepository.findById(id)
                .orElseThrow(() -> new NoIdException("Идентификатор не найден"));
        FileUtil.deleteFile(review.getPathImg());
        reviewRepository.delete(review);
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
     * @throws IOException   Исключение, которое выбрасывается, если произошла ошибка при загрузке изображения
     * @throws NoIdException Исключение, которое выбрасывается, если не найден объект по заданному id
     */
    @Override
    public String updateImg(MultipartFile img, UUID id) throws IOException, NoIdException {
        var review = reviewRepository.findById(id)
                .orElseThrow(() -> new NoIdException("Идентификатор не найден"));

        FileUtil.replace(img, review.getPathImg());
        return review.getUrlImg();
    }
}
