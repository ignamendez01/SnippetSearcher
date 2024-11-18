package com.snippetSearcher.SnippetSearcher.Snippets;

import com.snippetSearcher.SnippetSearcher.Tests.Test;
import com.snippetSearcher.SnippetSearcher.Tests.TestController;
import com.snippetSearcher.SnippetSearcher.Tests.TestService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/snippets")
public class SnippetController {

    @Autowired
    private SnippetService snippetService;
    @Autowired
    private TestService testService;
    @Autowired
    private TestController testController;

    @GetMapping("/getAll")
    public List<Snippet> getAllSnippets() {
        return snippetService.getAllSnippets();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getSnippetById(@PathVariable Long id) {
        try {
            Snippet snippet = snippetService.getSnippetById(id);
            List<Test> tests = testService.getTestsBySnippetId(id);
            Map<String, Object> response = new HashMap<>();
            response.put("snippet", snippet);
            if (!tests.isEmpty()){
                response.put("tests", tests);
            }
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addSnippet(@RequestBody Snippet snippet) {
        try{
            snippetService.addSnippet(snippet);
            return ResponseEntity.ok("Snippet created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/addFromFile")
    public ResponseEntity<String> createSnippetFromFile(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("author") String author,
            @RequestParam("language") String language,
            @RequestParam("version") String version,
            @RequestParam("file") MultipartFile file) {

        String code;

        try {
            code = new String(file.getBytes());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error reading file: " + file.getOriginalFilename());
        }

        try {

            Snippet newSnippet = new Snippet();
            newSnippet.setName(name);
            newSnippet.setDescription(description);
            newSnippet.setAuthor(author);
            newSnippet.setLanguage(language);
            newSnippet.setVersion(version);
            newSnippet.setCode(code);

            snippetService.addSnippet(newSnippet);
            return ResponseEntity.ok("Snippet created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateSnippet(@PathVariable Long id, @RequestBody Snippet snippet) {
        try{
            snippetService.updateSnippet(id, snippet);
            List<Test> tests = testService.getTestsBySnippetId(id);
            List<String> outputMessages = new ArrayList<>();
            outputMessages.add("Snippet updated successfully");
            for (Test test : tests) {
                if (testController.executeTest(test.getId()).getStatusCode() == HttpStatus.OK) {
                    outputMessages.add("Test " + test.getId() + " passed");
                }else{
                    outputMessages.add("Test " + test.getId() + " failed: "+ testController.executeTest(test.getId()).getBody());
                }
            }
            return ResponseEntity.ok(String.join("\n", outputMessages));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
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
            List<Test> tests = testService.getTestsBySnippetId(id);
            List<String> outputMessages = new ArrayList<>();
            outputMessages.add("Snippet updated successfully");
            for (Test test : tests) {
                if (testController.executeTest(test.getId()).getStatusCode() == HttpStatus.OK) {
                    outputMessages.add("Test " + test.getId() + " passed");
                }else{
                    outputMessages.add("Test " + test.getId() + " failed: "+ testController.executeTest(test.getId()).getStatusCode());
                }
            }
            return ResponseEntity.ok(String.join("\n", outputMessages));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteSnippet(@PathVariable Long id){
        try{
            snippetService.deleteSnippet(id);
            return ResponseEntity.ok("Snippet deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/share/{id}/{username}")
    public ResponseEntity<String> shareSnippet(@PathVariable Long id, @PathVariable String username) {
        try{
            snippetService.shareSnippet(id, username);
            return ResponseEntity.ok("Snippet shared with "+username);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<String> downloadSnippetCode(@PathVariable Long id) {
        try{
            String code = snippetService.downloadSnippet(id);
            return ResponseEntity.ok(code);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }
}