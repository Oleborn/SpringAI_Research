package oleborn.springai_research.ai.advisor;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import oleborn.springai_research.dictionary.AiTemplateMessage;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.AdvisorChain;
import org.springframework.ai.chat.client.advisor.api.BaseAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.mistralai.MistralAiChatOptions;

import java.util.Map;

/**
 * Advisor для расширения (переписывания) пользовательских запросов.
 * Можно использовать для нормализации формулировок, подбора синонимов и т.п.
 */
@Slf4j
@Builder
public class ExpansionQueryAdvisor implements BaseAdvisor {
    // Класс Advisor (перехватчик), который расширяет/переписывает пользовательский запрос перед отправкой в LLM.

    public static final String ENRICHED_QUESTION = "ENRICHED_QUESTION";
    // Константа-ключ для сохранения обогащённого вопроса в контекст.
    public static final String ORIGINAL_QUESTION = "ORIGINAL_QUESTION";
    // Константа-ключ для сохранения оригинального вопроса в контекст.

    private final ChatClient chatClient;
    // Клиент для общения с моделью (через Spring AI).

    @Getter
    private final int order;
    // Порядок выполнения Advisor в цепочке.

    private final double temperature;
    // Параметр "temperature" модели (контролирует креативность).
    private final double topP;
    // Параметр "topP" (контролирует разнообразие ответов).

    private static final PromptTemplate template = PromptTemplate.builder()
            .template(AiTemplateMessage.PROMPT_TEMPLATE_2.getMessage())
            // Загружаем шаблон промпта из enum AiTemplateMessage.
            .build();
    // Этот шаблон будет использоваться для генерации обогащённого вопроса.

    /**
     * Фабричный builder для удобного создания с настройкой ChatClient.
     * Передаём модель и параметры temperature/topP.
     */
    public static ExpansionQueryAdvisorBuilder builder(
            ChatModel chatModel,  // модель (например, Mistral)
            double temperature,   // креативность
            double topP           // разнообразие
    ) {
        return new ExpansionQueryAdvisorBuilder()
                .chatClient(
                        ChatClient.builder(chatModel) // создаём ChatClient на основе модели
                                .defaultOptions(
                                        MistralAiChatOptions.builder()
                                                .temperature(temperature) // задаём temperature
                                                .topP(topP)               // задаём topP
                                                .build()
                                )
                                .build()
                )
                .temperature(temperature) // сохраняем параметры в билдере
                .topP(topP);
    }

    @Override
    public ChatClientRequest before(ChatClientRequest chatClientRequest, AdvisorChain advisorChain) {
        // Метод выполняется ДО отправки запроса в LLM.

        String originalQuestion = chatClientRequest.prompt().getUserMessage().getText();
        // Достаём текст оригинального вопроса пользователя.
        log.info("Expansion: original='{}'", originalQuestion);

        String enrichedQuestion = expand(originalQuestion);
        // Обогащаем вопрос через LLM (expand вызывает ChatClient).

        if (enrichedQuestion == null || enrichedQuestion.isBlank()) {
            // Если не удалось обогатить — используем оригинал.
            log.warn("Expansion: не удалось обогатить вопрос '{}', оставляем оригинал", originalQuestion);
            enrichedQuestion = originalQuestion;
        } else {
            log.info("Expansion: enriched='{}'", enrichedQuestion);
        }

        return chatClientRequest.mutate()
                // Создаём новый ChatClientRequest на основе старого
                .context(ORIGINAL_QUESTION, originalQuestion) // сохраняем оригинал в контекст
                .context(ENRICHED_QUESTION, enrichedQuestion) // сохраняем обогащённый вопрос
                .build(); // возвращаем изменённый запрос
    }

    @Override
    public ChatClientResponse after(ChatClientResponse chatClientResponse, AdvisorChain advisorChain) {
        return chatClientResponse;
    }

    /**
     * Генерация обогащённого вопроса через LLM.
     */
    private String expand(String originalQuestion) {
        try {
            String rendered = template.render(Map.of("question", originalQuestion));
            // Подставляем оригинальный вопрос в шаблон → получаем промпт.

            return chatClient
                    .prompt()        // начинаем построение запроса
                    .user(rendered)  // добавляем user-сообщение (обработанный промпт)
                    .call()          // выполняем вызов к модели
                    .content();      // получаем результат в виде строки
        } catch (Exception e) {
            // Если модель не ответила или случилась ошибка — возвращаем null.
            log.error("Expansion: ошибка при расширении вопроса '{}'", originalQuestion, e);
            return null;
        }
    }
}
