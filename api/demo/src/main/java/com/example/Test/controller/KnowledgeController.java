package com.example.Test.controller;

import com.example.Test.service.KnowledgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/knowledge")
public class KnowledgeController {

    String databaseName = "neo4j";
    private final KnowledgeService knowledgeService;

    public KnowledgeController(KnowledgeService knowledgeService) {
        this.knowledgeService = knowledgeService;
    }

    @GetMapping("/by_id/{id}")
    public ResponseEntity<Map<String, Object>> getKnowledgeById(
            @PathVariable String id,
            @RequestParam(defaultValue = "neo4j") String databaseName) {
        // 调用服务层，传入 ID 和数据库名称
        Map<String, Object> knowledge = knowledgeService.getKnowledgeById(id, databaseName);

        if (knowledge.containsKey("message") && "没找到".equals(knowledge.get("message"))) {
            // 返回 HTTP 状态码 404（Not Found）和错误信息
            return ResponseEntity.status(404).body(knowledge);
        } else {
            // 返回 HTTP 状态码 200（OK）和查询结果
            return ResponseEntity.ok(knowledge);
        }
    }

    @GetMapping("/by_name/{name}")
    public ResponseEntity<Map<String, Object>> getKnowledgeByString(
            @PathVariable String name,
            @RequestParam(defaultValue = "neo4j") String databaseName) {
        // 调用服务层，传入 ID 和数据库名称
        Map<String, Object> knowledge = knowledgeService.getKnowledgeByString(name, databaseName);

        if (knowledge.containsKey("message") && "没找到".equals(knowledge.get("message"))) {
            // 返回 HTTP 状态码 404（Not Found）和错误信息
            return ResponseEntity.status(404).body(knowledge);
        } else {
            // 返回 HTTP 状态码 200（OK）和查询结果
            return ResponseEntity.ok(knowledge);
        }
    }


    // 功能 1: 获取所有 subject 和 knowledge 节点及其关系
    @GetMapping("/nodes-and-relationships")
    public ResponseEntity<Map<String, Object>> getSubjectsKnowledgeAndRelationships(
            @RequestParam(required = false, defaultValue = "neo4j") String databaseName) {
        Map<String, Object> result = knowledgeService.getSubjectsKnowledgeAndRelationships(databaseName);
        if (result.containsKey("message") && "没有找到任何数据".equals(result.get("message"))) {
            return ResponseEntity.status(404).body(result);
        }
        return ResponseEntity.ok(result);
    }

    // 功能 2: 随机抽取指定数量的 test 节点
    @GetMapping("/random-tests")
    public ResponseEntity<List<Map<String, Object>>> getRandomTests(
            @RequestParam int limit) {
        List<Map<String, Object>> result = knowledgeService.getRandomTests(databaseName, limit);
        if (result.isEmpty()) {
            return ResponseEntity.status(404).body(Collections.emptyList());
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/random-tests-has-limits")
    public ResponseEntity<List<Map<String, Object>>> getRandomTestsHasLimits(
            @RequestParam int limit,
            @RequestParam List<String> labels) {

        //调试用输出
        System.out.print("前端访问的题目知识点：");
        for(String s:labels)
        {
            System.out.print(s+"  ");
        }
        System.out.println(" ");

        List<Map<String, Object>> result = knowledgeService.getRandomTestsHasLimits(databaseName, limit , labels);
        if (result.isEmpty()) {
            return ResponseEntity.status(404).body(Collections.emptyList());
        }
        return ResponseEntity.ok(result);
    }
}
