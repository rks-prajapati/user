package com.test.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.util.FileUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static com.test.user.UserObjectMother.createDefaultUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ApiStarter.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc()
@Slf4j
public class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testRetrieveUser() throws Exception {
        String expectedResponse = readFromFile("get_user_success.json");
        this.mockMvc.perform(get("/user/1111"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    public void testNotFoundUser() throws Exception {
        this.mockMvc.perform(get("/user/1234"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No user found for userId 1234"));
    }

    @Test
    public void testUpdateUser() throws Exception {
        User userToUpdate = createDefaultUser();
        userToUpdate.setFirstName("NewFirstName");
        userToUpdate.setLastName("NewLastName");

        String expectedResponse = readFromFile("update_user_success.json");
        this.mockMvc.perform(
                put("/user/2222")
                    .content(new ObjectMapper().writeValueAsString(userToUpdate))
                .contentType("application/json")
        )
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    public static String readFromFile(String filePath) throws IOException {
        try {
            return new String(FileUtil.class.getClassLoader().getResourceAsStream(filePath).readAllBytes());
        } catch (IOException | NullPointerException e) {
            log.warn("invalid file or path " + filePath);
            throw e;
        }
    }
}
