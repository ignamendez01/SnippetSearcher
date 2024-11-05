package com.snippetSearcher.SnippetSearcher.Snippets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
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

    @PostMapping("/addFromFile")
    public ResponseEntity<String> createSnippetFromFile(@RequestBody SnippetRequest snippetRequest) {
        try {
            String code = Files.readString(Paths.get(snippetRequest.getFilePath()));
            Snippet newSnippet = new Snippet();
            newSnippet.setName(snippetRequest.getName());
            newSnippet.setDescription(snippetRequest.getDescription());
            newSnippet.setAuthor(snippetRequest.getAuthor());
            newSnippet.setLanguage(snippetRequest.getLanguage());
            newSnippet.setVersion(snippetRequest.getVersion());
            newSnippet.setCode(code);

            snippetService.addSnippet(newSnippet);
            return ResponseEntity.ok("Snippet created successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error reading file: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public void updateSnippet(@PathVariable Long id, @RequestBody Snippet snippet) {
        snippetService.updateSnippet(id, snippet);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteSnippet(@PathVariable Long id){
        snippetService.deleteSnippet(id);
    }

    @PostMapping("/share/{id}/{username}")
    public void shareSnippet(@PathVariable Long id, @PathVariable String username) {
        snippetService.shareSnippet(id, username);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<String> downloadSnippetCode(@PathVariable Long id) {
        Optional<Snippet> snippet = snippetService.getSnippetById(id);
        if (snippet.isPresent()) {
            return ResponseEntity.ok(snippet.get().getCode());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Snippet no encontrado.");
        }
    }
}