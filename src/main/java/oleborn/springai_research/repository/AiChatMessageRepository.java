package oleborn.springai_research.repository;

import oleborn.springai_research.model.entity.AiChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AiChatMessageRepository extends JpaRepository<AiChatMessage, UUID> {
}
