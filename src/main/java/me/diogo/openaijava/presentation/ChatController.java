package me.diogo.openaijava.presentation;

import io.swagger.v3.oas.annotations.Operation;
import me.diogo.openaijava.operation.ChatUseCase;
import me.diogo.openaijava.presentation.dto.ChatRequest;
import me.diogo.openaijava.presentation.dto.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.util.Objects;

@Controller
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    private ChatUseCase chatUseCase;

    @PostMapping
    @ResponseBody
    @Operation(summary = "Ask the chat a question.", description = "This endpoint is simulating an ecommerce customer service chat-bot and will only answer questions related to ecommerce. It's designed to asynchronously send data.")
    public ResponseBodyEmitter answerQuestion(@RequestBody ChatRequest request) {
        final var chatCompletionChunkFlowable = chatUseCase.answerQuestion(request);
        final var emitter = new ResponseBodyEmitter();

        chatCompletionChunkFlowable.subscribe(c -> {
            final String content = c.getChoices().getFirst().getMessage().getContent();
            if (Objects.nonNull(content)) {
                emitter.send(content);
            }
        }, emitter::completeWithError, emitter::complete);

        return emitter;
    }

    @GetMapping("product/{product}/category")
    @ResponseBody
    @Operation(summary = "Find out the product category.", description = "This endpoint is simulating an product categorizer and will only answer the name of the product category.")
    public ResponseEntity<ProductResponse> getCategory(@PathVariable("product") String product) {
        final String category = chatUseCase.findCategory(product);

        return ResponseEntity.ok(new ProductResponse(product, category));
    }
}
