MATCH (gene :Gene)
RETURN gene.symbol
ORDER BY gene.length DESC,
gene.name ASC
