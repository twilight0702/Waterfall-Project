package com.example.Test.controller;

import com.example.Test.service.KnowledgeService;
import com.example.Test.service.CurrUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
        for (String s : labels) {
            System.out.print(s + "  ");
        }
        System.out.println(" ");

        List<Map<String, Object>> result = knowledgeService.getRandomTestsHasLimits(databaseName, limit, labels);
        if (result.isEmpty()) {
            return ResponseEntity.status(404).body(Collections.emptyList());
        }
        return ResponseEntity.ok(result);
    }

    /// 存储用户信息
    ///
    /// @param userData: 用户信息，包括一个 name 字段
    /// @return 返回存储结果
    @PostMapping("/saveUserNode")
    public ResponseEntity<String> saveUserNode(@RequestBody Map<String, Object> userData) {
        String name = (String) userData.get("userName");
        if (knowledgeService.saveUserNode(name, databaseName)) {
            System.out.printf("存储用户信息成功！名字:%s\n", name);
            return ResponseEntity.ok("存储用户信息成功！");
        }
        return ResponseEntity.status(500).body("存储用户信息失败");
    }

    /// 存储题目（同时可以用于写过的题目和错题存储）
    ///
    /// @param data 输入的请求体Map<String,Object>，包括一个题目信息的list，名字是testData，一个用户信息，名字是userName
    /// @param rela 需要保存的题目类型，如果是写过的题目，是HAVE_DONE，如果是错题，是INCORRECT
    @PostMapping("/saveTest")
    public ResponseEntity<String> saveDoneTest(@RequestBody Map<String, Object> data,
                                               @RequestParam String rela) {
        List<Map<String, Map<String, Object>>> testData = (List<Map<String, Map<String, Object>>>) data.get("testData");
        String userIDString = (String) data.get("userName");
        if (knowledgeService.saveTest(testData, userIDString, databaseName, rela)) {
            System.out.println("存储题目数据成功");
            return ResponseEntity.ok("存储题目数据成功");
        }
        return ResponseEntity.status(500).body("存储题目数据失败");
    }

    /// 获取用户写过的所有题目
    ///
    /// @param userName 是被查询的用户的名字
    /// @param rela     需要查询的题目类型，如果是写过的题目，是HAVE_DONE，如果是错题，是INCORRECT
    @GetMapping("/getTest")
    public ResponseEntity<Map<String, Object>> getDoneTests(@RequestParam String userName, @RequestParam String rela) {
        Map<String, Object> returnResult = knowledgeService.getDoneTests(userName, databaseName, rela);

        return ResponseEntity.ok(returnResult);
    }

    /// 存储测试信息节点
    @PostMapping("/saveExamination")
    public ResponseEntity<String> saveTestInfo(@RequestBody Map<String, Object> data) {
        String userName = (String) data.get("userName");
        Map<String, Object> examination = (Map<String, Object>) data.get("examination");
        if (knowledgeService.saveExamination(examination, userName, databaseName)) {
            return ResponseEntity.ok("存储测试信息成功");
        } else {
            return ResponseEntity.status(500).body("存储测试信息失败");
        }
    }

    ///获取测试信息节点
//    @GetMapping("/getExamination")
//    public ResponseEntity<Map<String, Object>> getExamination(@RequestParam String userName) {
//    }


    /// 添加在线用户
    @GetMapping("/addUser")
    public ResponseEntity<String> addUser(@RequestParam Integer key, @RequestParam String userName) {
        CurrUser.addUser(key, userName);
        return ResponseEntity.ok("设置用户名成功");
    }

    /// 用key查询在线用户的名字
    @GetMapping("/findUser")
    public ResponseEntity<String> findUser(@RequestParam Integer key) {
        String name = CurrUser.findUser(key);
        return ResponseEntity.ok(name);
    }

    /// 删除在线用户
    @GetMapping("/removeUser")
    public ResponseEntity<String> removeUser(@RequestParam Integer key) {
        CurrUser.removeUser(key);
        return ResponseEntity.ok("删除用户成功");
    }


    /// 存储用户题目信息（新版）
    @PostMapping("/submit-test")
    public ResponseEntity<String> saveUserTests(@RequestBody Map<String, Object> data) {
        if(knowledgeService.saveUserTests(data, databaseName)) {
            return ResponseEntity.ok("success");
        }
        else{
            return ResponseEntity.status(500).body("存储用户题目信息失败");
        }
    }

    ///获取用户题目信息（新版）
    @GetMapping("/getUserTests")
    public ResponseEntity<List<Map<String, Object>>> getUserTests(@RequestParam String userName) {
        List<Map<String,Object>> rr=new ArrayList<>();
        Map<String,Object> map= new HashMap<>();
        map.put("123","123");
        rr.add(map);
        List<Map<String, Object>> result=knowledgeService.getUserTests(userName,databaseName);
        return ResponseEntity.ok(result);
        //return ResponseEntity.ok(rr);
    }


    ///用题目id查询题目信息
    @GetMapping("/getTestById")
    public ResponseEntity<Map<String, Object>> getTestById(@RequestParam String id) {
        Map<String, Object> result = knowledgeService.getTestById(id, databaseName);
        return ResponseEntity.ok(result);
    }
}
