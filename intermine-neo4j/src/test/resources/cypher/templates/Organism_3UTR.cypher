MATCH (gene :Gene),
(gene)-[:PART_OF]-(gene_organism :Organism),
(gene)-[:gene]-(gene_utrs :UTR)
WHERE gene_organism.name = 'Drosophila melanogaster'
RETURN gene_utrs.primaryIdentifier,
gene_utrs.length,
gene_utrs_chromosomelocation.start,
gene_utrs_chromosomelocation.end,
gene.secondaryIdentifier
ORDER BY gene_utrs.primaryIdentifier ASC
