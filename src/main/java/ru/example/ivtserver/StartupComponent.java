package ru.example.ivtserver;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.example.ivtserver.utils.FileUtil;

import java.nio.file.Path;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Component
public class StartupComponent implements CommandLineRunner {

    @Value("${upload.path.default-dir}")
    Path pathImages;

    @Value("${upload.path.partners}")
    Path pathPartners;

    @Value("${upload.path.reviews}")
    Path pathReviews;

    @Value("${upload.path.teachers}")
    Path pathTeachers;


    @Override
    public void run(String... args) throws Exception {
        FileUtil.existDir(pathImages);
        FileUtil.existDir(pathPartners);
        FileUtil.existDir(pathReviews);
        FileUtil.existDir(pathTeachers);
    }
}


