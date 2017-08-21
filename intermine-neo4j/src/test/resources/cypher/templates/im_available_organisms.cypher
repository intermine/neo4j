MATCH (gene :Gene),
(gene)-[:PART_OF]-(gene_organism :Organism)
WHERE gene.primaryIdentifier = '*'
RETURN gene_organism.shortName
ORDER BY gene_organism.shortName ASC
