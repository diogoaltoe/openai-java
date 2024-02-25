package me.diogo.openaijava.operation;

import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionChunk;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import io.reactivex.Flowable;
import me.diogo.openaijava.presentation.dto.QuestionRequest;
import me.diogo.openaijava.infra.OpenAiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChatUseCase {
    @Autowired
    private OpenAiClient<List<ChatCompletionChoice>, Flowable<ChatCompletionChunk>> openAiClient;

    public Flowable<ChatCompletionChunk> answerQuestion(QuestionRequest request) {
        final List<String> systemRules = List.of(
                "You are an ecommerce customer service chatbot and must only answer questions related to ecommerce."
        );
        final var system = new ChatMessage(ChatMessageRole.SYSTEM.value(), String.join("\n", systemRules));
        final var user = new ChatMessage(ChatMessageRole.USER.value(), request.question());

        return openAiClient.chatRequestStream(List.of(system, user));
    }
}
