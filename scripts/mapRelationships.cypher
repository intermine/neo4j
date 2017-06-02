MATCH (n)-[r]->(m)
WHERE NOT n:Metagraph AND NOT m:Metagraph
WITH labels(n) as start_labels, type(r) as rel_type, keys(r) as rel_keys, labels(m) as end_labels
MERGE (a:Metagraph {metaType:'NodeType', labels: start_labels})
MERGE (b:Metagraph {metaType:'NodeType', labels: end_labels})
MERGE (a)<-[:StartNodeType]-(rel:Metagraph {metaType: 'RelType', type:rel_type })-[:EndNodeType]->(b)
SET rel.properties =
CASE
    WHEN rel.properties IS NULL AND rel_keys IS NULL THEN []
    WHEN rel.properties IS NULL AND rel_keys IS NOT NULL THEN rel_keys
    WHEN rel.properties IS NOT NULL AND rel_keys IS NULL THEN rel.properties
    WHEN rel.properties IS NOT NULL AND rel_keys IS NOT NULL THEN apoc.coll.union(rel.properties, rel_keys)
END

