MATCH (gene :Gene)
WHERE Gene.primaryIdentifier IN []
RETURN gene.primaryIdentifier
ORDER BY gene.primaryIdentifier ASC
