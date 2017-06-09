  // put together some examples of homologue data models

  // clear the slate
MATCH(n) DETACH DELETE n;

     // 1. straight-up copy of FlyMine model using PARTICIPATES_IN as Gene-Homologue relationship type
CREATE p1 =
       (g1:Gene {id:123451, name:"Gene.1"})
       -[:PARTICIPATES_IN]->
       (h:Homologue1 {id:123453, name:"Homo.1", bootstrapScore:1.23453, type:"orthologue"})
       <-[:PARTICIPATES_IN]-
       (g2:Gene {id:123452, name:"Gene.2"});
       
MATCH (h:Homologue1 {id:123453, name:"Homo.1", bootstrapScore:1.23453, type:"orthologue"}) CREATE (h)-[:CROSS_REFERENCED_BY]->(c:CrossReference {id:123461, name:"CR.1"});
MATCH (h:Homologue1 {id:123453, name:"Homo.1", bootstrapScore:1.23453, type:"orthologue"}) CREATE (h)-[:CROSS_REFERENCED_BY]->(c:CrossReference {id:123462, name:"CR.2"});

MATCH (h:Homologue1 {id:123453, bootstrapScore:1.23453, type:"orthologue"}) CREATE (h)-[:DATA_FROM]->(d:DataSet {id:123471, name:"DS.1"});
MATCH (h:Homologue1 {id:123453, bootstrapScore:1.23453, type:"orthologue"}) CREATE (h)-[:DATA_FROM]->(d:DataSet {id:123472, name:"DS.2"});
       
MATCH (h:Homologue1 {id:123453, bootstrapScore:1.23453, type:"orthologue"}) CREATE (h)-[:EVIDENCED_BY]->(e:Evidence {id:123481, name:"EV.1"});

MATCH (h:Homologue1 {id:123453, bootstrapScore:1.23453, type:"orthologue"}) CREATE (h)-[:PUBLISHED_IN]->(p:Publication {id:123491, name:"Pub.1"});
MATCH (h:Homologue1 {id:123453, bootstrapScore:1.23453, type:"orthologue"}) CREATE (h)-[:PUBLISHED_IN]->(p:Publication {id:123492, name:"Pub.2"});
       
       
      // 2. basic gene-homo-gene relationship
CREATE p = (g1:Gene {id:234551, name:"Gene.1"})-[:HOMOLOG_OF {name:"Homo.1", bootstrapScore:1.2345, type:"orthologue"}]->(g2:Gene {id:234552, name:"Gene.2"});


