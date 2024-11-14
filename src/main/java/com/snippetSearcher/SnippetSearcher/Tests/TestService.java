package com.snippetSearcher.SnippetSearcher.Tests;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        if (test.getSnippetId() == null) {
            throw new IllegalArgumentException("Snippet ID cannot be null");
        }
        if (test.getOutputs() == null || test.getOutputs().isEmpty()) {
            throw new IllegalArgumentException("Output list cannot be null or empty");
        }
        testRepository.save(test);
    }

    public void updateTest(Long id, Test test) {
        testRepository.findById(id).ifPresentOrElse(
                t -> {
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
                },
                () -> {
                    throw new EntityNotFoundException("Test with ID " + id + " not found");
                }
        );
    }

    public void deleteTest(Long id) {
        testRepository.findById(id).ifPresentOrElse(
                test -> {
                    testRepository.deleteById(id);
                },
                () -> {
                    throw new EntityNotFoundException("Test with ID " + id + " not found");
                }
        );
    }

    public List<Test> getTestsBySnippetId(Long snippetId) {
        return testRepository.findTestsBySnippetId(snippetId);
    }
}

