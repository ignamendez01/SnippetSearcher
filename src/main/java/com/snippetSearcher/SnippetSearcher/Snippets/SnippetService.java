package com.snippetSearcher.SnippetSearcher.Snippets;

import com.snippetSearcher.SnippetSearcher.Tests.Test;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SnippetService {
    private Logger logger = LoggerFactory.getLogger(SnippetService.class);

    @Autowired
    private SnippetRepository snippetRepository;

    public List<Snippet> getAllSnippets() {
        List<Snippet> snippets = snippetRepository.findAll();
        if (snippets.isEmpty()){
            logger.error("List is empty");
            throw new EntityNotFoundException();
        }else{
            logger.info("Full list of snippets");
            return snippets;
        }
    }

    public Snippet getSnippetById(Long id) {
        return snippetRepository.findById(id)
                .map(snippet -> {
                    logger.info("Snippet found");
                    return snippet;
                })
                .orElseThrow(() -> {
                    logger.error("Snippet doesn't exist");
                    return new EntityNotFoundException("Snippet with ID " + id + " not found");
                });
    }

    public void addSnippet(Snippet snippet) {
        if (snippet.getName() == null || snippet.getName().isEmpty()) {
            logger.error("Missing element to create a snippet");
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (snippet.getDescription() == null || snippet.getDescription().isEmpty()) {
            logger.error("Missing element to create a snippet");
            throw new IllegalArgumentException("Description cannot be null or empty");
        }
        if (snippet.getAuthor() == null || snippet.getAuthor().isEmpty()) {
            logger.error("Missing element to create a snippet");
            throw new IllegalArgumentException("Author cannot be null or empty");
        }
        if (snippet.getCode() == null || snippet.getCode().isEmpty()) {
            logger.error("Missing element to create a snippet");
            throw new IllegalArgumentException("Code cannot be null or empty");
        }
        if (snippet.getLanguage() == null || snippet.getLanguage().isEmpty()) {
            logger.error("Missing element to create a snippet");
            throw new IllegalArgumentException("Language cannot be null or empty");
        }
        if (snippet.getVersion() == null || snippet.getVersion().isEmpty()) {
            logger.error("Missing element to create a snippet");
            throw new IllegalArgumentException("Version cannot be null or empty");
        }
        logger.info("Snippet created");
        snippetRepository.save(snippet);
    }

    public void updateSnippet(Long id, Snippet snippet) {
        snippetRepository.findById(id).ifPresentOrElse(
                s -> {
                    if (snippet.getName() != null) s.setName(snippet.getName());
                    if (snippet.getDescription() != null) s.setDescription(snippet.getDescription());
                    if (snippet.getAuthor() != null) s.setAuthor(snippet.getAuthor());
                    if (snippet.getCode() != null) s.setCode(snippet.getCode());
                    if (snippet.getLanguage() != null) s.setLanguage(snippet.getLanguage());
                    if (snippet.getVersion() != null) s.setVersion(snippet.getVersion());
                    logger.info("Snippet updated");
                    snippetRepository.save(s);
                },
                () -> {
                    logger.error("No snippet to update");
                    throw new EntityNotFoundException("Snippet with ID " + id + " not found");
                }
        );

    }

    public void deleteSnippet(Long id) {
        snippetRepository.findById(id).ifPresentOrElse(
                snippet -> {
                    logger.info("Snippet deleted");
                    snippetRepository.deleteById(id);
                },
                () -> {
                    logger.error("No snippet to delete");
                    throw new EntityNotFoundException("Snippet with ID " + id + " not found");
                }
        );
    }

    public void shareSnippet(Long id, String username) {
        snippetRepository.findById(id).ifPresentOrElse(
                snippet -> {
                    snippet.getSharedUsers().add(username);
                    logger.info("Snippet shared");
                    snippetRepository.save(snippet);
                },
                () -> {
                    logger.error("No snippet to share");
                    throw new EntityNotFoundException("Snippet with ID " + id + " not found");
                }
        );
    }

    public String downloadSnippet(Long id) {
        return snippetRepository.findById(id)
                .map(snippet -> {
                    logger.info("Snippet downloaded");
                    return snippet.getCode();
                })
                .orElseThrow(() -> {
                    logger.error("No snippet to download");
                    return new EntityNotFoundException("Snippet with ID " + id + " not found");
                });
    }
}
