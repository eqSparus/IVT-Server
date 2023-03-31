CREATE
COLLECTION `IVT-Bucket`._default.`user-records`;
CREATE
COLLECTION `IVT-Bucket`._default.`site-content`;
CREATE
COLLECTION `IVT-Bucket`._default.`teachers-content`;

CREATE
    PRIMARY INDEX `primary-id-user-records` ON default:`IVT-Bucket`._default.`user-records`;
CREATE
    PRIMARY INDEX `primary-id-site-content` ON default:`IVT-Bucket`._default.`site-content`;
CREATE
    PRIMARY INDEX `primary-id-teachers-content` ON default:`IVT-Bucket`._default.`teachers-content`;

INSERT INTO `IVT-Bucket`._default.`user-records` (KEY, VALUE)
    VALUES (UUID(), {
        "createAt": 1678023811880,
        "email": "kot-1693@mail.ru",
        "password": "$2a$10$NkcyMcfEegK0meWhBuUS3etsssbCTSfpsQpvnA/hSo3exiqwpXJxC",
        "role": "ADMINISTRATOR",
        "updateAt": 1679909305223,
        "_class": "ru.example.ivtserver.entities.User"
        });

INSERT INTO `IVT-Bucket`._default.`site-content` (KEY, VALUE)
    VALUES (UUID(), {
        "_class": "ru.example.ivtserver.entities.Department",
        "address": "Пр. Мира, 11 гл. корпус, каб. П-102",
        "createAt": 1677412916981,
        "email": "ivt@mail.ru",
        "leaderId": "74bae5af-1bbc-48d2-b9dc-55bf9931cc95",
        "phone": "+7-905-096-74-34",
        "slogan": "Научим программировать твоё будущее",
        "title": "Информатика и вычислительная техника",
        "updateAt": 1679821851151
        });

INSERT INTO `IVT-Bucket`._default.`site-content` (KEY, VALUE)
    VALUES (UUID(), {
        "_class": "ru.example.ivtserver.entities.AboutDepartment",
        "createAt": 1677420830633,
        "description": "Специальность имеет множество профилей обучения, предполагающих специализацию на определенных аспектах информационных систем, технологиях, учет отраслевой специфики",
        "icon": "http://localhost:8080/api/v1/images/about/book.svg",
        "title": "Профили",
        "updateAt": 1679939924018
        });

INSERT INTO `IVT-Bucket`._default.`site-content` (KEY, VALUE)
    VALUES (UUID(), {
        "_class": "ru.example.ivtserver.entities.AboutDepartment",
        "createAt": 1677420829617,
        "description": "У наших выпускников огромный выбор профессий. Выпускники могут быть программистами, верстальщиками, web-администраторами. Это лишь меньшая часть списка профессий",
        "icon": "http://localhost:8080/api/v1/images/about/suitcase.svg",
        "title": "Профессии",
        "updateAt": 1679913150970
        });


INSERT INTO `IVT-Bucket`._default.`site-content` (KEY, VALUE)
    VALUES (UUID(), {
        "_class": "ru.example.ivtserver.entities.AboutDepartment",
        "createAt": 1677420826473,
        "description": "Наши выпускники получают широкие знания в области разработки программного обеспечения, построения высоконагруженных информационных систем, программирования мобильных устройств",
        "icon": "http://localhost:8080/api/v1/images/about/human.svg",
        "title": "Выпускники",
        "updateAt": 1679913151647
        });

