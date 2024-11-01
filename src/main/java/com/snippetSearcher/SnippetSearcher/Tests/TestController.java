package com.snippetSearcher.SnippetSearcher.Tests;

import org.springframework.beans.factory.annotation.Autowired;
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
    public void addTest(@RequestBody Test test) {
        testService.addTest(test);
    }

    @PutMapping("/update/{id}")
    public void updateTest(@PathVariable Long id, @RequestBody Test test) {
        testService.updateTest(id, test);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteTest(@PathVariable Long id) {
        testService.deleteTest(id);
    }

    @GetMapping("/execute/{id}")
    public void executeTest(@PathVariable Long id) {

    }
}

