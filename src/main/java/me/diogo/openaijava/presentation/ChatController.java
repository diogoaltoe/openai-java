package me.diogo.openaijava.presentation;

import me.diogo.openaijava.operation.ChatUseCase;
import me.diogo.openaijava.presentation.dto.QuestionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
    public ResponseBodyEmitter answerQuestion(@RequestBody QuestionRequest request) {
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
}
