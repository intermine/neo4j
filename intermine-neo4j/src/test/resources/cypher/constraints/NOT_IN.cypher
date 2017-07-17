MATCH (gene :Gene)

WHERE NOT Gene.primaryIdentifier IN []
RETURN gene.primaryIdentifier
ORDER BY gene.primaryIdentifier ASC
