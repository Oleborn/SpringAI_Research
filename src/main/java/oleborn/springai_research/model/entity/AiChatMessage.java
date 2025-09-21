package oleborn.springai_research.model.entity;

import jakarta.persistence.*;
import lombok.*;
import oleborn.springai_research.dictionary.AIRole;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.TimeZoneStorage;
import org.hibernate.annotations.TimeZoneStorageType;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ai_chat_messages")
@Builder
public class AiChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", nullable = false)
    private AiChat aiChat;

    @Column(name = "text", columnDefinition = "TEXT")
    private String text;

    @Enumerated(EnumType.STRING)
    @Column(name = "ai_role", nullable = false)
    private AIRole aiRole;

    @CreationTimestamp
    @TimeZoneStorage(TimeZoneStorageType.NORMALIZE)
    private ZonedDateTime createdAt;

}