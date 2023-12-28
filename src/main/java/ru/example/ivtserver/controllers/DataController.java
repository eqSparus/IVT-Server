package ru.example.ivtserver.controllers;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.example.ivtserver.entities.dto.SiteContentDto;
import ru.example.ivtserver.services.*;

/**
 * Контролер для получения наполнения сайта.
 */
@CrossOrigin(origins = "http://localhost:8081", methods = RequestMethod.GET)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping(path = "/data")
@RequiredArgsConstructor
public class DataController {

    AboutDepartmentService aboutDepartmentService;
    DepartmentService departmentService;
    DirectionService directionService;
    SiteLinkService siteLinkService;
    EntrantService entrantService;
    PartnerService partnerService;
    ReviewService reviewService;


    /**
     * Контрольная точка для возвращает контент сайта в виде объекта {@link SiteContentDto},
     * включающего информацию о кафедре, ссылках на сайте, описании кафедры,
     * направлениях, информации абитуриенту, партнерах и отзывах.
     *
     * @return Объект {@link SiteContentDto}, содержащий контент сайта.
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public SiteContentDto getCiteContent() {

        var department = departmentService.getDepartment();
        var siteLinks = siteLinkService.getAllLink();
        var aboutDepartment = aboutDepartmentService.getAll();
        var directions = directionService.getAll();
        var entrants = entrantService.getAll();
        var partners = partnerService.getAllPartners();
        var reviews = reviewService.getAllReviews();

        return SiteContentDto.builder()
                .department(new SiteContentDto.MainDepartmentDto(department, siteLinks))
                .aboutDepartment(aboutDepartment)
                .direction(directions)
                .entrants(entrants)
                .partners(partners)
                .reviews(reviews)
                .build();
    }
}
