package com.snippetSearcher.SnippetSearcher.Snippets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SnippetService {
    @Autowired
    private SnippetRepository snippetRepository;

    public List<Snippet> getAllSnippets() {
        return snippetRepository.findAll();
    }

    public Optional<Snippet> getSnippetById(Long id) {
        return snippetRepository.findById(id);
    }

    public void addSnippet(Snippet snippet) {
        snippetRepository.save(snippet);
    }

    public void updateSnippet(Long id, Snippet snippet) {
        snippetRepository.findById(id).ifPresent(s -> {
            if (snippet.getName() != null) {
                s.setName(snippet.getName());
            }
            if (snippet.getDescription() != null) {
                s.setDescription(snippet.getDescription());
            }
            if (snippet.getAuthor() != null) {
                s.setAuthor(snippet.getAuthor());
            }
            if (snippet.getCode() != null) {
                s.setCode(snippet.getCode());
            }
            if (snippet.getLanguage() != null) {
                s.setLanguage(snippet.getLanguage());
            }
            if (snippet.getVersion() != null) {
                s.setVersion(snippet.getVersion());
            }
            snippetRepository.save(s);
        });
    }

    public void deleteSnippet(Long id) {
        snippetRepository.deleteById(id);
    }

    public void shareSnippet(Long id, String username) {
        snippetRepository.findById(id).ifPresent(snippet -> {
            snippet.getSharedUsers().add(username);
            snippetRepository.save(snippet);
        });
    }
}
