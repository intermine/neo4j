MATCH (gene :Gene),
(gene)-[:PART_OF]-(gene_organism :Organism),
(gene)-[:chromosomeLocation]-(gene_chromosomelocation :Location),
(gene)-[:chromosome]-(gene_chromosome :Chromosome)

WHERE gene_organism.name = 'Drosophila melanogaster'
RETURN gene_chromosome.primaryIdentifier,
gene_chromosomelocation.strand,
gene_chromosomelocation.start,
gene_chromosomelocation.end,
gene.length,
gene.symbol,
gene.secondaryIdentifier
ORDER BY gene_chromosome.primaryIdentifier ASC
