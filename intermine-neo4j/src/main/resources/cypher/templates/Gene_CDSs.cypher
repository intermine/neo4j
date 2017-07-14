MATCH (gene :Gene),
(gene)-[:PART_OF]-(gene_organism :Organism),
(gene)-[:CDSs]-(gene_cdss :CDS)

WHERE gene_organism.name = 'Drosophila melanogaster'
RETURN gene.symbol,
gene.primaryIdentifier,
gene_cdss.primaryIdentifier
ORDER BY gene.symbol ASC
