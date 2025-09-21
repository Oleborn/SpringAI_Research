package oleborn.springai_research.ai;

import lombok.RequiredArgsConstructor;
import oleborn.springai_research.ai.advisor.ExpansionQueryAdvisor;
import oleborn.springai_research.ai.advisor.RagCustomAdvisor;
import oleborn.springai_research.service.AiChatService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.mistralai.MistralAiChatOptions;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ChatClientConfig {

    private final AiChatService chatService;
    private final VectorStore vectorStore;
    private final RagCustomAdvisor ragCustomAdvisor;
    private final ExpansionQueryAdvisor expansionQueryAdvisor;

    @Value("${app.maxMessages}")
    private int maxMessage;

    @Bean
    public ChatClient chatClientCustom(ChatClient.Builder builder) {
        return builder
                .defaultAdvisors(
                        expansionQueryAdvisor,    // 1. Расширение запроса
                        ragCustomAdvisor,         // 2. RAG поиск
                        addPostgresAdvisor(2),
                        SimpleLoggerAdvisor.builder().order(4).build()
                )
                .defaultOptions(
                        MistralAiChatOptions.builder()
                                .temperature(0.3) //больше - гонит
                                .topP(0.7) //70% варианты совпадения достаточно для поиска
                                .maxTokens(400)
                                .build()
                )
                .build();
    }

    private Advisor addPostgresAdvisor(int order) {
        return MessageChatMemoryAdvisor.builder(getPostgresChatMemory())
                .order(order)
                .build();
    }

    private ChatMemory getPostgresChatMemory() {
        return CustomPostgresChatMemory.builder()
                .maxMessages(maxMessage)
                .chatService(chatService)
                .build();
    }

}
