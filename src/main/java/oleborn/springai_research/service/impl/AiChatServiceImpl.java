package oleborn.springai_research.service.impl;

import lombok.RequiredArgsConstructor;
import oleborn.springai_research.model.entity.AiChat;
import oleborn.springai_research.repository.AiChatRepository;
import oleborn.springai_research.service.AiChatService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AiChatServiceImpl implements AiChatService {

    private final AiChatRepository chatRepository;

    @Override
    public AiChat createChat(UUID id) {
        return chatRepository.save(AiChat.builder()
                .id(id == null ? UUID.randomUUID() : id)
                .build());
    }

    @Override
    public AiChat getChat(UUID id) {

        Optional<AiChat> byId = chatRepository.findById(id);

        return byId.orElseGet(() -> createChat(id));
    }

    @Override
    public void saveChat(AiChat chat) {
        chatRepository.save(chat);
    }
}
