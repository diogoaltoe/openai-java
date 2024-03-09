package me.diogo.openaijava.operation;

import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionChunk;
import io.reactivex.Flowable;
import me.diogo.openaijava.infra.OpenAiClient;
import me.diogo.openaijava.presentation.dto.AssistantResponse;
import me.diogo.openaijava.presentation.dto.AssistantRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssistantUseCase {
    @Autowired
    private OpenAiClient<List<ChatCompletionChoice>, Flowable<ChatCompletionChunk>> openAiClient;
    @Autowired
    private CalculatorShipping calculatorShipping;

    public AssistantResponse answerQuestion(AssistantRequest request) {
        final var message = openAiClient.assistantRequest(request.prompt(), request.threadId(), calculatorShipping.getCalculateShippingFunction());
        if (message.isEmpty()) return null;

        return new AssistantResponse(OpenAiClient.cleanContent(message), message.get().getThreadId());
    }

}
