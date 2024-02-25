package me.diogo.openaijava.infra;

import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionChunk;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import io.reactivex.Flowable;
import me.diogo.openaijava.operation.FileConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;


@SpringBootTest
class OpenAiTest {
    @Autowired
    private OpenAi<List<ChatCompletionChoice>, Flowable<ChatCompletionChunk>> openAi;
    @Autowired
    private FileConverter fileConverter;

    @Test
    void successToCallChat() {
        final String response = openAi.chatRequest(List.of(createFakeChatSystemMessage(), createFakeChatUserMessages()))
                .getFirst().getMessage().getContent();

        Assertions.assertNotNull(response);
        Assertions.assertEquals("eyewear".toLowerCase(), response.toLowerCase());
    }

    @Test
    void failedToCallChat() {
        final var openAi = new OpenAi<>();
        openAi.setKey("fake-openai-api-key");

        final var exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                openAi.chatRequest(List.of(createFakeChatSystemMessage(), createFakeChatUserMessages()))
                        .getFirst().getMessage().getContent());
        Assertions.assertEquals("Error with OpenAI API Key.", exception.getMessage());
    }

    @Test
    void successToCallChatWithBiggerPrompt() throws IOException {
        final String filePath = "message_to_count_tokens.txt";
        final String response = openAi.chatRequest(List.of(new ChatMessage(ChatMessageRole.USER.value(), fileConverter.readToString(filePath))))
                .getFirst().getMessage().getContent();

        Assertions.assertNotNull(response);
    }

    private static ChatMessage createFakeChatSystemMessage() {
        final List<String> systemMessages = List.of(
                "You are a product categorizer and must only answer the name of the product category entered.",
                "If the user asks something other than product categorization, you must respond that you cannot help as your role is only to answer the product category."
        );
        return new ChatMessage(ChatMessageRole.SYSTEM.value(), String.join("\n", systemMessages));
    }

    private static ChatMessage createFakeChatUserMessages() {
        final List<String> userMessages = List.of(
                "sunglasses"
        );
        return new ChatMessage(ChatMessageRole.USER.value(), String.join("\n", userMessages));
    }
}