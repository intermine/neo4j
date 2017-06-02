MATCH (n)
WHERE NOT n:Metagraph
WITH labels(n) as LABELS, keys(n) as KEYS
MERGE (m:Metagraph {metaType: 'NodeType', labels: LABELS})
SET m.properties =
CASE m.properties
	WHEN NULL THEN KEYS
    ELSE apoc.coll.union(m.properties, KEYS)
END
