package me.diogo.openaijava.resource;

import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest
class OpenAiTest {
    @Autowired
    private OpenAi openAi;

    @Test
    void get_valid_response() {
        final List<String> systemMessages = List.of(
                "You are a product categorizer and must only answer the name of the product category entered.",
                "If the user asks something other than product categorization, you must respond that you cannot help as your role is only to answer the product category."
        );
        final List<String> userMessages = List.of(
                "sunglasses"
        );
        final var system = new ChatMessage(ChatMessageRole.SYSTEM.value(), String.join("\n", systemMessages));
        final var user = new ChatMessage(ChatMessageRole.USER.value(), String.join("\n", userMessages));

        final String response = openAi.chatRequest(List.of(system, user)).getFirst().getMessage().getContent();
        Assertions.assertNotNull(response);
        Assertions.assertEquals("Accessories", response);
    }
}