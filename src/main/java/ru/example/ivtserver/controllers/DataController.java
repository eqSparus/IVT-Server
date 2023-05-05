package ru.example.ivtserver.controllers;


import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.example.ivtserver.entities.mapper.SiteContentDto;
import ru.example.ivtserver.services.*;

@CrossOrigin(origins = "http://localhost:8081")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping(path = "/data")
public class DataController {

    AboutDepartmentService aboutDepartmentService;
    DepartmentService departmentService;
    DirectionService directionService;
    SiteLinkService siteLinkService;
    EntrantService entrantService;
    PartnerService partnerService;

    ReviewService reviewService;

    @Autowired
    public DataController(AboutDepartmentService aboutDepartmentService,
                          DepartmentService departmentService,
                          DirectionService directionService,
                          SiteLinkService siteLinkService,
                          EntrantService entrantService,
                          PartnerService partnerService,
                          ReviewService reviewService) {
        this.aboutDepartmentService = aboutDepartmentService;
        this.departmentService = departmentService;
        this.directionService = directionService;
        this.siteLinkService = siteLinkService;
        this.entrantService = entrantService;
        this.partnerService = partnerService;
        this.reviewService = reviewService;
    }


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
                .department(new SiteContentDto.MainDepartment(department, siteLinks))
                .aboutDepartment(aboutDepartment)
                .direction(directions)
                .entrants(entrants)
                .partners(partners)
                .reviews(reviews)
                .build();
    }
}
