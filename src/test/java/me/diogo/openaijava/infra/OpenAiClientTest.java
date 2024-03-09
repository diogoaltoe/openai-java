package me.diogo.openaijava.infra;

import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionChunk;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import io.reactivex.Flowable;
import me.diogo.openaijava.operation.FileConverter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
class OpenAiClientTest {
    @Autowired
    private OpenAiClient<List<ChatCompletionChoice>, Flowable<ChatCompletionChunk>> openAiClient;
    @Autowired
    private FileConverter fileConverter;

    /*
     * CHAT
     */

    @Test
    void successToCallChat() {
        final var response = openAiClient.chatRequest(List.of(createFakeChatSystemMessage(), createFakeChatUserMessages()))
                .getFirst().getMessage().getContent();

        assertNotNull(response);
        assertEquals("Apparel and Fashion", response);
    }

    @Test
    void failedToCallChat() {
        final var openAiClient = new OpenAiClient<>("fake-openai-api-key", null);

        final var exception = assertThrows(IllegalArgumentException.class, () ->
                openAiClient.chatRequest(List.of(createFakeChatSystemMessage(), createFakeChatUserMessages()))
                        .getFirst().getMessage().getContent());
        assertEquals("Error with OpenAI API Key.", exception.getMessage());
    }

    @Test
    void successToCallChatWithBiggerPrompt() throws IOException {
        final var filePath = "message_to_count_tokens.txt";
        final var response = openAiClient.chatRequest(List.of(new ChatMessage(ChatMessageRole.USER.value(), fileConverter.readToString(filePath))))
                .getFirst().getMessage().getContent();

        assertNotNull(response);
    }

    private static ChatMessage createFakeChatSystemMessage() {
        final var systemMessages = List.of(
                "You are a product categorizer and must only answer the name of the product category entered.",
                "Choose a category from the list below:\n" +
                        "    Electronics\n" +
                        "    Apparel and Fashion\n" +
                        "    Home and Kitchen Appliances\n" +
                        "    Beauty and Personal Care\n" +
                        "    Automotive Parts and Accessories\n" +
                        "    Sports and Outdoor Equipment\n" +
                        "    Books and Media\n" +
                        "    Toys and Games\n" +
                        "    Health and Wellness Products\n" +
                        "    Furniture and Home Decor\n",
                        "    Others",
                "If the user asks something other than product categorization, you must respond that you cannot help as your role is only to answer the product category."
        );
        return new ChatMessage(ChatMessageRole.SYSTEM.value(), String.join("\n", systemMessages));
    }

    private static ChatMessage createFakeChatUserMessages() {
        final var userMessages = List.of(
                "sunglasses"
        );
        return new ChatMessage(ChatMessageRole.USER.value(), String.join("\n", userMessages));
    }


    /*
     * ASSISTANT
     */

    @Test
    void successToCallAssistant() {
        final var response = openAiClient.assistantRequest("List two products that you sell?", null);

        assertTrue(response.isPresent());
        assertTrue(OpenAiClient.cleanContent(response).contains("1"));
        assertTrue(OpenAiClient.cleanContent(response).contains("2"));
        assertFalse(OpenAiClient.cleanContent(response).contains("3"));
    }

    @Test
    void successToCallAssistantWithoutHistory() {
        final var response = openAiClient.assistantRequest("Could you list more two?", null);

        assertTrue(response.isPresent());
        assertFalse(OpenAiClient.cleanContent(response).contains("1"));
        assertFalse(OpenAiClient.cleanContent(response).contains("2"));
        assertFalse(OpenAiClient.cleanContent(response).contains("3"));
    }

    @Test
    void successToCallAssistantWithHistory() {
        final var firstResponse = openAiClient.assistantRequest("List two products that you sell?", null);

        assertTrue(firstResponse.isPresent());
        assertTrue(OpenAiClient.cleanContent(firstResponse).contains("1"));
        assertTrue(OpenAiClient.cleanContent(firstResponse).contains("2"));
        assertFalse(OpenAiClient.cleanContent(firstResponse).contains("3"));

        final var response = openAiClient.assistantRequest("Could you list more two?", firstResponse.get().getThreadId());

        assertTrue(response.isPresent());
        assertFalse(OpenAiClient.cleanContent(response).contains("1"));
        assertFalse(OpenAiClient.cleanContent(response).contains("2"));
        assertTrue(OpenAiClient.cleanContent(response).contains("3"));
        assertTrue(OpenAiClient.cleanContent(response).contains("4"));
        assertFalse(OpenAiClient.cleanContent(response).contains("5"));
    }
}