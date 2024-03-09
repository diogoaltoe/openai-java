package me.diogo.openaijava.operation;

import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionChunk;
import com.theokanning.openai.completion.chat.ChatFunction;
import io.reactivex.Flowable;
import me.diogo.openaijava.infra.OpenAiClient;
import me.diogo.openaijava.model.Shipping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CalculatorShipping {
    @Autowired
    private OpenAiClient<List<ChatCompletionChoice>, Flowable<ChatCompletionChunk>> openAiClient;

    private BigDecimal calculate(final Shipping shipping) {
        BigDecimal multiplier;

        switch (shipping.state()) {
            case ES, MG, RJ, SP -> {
                multiplier = new BigDecimal("1.5");
            }
            default -> {
                multiplier = new BigDecimal("4.5");
            }
        }

        return multiplier.multiply(new BigDecimal(shipping.productQuantity()));
    }

    public ChatFunction getCalculateShippingFunction() {
        return ChatFunction.builder()
                .name("calculateShipping")
                .executor(Shipping.class, this::calculate)
                .build();
    }
}
