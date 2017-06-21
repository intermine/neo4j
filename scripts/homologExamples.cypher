  // put together some examples of homologue data models
  
  // clear the slate
MATCH(n) DETACH DELETE n;
     
     // 1. straight-up copy of FlyMine model using PARTICIPATES_IN as Gene-Homologue relationship type
CREATE p1 =
       (g1:Gene {id:123451, name:"Gene.1"})
       -[:PARTICIPATES_IN]->
       (h:Homologue {id:223451, name:"Homo.1", bootstrapScore:1.2345, type:"orthologue"})
       <-[:PARTICIPATES_IN]-
       (g2:Gene {id:123452, name:"Gene.2"});
       
MATCH (h:Homologue {id:223451}) CREATE (h)-[:CROSS_REFERENCED_BY]->(c:CrossReference {id:123461, name:"CR.1"});
MATCH (h:Homologue {id:223451}) CREATE (h)-[:CROSS_REFERENCED_BY]->(c:CrossReference {id:123462, name:"CR.2"});
MATCH (h:Homologue {id:223451}) CREATE (h)-[:DATA_FROM]->(d:DataSet {id:123471, name:"DS.1"});
MATCH (h:Homologue {id:223451}) CREATE (h)-[:DATA_FROM]->(d:DataSet {id:123472, name:"DS.2"});
MATCH (h:Homologue {id:223451}) CREATE (h)-[:EVIDENCED_BY]->(e:Evidence {id:123481, name:"EV.1"});
MATCH (h:Homologue {id:223451}) CREATE (h)-[:PUBLISHED_IN]->(p:Publication {id:123491, name:"Pub.1"});
MATCH (h:Homologue {id:223451}) CREATE (h)-[:PUBLISHED_IN]->(p:Publication {id:123492, name:"Pub.2"});

      // 2. basic gene-homo-gene relationship
CREATE p1 =
       (g1:Gene {id:223451, name:"Gene.1"})
       -[:HOMOLOGUE_OF {name:"Homo.1", bootstrapScore:1.2345, type:"orthologue"}]->
       (g2:Gene {id:223452, name:"Gene.2"});
       

      // REPEAT
      
MATCH (g1:Gene {id:123451, name:"Gene.1"}) CREATE p1 = 
      (g1)-[:PARTICIPATES_IN]->
      (h:Homologue {id:223452, name:"Homo.2", bootstrapScore:2.2345, type:"orthologue"})
      <-[:PARTICIPATES_IN]-
      (g3:Gene {id:123453, name:"Gene.3"});
       
MATCH (h:Homologue {id:223452}) CREATE (h)-[:CROSS_REFERENCED_BY]->(c:CrossReference {id:223461, name:"CR.1"});
MATCH (h:Homologue {id:223452}) CREATE (h)-[:CROSS_REFERENCED_BY]->(c:CrossReference {id:223462, name:"CR.2"});
MATCH (h:Homologue {id:223452}) CREATE (h)-[:DATA_FROM]->(d:DataSet {id:223471, name:"DS.1"});
MATCH (h:Homologue {id:223452}) CREATE (h)-[:DATA_FROM]->(d:DataSet {id:223472, name:"DS.2"});
MATCH (h:Homologue {id:223452}) CREATE (h)-[:EVIDENCED_BY]->(e:Evidence {id:223481, name:"EV.1"});
MATCH (h:Homologue {id:223452}) CREATE (h)-[:PUBLISHED_IN]->(p:Publication {id:223491, name:"Pub.1"});
MATCH (h:Homologue {id:223452}) CREATE (h)-[:PUBLISHED_IN]->(p:Publication {id:223492, name:"Pub.2"});

MATCH (g1:Gene {id:223451, name:"Gene.1"}) CREATE p2 =
      (g1)-[:HOMOLOGUE_OF {name:"Homo.2", bootstrapScore:2.2345, type:"orthologue"}]->
      (g3:Gene {id:223453, name:"Gene.3"});


