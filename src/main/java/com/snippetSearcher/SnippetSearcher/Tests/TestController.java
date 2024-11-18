package com.snippetSearcher.SnippetSearcher.Tests;

import com.snippetSearcher.SnippetSearcher.Snippets.Snippet;
import com.snippetSearcher.SnippetSearcher.Snippets.SnippetService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tests")
public class TestController {

    @Autowired
    private TestService testService;
    @Autowired
    private SnippetService snippetService;

    @GetMapping("/getAll")
    public List<Test> getAllTests() {
        return testService.getAllTests();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getTestById(@PathVariable Long id) {
        try {
            Test test = testService.getTestById(id);
            return ResponseEntity.ok(test);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addTest(@RequestBody Test test) {
        if(snippetService.getSnippetById(test.getSnippetId()) == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Snippet not found");
        }else{
            try{
                testService.addTest(test);
                return ResponseEntity.ok("Test created successfully");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(e.getMessage());
            }
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateTest(@PathVariable Long id, @RequestBody Test test) {
        try{
            testService.updateTest(id, test);
            return ResponseEntity.ok("Test updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTest(@PathVariable Long id) {
        try{
            testService.deleteTest(id);
            return ResponseEntity.ok("Test deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/execute/{id}")
    public ResponseEntity<String> executeTest(@PathVariable Long id) {
        try{
            Test test = testService.getTestById(id);
            Snippet s = snippetService.getSnippetById(test.getSnippetId());
            String message = testService.executeTest(test, s);
            return ResponseEntity.ok(String.join("\n", message));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}

