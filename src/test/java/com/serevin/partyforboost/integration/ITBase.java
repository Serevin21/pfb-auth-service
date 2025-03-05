package com.serevin.partyforboost.integration;

import com.serevin.partyforboost.service.UserService;
import com.serevin.partyforboost.utils.ApiPaths;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.utility.DockerImageName;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Testcontainers
public class ITBase {

    protected final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    protected MockMvc mvc;
    @Autowired
    protected UserService userService;
    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:15.1"));

    @BeforeAll
    static void startContainer() {
        postgres.start();
    }

    @AfterAll
    static void stopContainer() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.liquibase.change-log", () -> "classpath:db/changelog/test-changelog.yaml");
    }

    protected void createUser(String email, String username, String password){
        String jsonRequest = String.format("""
                    {
                        "email": "%s",
                        "password": "%s",
                        "username": "%s"
                    }
                """, email, password, username);

        try {
            mvc.perform(post(ApiPaths.SIGN_UP)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonRequest))
                    .andExpect(status().isOk());
        } catch (Exception e){
            throw new RuntimeException("Failed to create user", e);
        }
    }

    protected String createEmailJson(String email) {
        return String.format("""
            {
                "email": "%s"
            }
            """, email);
    }

    protected String createEmailAndCodeJson(String email, String code) {
        return String.format("""
                {
                    "email": "%s",
                    "code": "%s"
                }
                """, email, code);
    }

    protected String createEmailAndCodeAndPasswordJson(String email, String code, String password) {
        return String.format("""
                {
                    "email": "%s",
                    "code": "%s",
                    "password": "%s"
                }
                """, email, code, password);
    }
}
