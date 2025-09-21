package oleborn.springai_research.controller;

import lombok.RequiredArgsConstructor;
import oleborn.springai_research.model.dto.RequestDto;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AiController {

    private final ChatClient chatClient;

    @PostMapping("/ask")
    public String ask(@RequestBody RequestDto requestDto) {
        return chatClient.prompt()
                .advisors(
                        advisorSpec -> advisorSpec.param(
                                ChatMemory.CONVERSATION_ID,
                                requestDto.chatId()
                        )
                )
                .user(
                        requestDto.question()
                )
                .call()
                .content();
    }
}