package oleborn.springai_research.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.TimeZoneStorage;
import org.hibernate.annotations.TimeZoneStorageType;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ai_chat")
@Builder
public class AiChat {

    @Id
    //@GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OrderBy("createdAt ASC ")
    @OneToMany(mappedBy = "aiChat",fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AiChatMessage> aiChatMessages = new ArrayList<>();

    @CreationTimestamp
    @TimeZoneStorage(TimeZoneStorageType.NORMALIZE)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @TimeZoneStorage(TimeZoneStorageType.NORMALIZE)
    private ZonedDateTime updatedAt;
}