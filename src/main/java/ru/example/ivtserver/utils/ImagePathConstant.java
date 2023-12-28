package ru.example.ivtserver.utils;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Константы для url изображений
 */
public final class ImagePathConstant {

    public static final String BASE_TEACHER_PATH = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/teachers/image").toUriString();

    public static final String BASE_PARTNER_PATH = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/partners/image").toUriString();

    public static final String BASE_REVIEW_PATH = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/reviews/image").toUriString();

}
