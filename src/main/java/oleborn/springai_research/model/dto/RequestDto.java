package oleborn.springai_research.model.dto;

import java.util.UUID;

public record RequestDto(
        UUID chatId,
        String question
) {
}
