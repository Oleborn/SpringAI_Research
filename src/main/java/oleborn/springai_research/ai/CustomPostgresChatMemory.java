package oleborn.springai_research.ai;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import oleborn.springai_research.dictionary.AIRole;
import oleborn.springai_research.model.entity.AiChat;
import oleborn.springai_research.model.entity.AiChatMessage;
import oleborn.springai_research.service.AiChatService;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Builder
public class CustomPostgresChatMemory implements ChatMemory {

    private final AiChatService chatService;
    private final int maxMessages;

    @Override
    public void add(String conversationId, List<Message> messages) {

        AiChat chat = chatService.getChat(UUID.fromString(conversationId));

        for (Message message : messages) {

            boolean alreadyExists = chat.getAiChatMessages().stream()
                    .anyMatch(existing ->
                            existing.getText().equals(message.getText()) &&
                            existing.getAiRole() == getAirole(message));

            if (!alreadyExists) {
                AiChatMessage aiChatMessage = AiChatMessage.builder()
                        .aiChat(chat)
                        .text(message.getText())
                        .aiRole(getAirole(message))
                        .build();

                chat.getAiChatMessages().add(aiChatMessage);
            }

        }

        chatService.saveChat(chat);

    }

    @Override
    public List<Message> get(String conversationId) {

        AiChat chat = chatService.getChat(UUID.fromString(conversationId));

        return chat.getAiChatMessages().stream()
                .skip(Math.max(0, chat.getAiChatMessages().size() - maxMessages))
                .map(this::getMessage)
                .limit(maxMessages)
                .toList();
    }

    @Override
    public void clear(String conversationId) {

    }

    private Message getMessage(AiChatMessage aiChatMessage) {
        switch (aiChatMessage.getAiRole()) {
            case USER -> {
                return new UserMessage(aiChatMessage.getText());
            }
            case ASSISTANT -> {
                return new AssistantMessage(aiChatMessage.getText());
            }
            case SYSTEM -> {
                return new SystemMessage(aiChatMessage.getText());
            }
            default -> {
                return null;
            }
        }
    }

    private AIRole getAirole(Message message){
        switch (message.getMessageType()){
            case USER -> {
                return AIRole.USER;
            }
            case ASSISTANT -> {
                return AIRole.ASSISTANT;
            }
            case SYSTEM -> {
                return AIRole.SYSTEM;
            }
            default -> {
                return null;
            }
        }
    }
}
