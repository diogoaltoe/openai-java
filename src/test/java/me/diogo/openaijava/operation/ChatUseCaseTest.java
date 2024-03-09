package me.diogo.openaijava.operation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ChatUseCaseTest {
    @Autowired
    ChatUseCase chatUseCase;

    @Test
    void successToGetCategory() {
        final String category = chatUseCase.findCategory("sunglasses");

        Assertions.assertNotNull(category);
        Assertions.assertEquals("Apparel and Fashion", category);
    }
}