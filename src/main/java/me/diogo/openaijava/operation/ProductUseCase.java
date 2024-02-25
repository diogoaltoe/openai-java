package me.diogo.openaijava.operation;

import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionChunk;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import io.reactivex.Flowable;
import me.diogo.openaijava.infra.OpenAiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductUseCase {
    @Autowired
    private OpenAiClient<List<ChatCompletionChoice>, Flowable<ChatCompletionChunk>> openAiClient;

    public String findCategory(final String product) {
        final var systemRules = List.of(
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
        final var system = new ChatMessage(ChatMessageRole.SYSTEM.value(), String.join("\n", systemRules));
        final var user = new ChatMessage(ChatMessageRole.USER.value(), product);

        return openAiClient
                .chatRequest(List.of(system, user))
                .getFirst()
                .getMessage()
                .getContent();
    }
}
