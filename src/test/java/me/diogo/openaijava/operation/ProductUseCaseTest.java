package me.diogo.openaijava.operation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductUseCaseTest {
    @Autowired
    ProductUseCase productUseCase;

    @Test
    void get_valid_category() {
        final String category = productUseCase.findCategory("sunglasses");

        Assertions.assertNotNull(category);
        Assertions.assertEquals("Apparel and Fashion", category);
    }
}