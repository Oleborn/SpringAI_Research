package oleborn.springai_research.repository;

import oleborn.springai_research.model.entity.AiChat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AiChatRepository extends JpaRepository<AiChat, UUID> {
}
