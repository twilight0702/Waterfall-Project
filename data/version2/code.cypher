//初始化数据库用
create (:subject{name:"瀑布模型"});

LOAD CSV WITH HEADERS FROM "file:///knowledge.csv" AS line
create (:knowledge{name:line.name,id:line.item_id,text:line.text});

LOAD CSV WITH HEADERS FROM "file:///associate.csv" AS line
MATCH (from:subject),(to:knowledge)
WHERE to.id=line.item_id
MERGE (from)-[r:关联]->(to);

LOAD CSV WITH HEADERS FROM "file:///include.csv" AS line
MATCH (from:knowledge),(to:knowledge)
WHERE from.id=line.item_id1 AND to.id=line.item_id2
MERGE (from)-[r:包含]->(to);

LOAD CSV WITH HEADERS FROM "file:///test.csv" AS line
create (:test{name:line.name,id:line.id,text:line.text,A:line.A,B:line.B,C:line.C,D:line.D,answer:line.answer,kn:line.kn});