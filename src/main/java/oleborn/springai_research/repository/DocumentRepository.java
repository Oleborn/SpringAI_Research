package oleborn.springai_research.repository;

import oleborn.springai_research.model.entity.LoadedDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DocumentRepository extends JpaRepository<LoadedDocument, UUID> {

    boolean existsByFilenameAndContentHash(String filename, String contentHash);

}