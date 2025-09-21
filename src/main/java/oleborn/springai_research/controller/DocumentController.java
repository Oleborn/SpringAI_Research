package oleborn.springai_research.controller;

import lombok.RequiredArgsConstructor;
import oleborn.springai_research.service.DocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @GetMapping("/load")
    public ResponseEntity<String> loadDocuments() {
        try {
            documentService.loadDocuments();
            return ResponseEntity.ok("Documents loaded successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error loading documents: " + e.getMessage());
        }
    }
}