package me.diogo.openaijava.presentation;

import me.diogo.openaijava.presentation.dto.QuestionRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChatControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testAnswerQuestion() {
        final var request = new QuestionRequest("What is an ecommerce?");
        final var response = restTemplate.postForEntity("/chat", request, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Ecommerce"));
    }
}