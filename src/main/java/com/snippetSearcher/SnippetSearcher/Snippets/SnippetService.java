package com.snippetSearcher.SnippetSearcher.Snippets;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SnippetService {
    @Autowired
    private SnippetRepository snippetRepository;

    public List<Snippet> getAllSnippets() {
        return snippetRepository.findAll();
    }

    public Snippet getSnippetById(Long id) {
        return snippetRepository.findById(id).orElse(null);
    }

    public void addSnippet(Snippet snippet) {
        if (snippet.getName() == null || snippet.getName().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (snippet.getDescription() == null || snippet.getDescription().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty");
        }
        if (snippet.getAuthor() == null || snippet.getAuthor().isEmpty()) {
            throw new IllegalArgumentException("Author cannot be null or empty");
        }
        if (snippet.getCode() == null || snippet.getCode().isEmpty()) {
            throw new IllegalArgumentException("Code cannot be null or empty");
        }
        if (snippet.getLanguage() == null || snippet.getLanguage().isEmpty()) {
            throw new IllegalArgumentException("Language cannot be null or empty");
        }
        if (snippet.getVersion() == null || snippet.getVersion().isEmpty()) {
            throw new IllegalArgumentException("Version cannot be null or empty");
        }
        snippetRepository.save(snippet);
    }

    public void updateSnippet(Long id, Snippet snippet) {
        List<String> testResults = new ArrayList<>();

        snippetRepository.findById(id).ifPresentOrElse(
                s -> {
                    if (snippet.getName() != null) s.setName(snippet.getName());
                    if (snippet.getDescription() != null) s.setDescription(snippet.getDescription());
                    if (snippet.getAuthor() != null) s.setAuthor(snippet.getAuthor());
                    if (snippet.getCode() != null) s.setCode(snippet.getCode());
                    if (snippet.getLanguage() != null) s.setLanguage(snippet.getLanguage());
                    if (snippet.getVersion() != null) s.setVersion(snippet.getVersion());

                    snippetRepository.save(s);
                },
                () -> {
                    throw new EntityNotFoundException("Snippet with ID " + id + " not found");
                }
        );

    }



    public void deleteSnippet(Long id) {
        snippetRepository.findById(id).ifPresentOrElse(
                snippet -> {
                    snippetRepository.deleteById(id);
                },
                () -> {
                    throw new EntityNotFoundException("Snippet with ID " + id + " not found");
                }
        );
    }

    public void shareSnippet(Long id, String username) {
        snippetRepository.findById(id).ifPresentOrElse(
                snippet -> {
                    snippet.getSharedUsers().add(username);
                    snippetRepository.save(snippet);
                },
                () -> {
                    throw new EntityNotFoundException("Snippet with ID " + id + " not found");
                }
        );
    }

}
