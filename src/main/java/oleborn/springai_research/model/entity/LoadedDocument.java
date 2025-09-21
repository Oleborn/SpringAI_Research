package oleborn.springai_research.model.entity;

import jakarta.persistence.*;
import lombok.*;
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
@Table(name = "loaded_document")
@Builder
public class LoadedDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "filename")
    private String filename;

    @Column(name = "content_hash")
    private String contentHash;

    @Column(name = "chunk_count")
    private int chunkCount;

    @CreationTimestamp
    @TimeZoneStorage(TimeZoneStorageType.NORMALIZE)
    private ZonedDateTime loadedAt;
}