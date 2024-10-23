package com.snippetSearcher.SnippetSearcher.Snippets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/snippets")
public class SnippetController {

    @Autowired
    private SnippetService snippetService;

    @GetMapping("/getAll")
    public List<Snippet> getAllSnippets() {
        return snippetService.getAllSnippets();
    }

    @GetMapping("/get/{id}")
    public Optional<Snippet> getSnippetById(@PathVariable Long id) {
        return snippetService.getSnippetById(id);
    }

    @PostMapping("/add")
    public void addSnippet(@RequestBody Snippet snippet) {
        snippetService.addSnippet(snippet);
    }

    @PutMapping("/update/{id}")
    public void updateSnippet(@PathVariable Long id, @RequestBody Snippet snippet) {
        snippetService.updateSnippet(id, snippet);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteSnippet(@PathVariable Long id){
        snippetService.deleteSnippet(id);
    }
}