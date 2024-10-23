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
            s.setName(snippet.getName());
            s.setDescription(snippet.getDescription());
            s.setCode(snippet.getCode());
            s.setLanguage(snippet.getLanguage());
            s.setVersion(snippet.getVersion());
            snippetRepository.save(s);
        });
    }

    public void deleteSnippet(Long id) {
        snippetRepository.deleteById(id);
    }
}
