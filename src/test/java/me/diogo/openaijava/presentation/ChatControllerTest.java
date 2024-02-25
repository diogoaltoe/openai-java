package me.diogo.openaijava.presentation;

import me.diogo.openaijava.presentation.dto.QuestionRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChatControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testAnswerQuestion() {
        final var request = new QuestionRequest("What is an ecommerce?");
        ResponseEntity<String> response = restTemplate.postForEntity("/chat", request, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("Ecommerce"));
    }
}