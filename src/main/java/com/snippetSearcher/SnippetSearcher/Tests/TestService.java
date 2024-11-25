package com.snippetSearcher.SnippetSearcher.Tests;

import com.snippetSearcher.SnippetSearcher.Snippets.Snippet;
import interpreter.Administrator;
import interpreter.Interpreter;
import interpreter.InterpreterFactory;
import interpreter.response.ErrorResponse;
import interpreter.response.InterpreterResponse;
import interpreter.response.SuccessResponse;
import lexer.Lexer;
import lexer.LexerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parser.Parser;
import parser.ParserFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

@Service
public class TestService {
    private Logger logger = LoggerFactory.getLogger(TestService.class);
    @Autowired
    private TestRepository testRepository;

    public List<Test> getAllTests() {
        List<Test> tests = testRepository.findAll();
        if (tests.isEmpty()){
            logger.error("List is empty");
            throw new EntityNotFoundException();
        }else{
            logger.info("Full list of tests");
            return tests;
        }
    }

    public Test getTestById(Long id) {
        return testRepository.findById(id)
                .map(test -> {
                    logger.info("Test found");
                    return test;
                })
                .orElseThrow(() -> {
                    logger.error("Test doesn't exist");
                    return new EntityNotFoundException("Test with ID " + id + " not found");
                });
    }

    public void addTest(Test test) {
        if (test.getSnippetId() == null || test.getSnippetId() < 1) {
            logger.error("Missing element to create test");
            throw new IllegalArgumentException("Snippet ID cannot be null or less than 1");
        }
        if (test.getOutputs() == null || test.getOutputs().isEmpty()) {
            logger.error("Missing element to create test");
            throw new IllegalArgumentException("Output list cannot be null or empty");
        }
        logger.info("Test created");
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
                    logger.info("Test updated");
                    testRepository.save(t);
                },
                () -> {
                    logger.error("No test to update");
                    throw new EntityNotFoundException("Test with ID " + id + " not found");
                }
        );
    }

    public void deleteTest(Long id) {
        testRepository.findById(id).ifPresentOrElse(
                test -> {
                    logger.info("Test deleted");
                    testRepository.deleteById(id);
                },
                () -> {
                    logger.error("No test to delete");
                    throw new EntityNotFoundException("Test with ID " + id + " not found");
                }
        );
    }

    public String executeTest(Test test, Snippet s) throws Exception {
        Interpreter interpreter = InterpreterFactory.interpreterVersion(new Administrator(), s.getVersion());
        Parser parser = ParserFactory.parserVersion(s.getVersion());
        Lexer lexer = LexerFactory.lexerVersion(s.getVersion());

        for (int i = 0; i < test.getInputs().size(); i++) {
            System.setIn(new ByteArrayInputStream(test.getInputs().get(i).getBytes()));
        }

        StringBuilder outputMessages = new StringBuilder();
        InputStream stream = new ByteArrayInputStream(s.getCode().getBytes());

        try {
            InterpreterResponse response = interpreter.interpretAST(parser.generateAST(lexer.makeTokens(stream)));
            if (response instanceof SuccessResponse) {
                for (int i = 0; i < test.getOutputs().size(); i++) {
                    String result = interpreter.getAdmin().getPrintedElements().poll();

                    if (!Objects.equals(test.getOutputs().get(i), result)) {
                        String errorMessage = "Mismatch in output: " + result + " instead of " + test.getOutputs().get(i);
                        outputMessages.append(errorMessage);
                        throw new Exception(outputMessages.toString());
                    } else {
                        outputMessages.append(result).append("\n");
                    }
                }
                logger.info("Test executed");
                outputMessages.append("Test successfully executed");
                return outputMessages.toString();

            } else {
                throw new Exception(((ErrorResponse) response).message());
            }
        } catch (Exception e) {
            logger.error("Test failed");
            throw new Exception(e.getMessage());
        }
    }

    public List<Test> getTestsBySnippetId(Long snippetId) {
        return testRepository.findTestsBySnippetId(snippetId);
    }
}

