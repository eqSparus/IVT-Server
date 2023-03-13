package ru.example.ivtserver.controllers;


import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.example.ivtserver.entities.dto.SiteContentDto;
import ru.example.ivtserver.services.AboutDepartmentService;
import ru.example.ivtserver.services.DepartmentService;
import ru.example.ivtserver.services.DirectionService;
import ru.example.ivtserver.services.SiteLinkService;

@CrossOrigin(origins = "http://localhost:8081")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping(path = "/data")
public class DataAccessController {

    AboutDepartmentService aboutDepartmentService;
    DepartmentService departmentService;
    DirectionService directionService;
    SiteLinkService siteLinkService;

    @Autowired
    public DataAccessController(AboutDepartmentService aboutDepartmentService,
                                DepartmentService departmentService,
                                DirectionService directionService,
                                SiteLinkService siteLinkService) {
        this.aboutDepartmentService = aboutDepartmentService;
        this.departmentService = departmentService;
        this.directionService = directionService;
        this.siteLinkService = siteLinkService;
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public SiteContentDto getCiteContent() {

        var department = departmentService.getDepartment();
        var siteLinks = siteLinkService.getAllLink();
        var aboutDepartment = aboutDepartmentService.getAll();
        var directions = directionService.getAll();

        return SiteContentDto.builder()
                .department(new SiteContentDto.MainDepartment(department, siteLinks))
                .aboutDepartment(aboutDepartment)
                .direction(directions)
                .build();
    }
}
