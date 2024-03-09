package me.diogo.openaijava.infra;

import com.theokanning.openai.OpenAiHttpException;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionChunk;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.messages.Message;
import com.theokanning.openai.messages.MessageRequest;
import com.theokanning.openai.runs.Run;
import com.theokanning.openai.runs.RunCreateRequest;
import com.theokanning.openai.service.OpenAiService;
import com.theokanning.openai.threads.ThreadRequest;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;
import me.diogo.openaijava.model.OpenAiModel;
import me.diogo.openaijava.operation.TokenCounter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class OpenAiClient<T extends List<ChatCompletionChoice>, S extends Flowable<ChatCompletionChunk>> {
    private final OpenAiService service;
    private final String assistantId;

    public OpenAiClient(@Value("${openai.api.key}") String key, @Value("${openai.assistant.id}") String assistantId) {
        this.service = new OpenAiService(
                Objects.requireNonNull(key, "Missing OpenAi API Key."),
                Duration.ofSeconds(30));
        this.assistantId = assistantId;
    }

    /*
     * CHAT
     */

    public T chatRequest(final List<ChatMessage> chatMessages) {
        return prepareRequest(chatMessages, false);
    }

    public S chatRequestStream(final List<ChatMessage> chatMessages) {
        return prepareRequest(chatMessages, true);
    }

    private <R> R prepareRequest(final List<ChatMessage> chatMessages, final boolean isStream) {
        final var model = selectModel(chatMessages);

        final var request = ChatCompletionRequest
                .builder()
                .model(model.getName())
                .messages(chatMessages)
                .stream(isStream)
                .build();

        return performRequest(request, 0, 5, isStream);
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

    private <R> R performRequest(final ChatCompletionRequest request, final int attempt, final int waitInSeconds, final boolean isStream) {
        if(attempt > 4)
            throw new RuntimeException("Error with the OpenAI API. All attempts failed.");

        try {
            return isStream
                    ? (R) this.service.streamChatCompletion(request)
                    : (R) this.service.createChatCompletion(request).getChoices();
        }
        catch (OpenAiHttpException e) {
            switch (e.statusCode) {
                case 401 -> throw new IllegalArgumentException("Error with OpenAI API Key.", e);
                case 429 -> {
                    return prepareNewAttempt(request, attempt, waitInSeconds, "Rate limit reached.", isStream);
                }
                case 500, 503 -> {
                    return prepareNewAttempt(request, attempt, waitInSeconds, "API is not responding.", isStream);
                }
                default -> throw new RuntimeException("Something went wrong with the OpenAI API. Received the Status Code: " + e.statusCode + " with Message: " + e.getMessage(), e.getCause());
            }
        }
        catch (RuntimeException e) {
            if (e.getMessage().contains("timeout"))
                return prepareNewAttempt(request, attempt, waitInSeconds, "OpenAI API response is taking more than expected.", isStream);

            throw e;
        }
    }

    private <R> R prepareNewAttempt(final ChatCompletionRequest request, final int attempt, final int waitInSeconds, final String warningMessage, final boolean isStream) {
        log.warn("{} A new attempt will be made soon.", warningMessage);
        try {
            Thread.sleep(1000L * waitInSeconds);
        } catch (InterruptedException e) {
            throw new RuntimeException("Error when sleeping before new attempt.", e);
        }
        return performRequest(request, attempt+1,waitInSeconds*2, isStream);
    }


    /*
     * ASSISTANT
     */

    public Optional<Message> assistantRequest(final String userPrompt, String threadId) {
        final var messageRequest = MessageRequest
                .builder()
                .role(ChatMessageRole.USER.value())
                .content(userPrompt)
                .build();

        if (Objects.isNull(threadId)) {
            log.info("Creating a new thread for the assistant.");

            final var threadRequest = ThreadRequest
                    .builder()
                    .messages(Collections.singletonList(messageRequest))
                    .build();
            final var thread = service.createThread(threadRequest);
            threadId = thread.getId();
            log.info("New thread for the assistant: {}", threadId);
        }
        else {
            log.info("Using the existing thread for the assistant: {}", threadId);

            service.createMessage(threadId, messageRequest);
        }

        final var runCreateRequest = RunCreateRequest
                .builder()
                .assistantId(this.assistantId)
                .build();
        var run = service.createRun(threadId, runCreateRequest);

        waitForRunComplete(threadId, run);
        log.info("Run is completed.");

        final var messages = service.listMessages(threadId);
        log.info("Found in the thread the following messages: {}", messages);

        return messages
                .getData()
                .stream()
                .max(Comparator.comparingInt(Message::getCreatedAt));
    }

    private void waitForRunComplete(final String threadId, final Run run) {
        final var retrieveRun = service.retrieveRun(threadId, run.getId());
        log.info("The current run status is: {}", retrieveRun.getStatus());

        if (!retrieveRun.getStatus().equalsIgnoreCase("completed")) {
            log.info("A new status verification will be made soon.");
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException("Error when sleeping before new status check.", e);
            }
            waitForRunComplete(threadId, run);
        }
    }

    public static String cleanContent(final Optional<Message> message) {
        return message.map(value -> value
                .getContent()
                .getFirst()
                .getText()
                .getValue()
                .replaceAll("\\u3010.*?\\u3011", "")).orElse(null);
    }
}
