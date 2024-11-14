package com.snippetSearcher.SnippetSearcher.Tests;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TestRepository extends JpaRepository<Test, Long> {
    @Query("SELECT t FROM Test t WHERE t.snippetId = :snippetId")
    List<Test> findTestsBySnippetId(@Param("snippetId") Long snippetId);
}

