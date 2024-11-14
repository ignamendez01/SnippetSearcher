package com.snippetSearcher.SnippetSearcher.Tests;

import com.snippetSearcher.SnippetSearcher.Snippets.Snippet;
import com.snippetSearcher.SnippetSearcher.Snippets.SnippetService;
import interpreter.Administrator;
import interpreter.Interpreter;
import interpreter.InterpreterFactory;
import interpreter.response.ErrorResponse;
import interpreter.response.InterpreterResponse;
import interpreter.response.SuccessResponse;
import lexer.LexerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lexer.Lexer;
import parser.Parser;
import parser.ParserFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    public Test getTestById(@PathVariable Long id) {
        return testService.getTestById(id);
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
        Test test = testService.getTestById(id);
        if (test != null) {
            Snippet s = snippetService.getSnippetById(test.getSnippetId());

            Interpreter interpreter = InterpreterFactory.interpreterVersion(new Administrator(), s.getVersion());
            Parser parser = ParserFactory.parserVersion(s.getVersion());
            Lexer lexer = LexerFactory.lexerVersion(s.getVersion());

            for (int i = 0; i < test.getInputs().size(); i++) {
                System.setIn(new ByteArrayInputStream(test.getInputs().get(i).getBytes()));
            }

            List<String> outputMessages = new ArrayList<>();
            try {
                InputStream stream = new ByteArrayInputStream(s.getCode().getBytes());
                InterpreterResponse response = interpreter.interpretAST(parser.generateAST(lexer.makeTokens(stream)));

                if (response instanceof SuccessResponse) {
                    for (int i = 0; i < test.getOutputs().size(); i++) {
                        String result = interpreter.getAdmin().getPrintedElements().poll();

                        if (!Objects.equals(test.getOutputs().get(i), result)) {
                            String errorMessage = "Mismatch in output: " + result + " instead of " + test.getOutputs().get(i);
                            outputMessages.add(errorMessage);
                            throw new Exception(errorMessage);
                        } else {
                            outputMessages.add("Output match: " + result);
                        }
                    }
                    outputMessages.add("Test successfully executed");
                    return ResponseEntity.ok(String.join("\n", outputMessages));
                } else {
                    throw new Exception(((ErrorResponse) response).message());
                }

            } catch (Exception e) {
                outputMessages.add("Error: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(String.join("\n", outputMessages));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No test with that id exists");
        }
    }

}

