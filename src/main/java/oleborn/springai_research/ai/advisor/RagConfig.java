package oleborn.springai_research.ai.advisor;

import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RagConfig {

    @Bean
    RagCustomAdvisor ragCustomAdvisor(
            VectorStore vectorStore,                  // бин VectorStore (подключение к pgvector и т.п.)
            @Value("${rag.advisor.top-k:3}") int topK,
            @Value("${rag.advisor.similarity-threshold:0.65}") double similarityThreshold,
            // читаем rag.advisor.similarity-threshold из конфигурации,
            // по умолчанию 0.8 → порог схожести
            @Value("${rag.advisor.max-context-chars:4000}") int maxContextChars
            // читаем rag.advisor.max-context-chars,
            // по умолчанию 4000 символов → ограничение размера контекста
    ) {
        return RagCustomAdvisor.builder()
                .vectorStore(vectorStore)              // передаём бин VectorStore внутрь Advisor
                .order(3)                              // порядок выполнения в цепочке Advisor’ов
                .topK(topK)                            // количество документов, которые вернёт поиск
                .similarityThreshold(similarityThreshold) // минимальный уровень схожести документа с запросом
                .build();
    }
}