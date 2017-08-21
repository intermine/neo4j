MATCH (gene :Gene),
(gene)-[:PART_OF]-(gene_organism :Organism),
(gene)-[:chromosome]-(gene_chromosome :Chromosome)
WHERE gene_chromosome.primaryIdentifier = '2L' AND gene_organism.name = 'Drosophila melanogaster'
RETURN gene.primaryIdentifier,
gene.symbol,
gene_chromosome.primaryIdentifier,
gene_chromosomelocation.start,
gene_chromosomelocation.end,
gene_chromosomelocation.strand
ORDER BY gene.primaryIdentifier ASC
