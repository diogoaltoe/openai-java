package me.diogo.openaijava.resource;

import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import lombok.extern.slf4j.Slf4j;
import me.diogo.openaijava.model.OpenAiModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class OpenAi {
    @Value("${openai-api-key}")
    private String key;


    public List<ChatCompletionChoice> chatRequest(final List<ChatMessage> chatMessages) {
        final var service = new OpenAiService(
                Objects.requireNonNull(key, "Missing OpenAi API Key."),
                Duration.ofSeconds(30));

        final var request = ChatCompletionRequest
                .builder()
                .model(OpenAiModel.GPT_4.getModel())
                .messages(chatMessages)
                .build();

        return service
                .createChatCompletion(request)
                .getChoices();

    }
}
