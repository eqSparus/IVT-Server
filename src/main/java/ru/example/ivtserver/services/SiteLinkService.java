package ru.example.ivtserver.services;


import ru.example.ivtserver.entities.SiteLink;
import ru.example.ivtserver.entities.mapper.request.SiteLinkRequestDto;
import ru.example.ivtserver.exceptions.NoIdException;

import java.util.List;
import java.util.UUID;

/**
 * Интерфейс сервиса для работы со ссылками на сайты кафедры
 */
public interface SiteLinkService {


    /**
     * Создает новую сайтовую ссылку на основе данных из объекта {@link SiteLinkRequestDto}.
     *
     * @param dto объект с данными для создания сайтовой ссылки
     * @return созданный объект {@link SiteLink}
     */
    SiteLink createLink(SiteLinkRequestDto dto);

    /**
     * Обновляет существующую сайтовую ссылку на основе данных из объекта {@link SiteLinkRequestDto}.
     *
     * @param dto объект с данными для обновления сайтовой ссылки
     * @return обновленный объект {@link SiteLink}
     * @throws NoIdException если ссылка с указанным id не найдена
     */
    SiteLink updateLink(SiteLinkRequestDto dto) throws NoIdException;

    /**
     * Возвращает список всех сайтовых ссылок.
     *
     * @return список {@link List} объектов {@link SiteLink}
     */
    List<SiteLink> getAllLink();

    /**
     * Удаляет сайтовую ссылку с указанным {@code id}.
     *
     * @param id сайтовой ссылки, которую нужно удалить
     */
    void deleteLink(UUID id);

}
