package me.diogo.openaijava.resource;

import com.theokanning.openai.OpenAiHttpException;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionChunk;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import io.reactivex.Flowable;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.diogo.openaijava.model.OpenAiModel;
import me.diogo.openaijava.operation.TokenCounter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
public class OpenAi<T extends List<ChatCompletionChoice>, S extends Flowable<ChatCompletionChunk>> {
    @Setter
    @Value("${openai-api-key}")
    protected String key;

    public T chatRequest(final List<ChatMessage> chatMessages) {
        return prepareRequest(chatMessages, false);
    }

    public S chatRequestStream(final List<ChatMessage> chatMessages) {
        return prepareRequest(chatMessages, true);
    }

    private <R> R prepareRequest(final List<ChatMessage> chatMessages, final boolean isStream) {
        final var model = selectModel(chatMessages);

        final var service = new OpenAiService(
                Objects.requireNonNull(key, "Missing OpenAi API Key."),
                Duration.ofSeconds(30));

        final var request = ChatCompletionRequest
                .builder()
                .model(model.getName())
                .messages(chatMessages)
                .stream(isStream)
                .build();

        return performRequest(service, request, 0, 5, isStream);
    }

    private static OpenAiModel selectModel(final List<ChatMessage> chatMessages) {
        final var model = OpenAiModel.getDefault();
        log.info("Using as default model: {}", model);

        int inputTokens = TokenCounter.count(model.getName(), chatMessages.stream().map(ChatMessage::getContent).collect(Collectors.joining("\n")));
        log.info("The prompt has {} tokens.", inputTokens);

        int outputTokens = 2048;
        int totalTokens = inputTokens + outputTokens;

        if (totalTokens > model.getTokens()) {
            log.info("The current model cannot handle the prompt.");

            final var mostTokensModel = OpenAiModel.mostTokens();
            if (totalTokens > mostTokensModel.getTokens())
                throw new IllegalArgumentException("The prompt has more tokens (" + inputTokens + ") than the Model with Most Tokens current available (" + mostTokensModel.getTokens() + ").");

            log.info("Changed model to: {}", mostTokensModel);
            return mostTokensModel;
        }

        return model;
    }

    private <R> R performRequest(final OpenAiService service, final ChatCompletionRequest request, final int attempt, final int waitInSeconds, final boolean isStream) {
        if(attempt > 4)
            throw new RuntimeException("Error with the OpenAI API. All attempts failed.");

        try {
            return isStream
                    ? (R) service.streamChatCompletion(request)
                    : (R) service.createChatCompletion(request).getChoices();
        }
        catch (OpenAiHttpException e) {
            switch (e.statusCode) {
                case 401 -> throw new IllegalArgumentException("Error with OpenAI API Key.", e);
                case 429 -> {
                    return prepareNewAttempt(service, request, attempt, waitInSeconds, "Rate limit reached.", isStream);
                }
                case 500, 503 -> {
                    return prepareNewAttempt(service, request, attempt, waitInSeconds, "API is not responding.", isStream);
                }
                default -> throw new RuntimeException("Something went wrong with the OpenAI API. Received the Status Code: " + e.statusCode + " with Message: " + e.getMessage(), e.getCause());
            }
        }
    }

    private <R> R prepareNewAttempt(final OpenAiService service, final ChatCompletionRequest request, final int attempt, final int waitInSeconds, final String warningMessage, final boolean isStream) {
        log.warn("{} A new attempt will be made soon.", warningMessage);
        try {
            Thread.sleep(1000L * waitInSeconds);
        } catch (InterruptedException e) {
            throw new RuntimeException("Error when sleeping before new attempt.", e);
        }
        return performRequest(service, request, attempt+1,waitInSeconds*2, isStream);
    }
}
