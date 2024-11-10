package com.snippetSearcher.SnippetSearcher.Tests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tests")
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("/getAll")
    public List<Test> getAllTests() {
        return testService.getAllTests();
    }

    @GetMapping("/get/{id}")
    public Test getTestById(@PathVariable Long id) {
        return testService.getTestById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addTest(@RequestBody Test test) {
        testService.addTest(test);
        return ResponseEntity.ok("Test created successfully");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateTest(@PathVariable Long id, @RequestBody Test test) {
        testService.updateTest(id, test);
        return ResponseEntity.ok("Test updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTest(@PathVariable Long id) {
        testService.deleteTest(id);
        return ResponseEntity.ok("Test deleted");
    }

    @GetMapping("/execute/{id}")
    public void executeTest(@PathVariable Long id) {
    }
}

