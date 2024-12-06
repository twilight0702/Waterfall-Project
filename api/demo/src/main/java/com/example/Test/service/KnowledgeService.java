package com.example.Test.service;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.Result;
import org.neo4j.driver.SessionConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class KnowledgeService {

    @Autowired
    private final Driver neo4jDriver;

    // 使用构造方法注入 Neo4j 驱动
    public KnowledgeService(Driver neo4jDriver) {
        this.neo4jDriver = neo4jDriver;
    }

    public Map<String, Object> getKnowledgeById(String id, String databaseName) {
        try (Session session = neo4jDriver.session(SessionConfig.forDatabase(databaseName))) {
            String query = "MATCH (n:knowledge {id: $id}) RETURN properties(n) AS properties";
            Map<String, Object> params = new HashMap<>();
            params.put("id", id);

            Result result = session.run(query, params);

            if (result.hasNext()) {
                return result.next().get("properties").asMap();
            } else {
                Map<String, Object> notFoundResponse = new HashMap<>();
                notFoundResponse.put("message", "没找到");
                return notFoundResponse;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to query Neo4j: " + e.getMessage(), e);
        }
    }

    public Map<String, Object> getKnowledgeByString(String name, String databaseName) {
        try (Session session = neo4jDriver.session(SessionConfig.forDatabase(databaseName))) {
            String query = "MATCH (n:knowledge {name: $name}) RETURN properties(n) AS properties";
            Map<String, Object> params = new HashMap<>();
            params.put("name", name);

            Result result = session.run(query, params);

            if (result.hasNext()) {
                return result.next().get("properties").asMap();
            } else {
                Map<String, Object> notFoundResponse = new HashMap<>();
                notFoundResponse.put("message", "没找到");
                return notFoundResponse;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to query Neo4j: " + e.getMessage(), e);
        }
    }



    // 功能 1: 获取所有 subject 和 knowledge 节点以及它们的关系
    public Map<String, Object> getSubjectsKnowledgeAndRelationships(String databaseName) {
        try (Session session = neo4jDriver.session(SessionConfig.forDatabase(databaseName))) {
            String query = """
MATCH (n:subject)-[r]->(m:knowledge)
OPTIONAL MATCH (m)-[r2]->(k:knowledge)
WITH collect(DISTINCT {id: id(n), labels: labels(n), properties: properties(n), type: 'subject'}) AS subjectNodes,
     collect(DISTINCT {id: id(m), labels: labels(m), properties: properties(m), type: 'knowledge'}) AS knowledgeNodes,
     collect(DISTINCT {startNodeId: id(startNode(r)), endNodeId: id(endNode(r)), type: type(r), relationshipType: 'subject-to-knowledge'}) AS relationships1,
     collect(DISTINCT {startNodeId: id(startNode(r2)), endNodeId: id(endNode(r2)), type: type(r2), relationshipType: 'knowledge-to-knowledge'}) AS relationships2
WITH subjectNodes, knowledgeNodes, relationships1 + relationships2 AS Relationships
MATCH (allKnowledge:knowledge)  // 查询所有的 knowledge 节点
RETURN subjectNodes, collect(DISTINCT {id: id(allKnowledge), labels: labels(allKnowledge), properties: properties(allKnowledge), type: 'knowledge'}) AS allKnowledgeNodes, Relationships

""";

            Result result = session.run(query);

            if (result.hasNext()) {
                return result.next().asMap();
            } else {
                Map<String, Object> notFoundResponse = new HashMap<>();
                notFoundResponse.put("message", "没有找到任何数据");
                return notFoundResponse;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to query Neo4j: " + e.getMessage(), e);
        }
    }

    // 功能 2: 随机抽取指定数量的 test 节点
    public List<Map<String, Object>> getRandomTests(String databaseName, int limit) {
        try (Session session = neo4jDriver.session(SessionConfig.forDatabase(databaseName))) {
            String query = """
                    MATCH (n:test)
                    WITH n ORDER BY rand() LIMIT $limit
                    RETURN collect({properties: properties(n)}) AS nodes
                    """;
            Map<String, Object> params = new HashMap<>();
            params.put("limit", limit);

            Result result = session.run(query, params);

            if (result.hasNext()) {
                return result.next().get("nodes").asList(value -> value.asMap());
            } else {
                return new ArrayList<>(); // 如果没有数据返回空列表
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to query Neo4j: " + e.getMessage(), e);
        }
    }

    public List<Map<String, Object>> getRandomTestsHasLimits(String databaseName, int limit,List<String> labels) {
        try (Session session = neo4jDriver.session(SessionConfig.forDatabase(databaseName))) {
            String query = """
                    MATCH (n:test)
                    WHERE n.kn IN $labels
                    WITH n ORDER BY rand() LIMIT $limit
                    RETURN collect({properties: properties(n)}) AS nodes
                    """;
            Map<String, Object> params = new HashMap<>();
            params.put("limit", limit);
            params.put("labels", labels);

            System.out.println("数据库访问语句"+query);
            Result result = session.run(query, params);

            if (result.hasNext()) {
                return result.next().get("nodes").asList(value -> value.asMap());
            } else {
                return new ArrayList<>(); // 如果没有数据返回空列表
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to query Neo4j: " + e.getMessage(), e);
        }
    }
}