package com.example.Test.service;

import org.neo4j.driver.*;
import org.neo4j.driver.Record;
import org.neo4j.driver.types.MapAccessor;
import org.neo4j.driver.types.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.List;

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
                return result.next().get("nodes").asList(MapAccessor::asMap);
            } else {
                return new ArrayList<>(); // 如果没有数据返回空列表
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to query Neo4j: " + e.getMessage(), e);
        }
    }

    public List<Map<String, Object>> getRandomTestsHasLimits(String databaseName, int limit, List<String> labels) {
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

            Result result = session.run(query, params);

            if (result.hasNext()) {
                return result.next().get("nodes").asList(MapAccessor::asMap);
            } else {
                return new ArrayList<>(); // 如果没有数据返回空列表
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to query Neo4j: " + e.getMessage(), e);
        }
    }

    /// 在数据库中存储用户信息
    public Boolean saveUserNode(String name, String databaseName) {
        try (Session session = neo4jDriver.session(SessionConfig.forDatabase(databaseName))) {
            String query = "CREATE (:User {name: $name})";
            Map<String, Object> params = new HashMap<>();
            params.put("name", name);

            session.run(query, params);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /// 在数据库中存储题目数据
    ///
    /// @param testData     前端返回题目数据
    /// @param userName     写这些题目的用户名字
    /// @param databaseName 存储的数据库名字
    public Boolean saveTest(List<Map<String, Map<String, Object>>> testData, String userName, String databaseName, String rela) {
        try (Session session = neo4jDriver.session(SessionConfig.forDatabase(databaseName))) {
            for (Map<String, Map<String, Object>> test : testData) {
                Map<String, Object> properties = test.get("properties");
                if (!checkIfTheTestHasBeenDone(userName, (String) properties.get("id"), rela)) {
                    System.out.println("题目没有存储过");
                    session.executeWrite(tx -> saveNode(tx, properties, userName, rela));
                } else {
                    System.out.println("题目已经存储过");
                }
            }
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /// 存储题目节点
    private Void saveNode(TransactionContext tx, Map<String, Object> properties, String userName, String rela) {
        String cypherQuery = String.format("MATCH (u:User {name: $userName}), (t:test {id: $id}) CREATE (u)-[:%s]->(t)", rela);
        tx.run(cypherQuery, Map.of("userName", userName, "id", properties.get("id")));
        return null;
    }

    /// 判断这道题目是否已经存储过了
    ///
    /// @return true表示已经存储过了，false表示没有存储过
    private Boolean checkIfTheTestHasBeenDone(String userName, String testID, String rela) {
        try (Session session = neo4jDriver.session()) {
            String cypherQuery = String.format("MATCH (u:User {name: $userName})-[r:%s]->(t:test {id: $testID}) RETURN count(r) > 0", rela);
            Map<String, Object> params = new HashMap<>();
            params.put("userName", userName);
            params.put("testID", testID);
            Result result = session.run(cypherQuery, params);

            if (result.hasNext()) {
                return result.next().get("count(r) > 0").asBoolean();
            } else {
                throw new RuntimeException("查询做过的题目出错!");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    /// 获取用户写过的所有题目
    public Map<String, Object> getDoneTests(String userName, String databaseName, String rela) {
        Map<String, Object> nodes = new HashMap<>();
        List<Map<String, Object>> tests = new ArrayList<>();
        try (Session session = neo4jDriver.session(SessionConfig.forDatabase(databaseName))) {
            String query = String.format("MATCH (u:User {name: $userName})-[r:%s]->(t:test) RETURN t", rela);
            Map<String, Object> params = new HashMap<>();
            params.put("userName", userName);
            Result result = session.run(query, params);

            if (result.hasNext()) {
                nodes.put("status", "ok");
            } else {
                nodes.put("status", "no Tests");
                return nodes;
            }
            while (result.hasNext()) {
                Record record = result.next();
                Node node = record.get("t").asNode();
                Map<String, Object> tempTest = node.asMap();
                tests.add(tempTest);
            }
            nodes.put("tests", tests);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            nodes.put("status", "error");
        }
        return nodes;
    }

    /// 保存测试信息
    public Boolean saveExamination(Map<String, Object> examination, String userName, String databaseName) {
        String grade = (String) examination.get("grade");
        String number_of_tests = (String) examination.get("number_of_tests");
        String correctness = (String) examination.get("correctness");
        String test_list = (String) examination.get("test_list");

        String[] testList = test_list.split(",");
        try (Session session = neo4jDriver.session(SessionConfig.forDatabase(databaseName))) {
            String createExaminationQuery = "CREATE (n:Examination {grade: $grade, number_of_tests: $number_of_tests, correctness: $correctness, test_list: $test_list}) RETURN ID(n)";
            Map<String, Object> examinationParams = Map.of(
                    "grade", grade,
                    "number_of_tests", number_of_tests,
                    "correctness", correctness,
                    "test_list", test_list
            );
            Long examNodeID = session.run(createExaminationQuery, examinationParams)
                    .single()
                    .get("ID(n)")
                    .asLong();

            // 将 User 和 Examination 节点相连
            String linkQuery = "MATCH (u:User {name: $userName}), (e:Examination) WHERE ID(e) = $examinationId CREATE (u)-[:EXAM]->(e)";
            Map<String, Object> linkParams = Map.of(
                    "userName", userName,
                    "examinationId", examNodeID
            );
            session.run(linkQuery, linkParams);
            for (String testID : testList) {
                String query = "MATCH (n:Examination),(t:test{id:$testID}) WHERE ID(n) =$examNodeID CREATE (n)-[:HAS_TEST]->(t)";
                session.run(query, Map.of("testID", testID, "examNodeID", examNodeID));
            }

            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    ///获取历次考试信息
//    public Map<String, Object> getExaminations(String userName,String databaseName) {
//    }


    /// (新）存储用户题目信息
    public Boolean saveUserTests(Map<String, Object> data, String databaseName) {
        String userName = (String) data.get("username");
        String score = (String) data.get("score");
        String testID = (String) data.get("testId");
        List<Map<String, Object>> tests = (List<Map<String, Object>>) data.get("results");

        try (Session session = neo4jDriver.session(SessionConfig.forDatabase(databaseName))) {
            //查看有没有该用户，没有就创建一个
            String findUserQuery = "MATCH (n:User{name:$userName}) RETURN n";
            Result userResult = neo4jDriver.session().run(findUserQuery, Map.of("userName", userName));
            if (!userResult.hasNext()) {
                saveUserNode(userName, databaseName);
            }

            //创建 UserExam 节点
            String createUserExamQuery = "CREATE (n:UserExam {score: $score, testID: $testID}) RETURN ID(n)";
            Map<String, Object> userExamParams = Map.of(
                    "score", score,
                    "testID", testID
            );
            Result result = session.run(createUserExamQuery, userExamParams);
            long userExamID = result.single().get("ID(n)").asLong();

            //把用户节点和UserExam节点相连
            createUserExamQuery = "MATCH (n:User{name:$userName}),(m:UserExam) WHERE ID(m)=$userExamID CREATE (n)-[:HAS_EXAM]->(m)";
            session.run(createUserExamQuery, Map.of("userName", userName, "userExamID", userExamID));

            //创建当此考试的测试节点并与UserExam节点相连
            String query = "CREATE (n:ExamTest{id:$tempTestID,answer:$answer,userAnswer:$userAnswer,isCorrect:$isCorrect,kn:$kn}) RETURN ID(n)";
            String query2 = "MATCH (n:UserExam),(m:ExamTest) WHERE ID(n)=$userExamID AND ID(m)=$examTestID CREATE (n)-[:HAS_TEST]->(m)";
            for (Map<String, Object> test : tests) {
                String tempTestID = (String) test.get("id");
                String answer = (String) test.get("answer");
                String userAnswer = (String) test.get("userAnswer");
                String isCorrect = (String) test.get("isCorrect");

                //查询这道题目的所属知识点
                String findKN="MATCH (n:test{id:$tempTestID}) RETURN n.kn";
                Result findKNr_Result=session.run(findKN,Map.of("tempTestID",tempTestID));
                String kn=findKNr_Result.single().get("n.kn").asString();

                result = session.run(query, Map.of("tempTestID", tempTestID, "answer", answer, "userAnswer", userAnswer, "isCorrect", isCorrect,"kn",kn));
                long examTestID = result.single().get("ID(n)").asLong();
                session.run(query2, Map.of("userExamID", userExamID, "examTestID", examTestID));
            }
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /// 获取用户题目信息
    public List<Map<String, Object>> getUserTests(String userName, String databaseName) {
        List<Map<String, Object>> returnResult = new ArrayList<>();

        try (Session session = neo4jDriver.session(SessionConfig.forDatabase(databaseName))) {
            // 查询用户的测试及关联的题目
            String query = """
                        MATCH (u:User {name: $userName})-[:HAS_EXAM]->(exam:UserExam)
                        OPTIONAL MATCH (exam)-[:HAS_TEST]->(test:ExamTest)
                        RETURN exam, collect(test) AS tests
                    """;
            Result result = session.run(query, Map.of("userName", userName));

            while (result.hasNext()) {
                Record record = result.next();
                Node userExamNode = record.get("exam").asNode(); // 获取 UserExam 节点
                List<Object> tests = record.get("tests").asList(Value::asNode); // 获取关联的 ExamTest 节点

                // 将 UserExam 的属性存储到 Map 中
                Map<String, Object> examData = userExamNode.asMap();
                Map<String, Object> examData1 = new HashMap<>(examData);
                // 将 ExamTest 的属性存储到 List 中
                List<Map<String, Object>> testList = new ArrayList<>();
                for (Object test : tests) {
                    testList.add(((Node) test).asMap());
                }

                // 添加测试列表到结果中
                examData1.put("results", testList);
                returnResult.add(examData1);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return returnResult;
    }


    ///用id获取题目信息
    public Map<String, Object> getTestById(String id,String databaseName) {
        try (Session session = neo4jDriver.session(SessionConfig.forDatabase(databaseName))) {
            String query = "MATCH (n:test) WHERE n.id=$id RETURN n";
            Result result = session.run(query, Map.of("id", id));
            return result.single().get("n").asMap();
        }catch(Exception e)
        {
            System.out.println(e.getMessage());
            return new HashMap<>();
        }
    }
}