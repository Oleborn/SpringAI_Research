# SpringAI_Research

Этот репозиторий представляет собой исследовательский проект, посвященный изучению и демонстрации возможностей Spring AI. Проект фокусируется на интеграции с моделями искусственного интеллекта, реализации механизмов памяти для диалоговых систем и использовании Retrieval Augmented Generation (RAG) для улучшения ответов моделей на основе внешней информации. В качестве базы данных используется PostgreSQL с расширением PgVector для эффективного хранения и поиска векторных эмбеддингов.

## Особенности

*   **Интеграция со Spring AI**: Использование фреймворка Spring AI для взаимодействия с различными моделями ИИ.
*   **Модель Mistral AI**: Пример интеграции с моделью Mistral AI для генерации текста.
*   **Механизмы памяти**: Реализация и демонстрация механизмов памяти для поддержания контекста в диалоговых системах.
*   **Retrieval Augmented Generation (RAG)**: Использование RAG для обогащения ответов моделей информацией из векторной базы данных.
*   **PostgreSQL с PgVector**: Хранение векторных эмбеддингов и метаданных в PostgreSQL с использованием расширения PgVector для семантического поиска.
*   **Spring Data JPA**: Управление персистентностью данных.
*   **RESTfull API**: Предоставление API для взаимодействия с функциями ИИ.

## Технологии

*   **Java 21**
*   **Spring Boot 3.5.5**
*   **Spring AI 1.0.1**
*   **Mistral AI**
*   **PostgreSQL**
*   **PgVector**
*   **Maven**
*   **Docker & Docker Compose**

## Начало работы

### Предварительные требования

Для запуска этого проекта вам потребуется:

*   Java Development Kit (JDK) 21
*   Apache Maven
*   Docker и Docker Compose (для запуска PostgreSQL)
*   Доступ к API Mistral AI (или другой LLM, настроенной в `application.yaml`)

### Установка и запуск

1.  **Клонируйте репозиторий:**

    ```bash
    git clone https://github.com/Oleborn/SpringAI_Research.git
    cd SpringAI_Research
    ```

2.  **Настройте переменные окружения:**

    Создайте файл `src/main/resources/application.properties` (если его нет) и добавьте необходимые конфигурации для Spring AI и базы данных. 

    Пример:

    ```properties
    spring:
      application:
        name: SpringAI_Research

    ai:
      mistralai:
        chat:
          options:
            model: "mistral-small-latest"
        api-key: "YOUR_MISTRAL_AI_API_KEY"

      vectorstore:
        pgvector:
          index-type: HNSW
          dimensions: 1024

    datasource:
      url: "jdbc:postgresql://localhost:5435/aidb"
      username: "postgres"
      password: "postgres"

    jpa:
      hibernate:
        ddl-auto: update
      properties:
        hibernate:
          format_sql: true
          dialect: org.hibernate.dialect.PostgreSQLDialect

    logging:
      level:
        org.springframework.ai.chat.client.advisor: DEBUG

    app:
      maxMessages: 10 #Количество сообщений читаемых в истории AIMessages из всей истории для контекста AI
      chunkSize: 500 #размер чанков на который разрезан документ
      document-path: "classpath:/ragdocument/**/*.txt"
    rag:
      advisor:
        top-k: 40
        similarity-threshold: 0.8
        max-context-chars: 10000
    expansion:
      advisor:
        temperature: 0.1
        top-p: 0.2
    ```

    **Важно**: Замените `YOUR_MISTRAL_AI_API_KEY` на ваш реальный ключ API Mistral AI.

3.  **Запустите PostgreSQL с PgVector:**

    Проект включает `docker-compose.yml` для удобного запуска PostgreSQL с предустановленным расширением PgVector.

    ```bash
    docker-compose up -d
    ```

    Убедитесь, что контейнер PostgreSQL запущен и доступен.
    
    **Настройки БД для работы:**

    ```
    -- Расширение для работы с векторами
    CREATE EXTENSION IF NOT EXISTS vector;

    -- Таблица для векторного хранилища
    CREATE TABLE IF NOT EXISTS vector_store (
      id        VARCHAR(255) PRIMARY KEY,
      content   TEXT,
      metadata  JSON,
      embedding VECTOR(1024)
    );

    -- Индекс HNSW для быстрого векторного поиска
    CREATE INDEX IF NOT EXISTS vector_store_hnsw_index ON vector_store USING hnsw (embedding vector_cosine_ops);
    ```

4.  **Соберите и запустите приложение:**

    ```bash
    mvn clean install
    mvn spring-boot:run
    ```

    Приложение будет доступно по адресу `http://localhost:8080`.

## Структура проекта

```
SpringAI_Research/
├── src/
│   ├── main/
│   │   ├── java/oleborn/springai_research/
│   │   │   ├── ai/                  # Компоненты, связанные с интеграцией AI
│   │   │   ├── controller/          # REST контроллеры
│   │   │   ├── dictionary/          # Словари или статические данные
│   │   │   ├── model/               # Модели данных (JPA сущности, DTO)
│   │   │   ├── repository/          # Репозитории Spring Data JPA
│   │   │   └── service/             # Сервисы бизнес-логики
│   │   └── resources/               # Ресурсы приложения (application.properties)
│   └── test/                        # Тесты
├── docker-compose.yml               # Конфигурация Docker Compose для PostgreSQL
├── pom.xml                          # Файл конфигурации Maven
└── README.md                        # Этот файл
```

## Использование

После запуска приложения вы можете взаимодействовать с ним через REST API. 

Основные эндпоинты:
*   `/api/chat`: Для взаимодействия с LLM с поддержкой памяти.
*   `/api/documents`: Для загрузки документов в векторное хранилище и использования их для RAG.


## Контакты

Если у вас есть вопросы, свяжитесь с автором: @Oleborn.


