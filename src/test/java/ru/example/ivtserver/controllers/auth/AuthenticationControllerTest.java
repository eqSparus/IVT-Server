package ru.example.ivtserver.controllers.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.example.ivtserver.entities.mapper.auth.request.UserRequestDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest {

    MockMvc mockMvc;
    ObjectMapper mapper;

    @Autowired
    public AuthenticationControllerTest(MockMvc mockMvc,
                                        ObjectMapper mapper) {
        this.mockMvc = mockMvc;
        this.mapper = mapper;
    }

    @Test
    void login() throws Exception {

        var user = new UserRequestDto("sparus-1693@yandex.ru", "rotroot");

        var result = mockMvc.perform(post("/login")
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        var response = result.getResponse();
        var refresh = response.getCookie("refresh");
        var access = response.getContentAsString();
        var contentType = response.getContentType();
        assertNotNull(refresh);
        assertNotNull(access);
        assertEquals(MediaType.APPLICATION_JSON_VALUE, contentType);
    }
}
