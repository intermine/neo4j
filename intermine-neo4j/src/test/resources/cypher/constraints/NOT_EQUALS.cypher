MATCH (gene :Gene)
WHERE NOT gene.length = 55
RETURN gene.primaryIdentifier
ORDER BY gene.primaryIdentifier ASC
