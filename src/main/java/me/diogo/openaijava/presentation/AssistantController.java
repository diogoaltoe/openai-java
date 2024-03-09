package me.diogo.openaijava.presentation;

import io.swagger.v3.oas.annotations.Operation;
import me.diogo.openaijava.operation.AssistantUseCase;
import me.diogo.openaijava.presentation.dto.AssistantRequest;
import me.diogo.openaijava.presentation.dto.AssistantResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Objects;

@Controller
@RequestMapping("/assistant")
public class AssistantController {
    @Autowired
    private AssistantUseCase assistantUseCase;

    @PostMapping
    @ResponseBody
    @Operation(summary = "Ask the assistant a question.", description = "This endpoint is simulating an assistant from EcoMart eCommerce. The assistant will use the Retrieval and Function features to answer your questions. If you enter the `threadId`, the assistant will consider the question history when providing an answer.")
    public ResponseEntity<AssistantResponse> answerQuestion(@RequestBody AssistantRequest request) {
        final var assistantResponse = assistantUseCase.answerQuestion(request);
        if (Objects.isNull(assistantResponse))
            return ResponseEntity.badRequest().body(null);

        return ResponseEntity.ok(assistantResponse);
    }
}
