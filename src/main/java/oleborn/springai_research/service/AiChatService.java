package oleborn.springai_research.service;

import oleborn.springai_research.model.entity.AiChat;

import java.util.UUID;

public interface AiChatService {

    AiChat createChat(UUID id);

    AiChat getChat(UUID id);

    void saveChat(AiChat chat);
}
