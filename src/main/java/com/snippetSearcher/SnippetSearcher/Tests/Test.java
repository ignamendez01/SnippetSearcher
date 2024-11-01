package com.snippetSearcher.SnippetSearcher.Tests;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "Tests")
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "snippet_id")
    private Long snippetId;

    @ElementCollection
    private List<String> inputs;

    @ElementCollection
    private List<String> outputs;

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public Long getSnippetId() {
        return snippetId;
    }

    public List<String> getInputs() {
        return inputs;
    }

    public List<String> getOutputs() {
        return outputs;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSnippetId(Long snippetId) {
        this.snippetId = snippetId;
    }

    public void setInputs(List<String> inputs) {
        this.inputs = inputs;
    }

    public void setOutputs(List<String> outputs) {
        this.outputs = outputs;
    }
}

