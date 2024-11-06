package com.snippetSearcher.SnippetSearcher.Snippets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public ResponseEntity<String> addSnippet(@RequestBody Snippet snippet) {
        snippetService.addSnippet(snippet);
        return ResponseEntity.ok("Snippet created successfully");
    }

    @PostMapping("/addFromFile")
    public ResponseEntity<String> createSnippetFromFile(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("author") String author,
            @RequestParam("language") String language,
            @RequestParam("version") String version,
            @RequestParam("file") MultipartFile file) {
        try {
            String code = new String(file.getBytes());

            Snippet newSnippet = new Snippet();
            newSnippet.setName(name);
            newSnippet.setDescription(description);
            newSnippet.setAuthor(author);
            newSnippet.setLanguage(language);
            newSnippet.setVersion(version);
            newSnippet.setCode(code);

            snippetService.addSnippet(newSnippet);
            return ResponseEntity.ok("Snippet created successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error reading file: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateSnippet(@PathVariable Long id, @RequestBody Snippet snippet) {
        snippetService.updateSnippet(id, snippet);
        return ResponseEntity.ok("Snippet updated successfully");
    }

    @PutMapping("/updateWithFile/{id}")
    public ResponseEntity<String> modifySnippetFromFile(
            @PathVariable Long id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "language", required = false) String language,
            @RequestParam(value = "version", required = false) String version,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        try {
            String code;

            if (file != null && !file.isEmpty()) {
                code = new String(file.getBytes());
            }else{
                code = null;
            }

            Snippet newSnippet = new Snippet();
            newSnippet.setName(name);
            newSnippet.setDescription(description);
            newSnippet.setAuthor(author);
            newSnippet.setLanguage(language);
            newSnippet.setVersion(version);
            newSnippet.setCode(code);

            snippetService.updateSnippet(id, newSnippet);
            return ResponseEntity.ok("Snippet updated successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error reading file: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteSnippet(@PathVariable Long id){
        snippetService.deleteSnippet(id);
        return ResponseEntity.ok("Snippet deleted");
    }

    @PostMapping("/share/{id}/{username}")
    public ResponseEntity<String> shareSnippet(@PathVariable Long id, @PathVariable String username) {
        snippetService.shareSnippet(id, username);
        return ResponseEntity.ok("Snippet shared with "+username);
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