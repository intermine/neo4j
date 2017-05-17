  // create a temporary label to contain InterMineID nodes that DO match with another node
  // create a relationship between the above and InterMineID
MATCH (n:InterMineID),(m) WHERE n.id=m.id AND NOT m:InterMineID CREATE (c:Completed)-[:matches]->(n);

      // delete orphans from InterMineID
MATCH (n:InterMineID) WHERE NOT (n)--() DELETE n;
      
      // delete the temp label nodes
MATCH (c:Completed) DETACH DELETE c;
      
      
