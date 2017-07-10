MATCH (n)
WHERE NOT n:Metagraph AND size(labels(n))>0
WITH labels(n) as LABELS, keys(n) as KEYS
MERGE (m:Metagraph:NodeType {labels: LABELS})
SET m.properties =
CASE m.properties
	WHEN NULL THEN KEYS
    ELSE apoc.coll.union(m.properties, KEYS)
END

