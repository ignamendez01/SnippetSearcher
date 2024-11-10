package com.snippetSearcher.SnippetSearcher.Tests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService {
    @Autowired
    private TestRepository testRepository;

    public List<Test> getAllTests() {
        return testRepository.findAll();
    }

    public Test getTestById(Long id) {
        return testRepository.findById(id).orElse(null);
    }

    public void addTest(Test test) {
        testRepository.save(test);
    }

    public void updateTest(Long id, Test test) {
        testRepository.findById(id).ifPresent(t -> {
            if (test.getSnippetId() != null) {
                t.setSnippetId(test.getSnippetId());
            }
            if (test.getInputs() != null) {
                t.setInputs(test.getInputs());
            }
            if (test.getOutputs() != null) {
                t.setOutputs(test.getOutputs());
            }
            testRepository.save(t);
        });
    }

    public void deleteTest(Long id) {
        testRepository.deleteById(id);
    }
}

