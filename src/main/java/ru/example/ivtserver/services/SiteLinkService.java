package ru.example.ivtserver.services;


import ru.example.ivtserver.entities.dto.SiteLinkDto;
import ru.example.ivtserver.entities.request.SiteLinkRequest;
import ru.example.ivtserver.exceptions.NoIdException;

import java.util.List;
import java.util.UUID;

/**
 * Интерфейс сервиса для работы со ссылками на сайты кафедры
 */
public interface SiteLinkService {


    /**
     * Создает новую сайтовую ссылку на основе данных из объекта {@link SiteLinkRequest}.
     *
     * @param request объект с данными для создания сайтовой ссылки
     * @return созданный объект {@link SiteLinkDto}
     */
    SiteLinkDto createLink(SiteLinkRequest request);

    /**
     * Обновляет существующую сайтовую ссылку на основе данных из объекта {@link SiteLinkRequest}.
     *
     * @param request объект с данными для обновления сайтовой ссылки
     * @param id идентификатор ссылки
     * @return обновленный объект {@link SiteLinkDto}
     * @throws NoIdException если ссылка с указанным id не найдена
     */
    SiteLinkDto updateLink(SiteLinkRequest request, UUID id) throws NoIdException;

    /**
     * Возвращает список всех сайтовых ссылок.
     *
     * @return список {@link List} объектов {@link SiteLinkDto}
     */
    List<SiteLinkDto> getAllLink();

    /**
     * Удаляет сайтовую ссылку с указанным {@code id}.
     *
     * @param id сайтовой ссылки, которую нужно удалить
     */
    void deleteLink(UUID id);

}
