package me.diogo.openaijava.operation;

import me.diogo.openaijava.model.OpenAiModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class TokenCounterTest {
    @Autowired
    private FileConverter fileConverter;

    @Test
    void success_to_count_tokens() {
        final String prompt = "You are a product categorizer and must only answer the name of the product category entered.";

        Assertions.assertEquals(18, TokenCounter.count(OpenAiModel.getDefault().getName(), prompt));
    }

    @Test
    void success_to_count_tokens_from_file() throws IOException {
        final String filePath = "message_to_count_tokens.txt";

        Assertions.assertEquals(17799, TokenCounter.count(OpenAiModel.getDefault().getName(), fileConverter.readToString(filePath)));
    }

    @Test
    void failed_to_count_tokens() {
        final String model = "fake-gpt";
        final String prompt = "You are a product categorizer and must only answer the name of the product category entered.";

        Assertions.assertEquals(0, TokenCounter.count(model, prompt));
    }
}