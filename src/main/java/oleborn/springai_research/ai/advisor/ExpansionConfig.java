package oleborn.springai_research.ai.advisor;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExpansionConfig {

    @Bean
    ExpansionQueryAdvisor expansionQueryAdvisor(
            ChatModel chatModel,                          // бин ChatModel (интерфейс к LLM, напр. Mistral)
            @Value("${expansion.advisor.temperature:0.1}") double temperature,
            // читаем expansion.advisor.temperature из application.yml,
            // по умолчанию 0.0 → детерминированные ответы
            @Value("${expansion.advisor.top-p:0.4}") double topP
            // читаем expansion.advisor.top-p из application.yml,
            // по умолчанию 1.0 → модель использует весь распределённый контекст
    ) {
        return ExpansionQueryAdvisor.builder(
                        chatModel,
                        temperature,
                        topP
                )
                .order(1) // задаём порядок выполнения в цепочке Advisor’ов (выполняется вторым, если Rag стоит на 0)
                .build();
    }
}