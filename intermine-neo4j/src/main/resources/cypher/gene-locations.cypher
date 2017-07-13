MATCH (gene :Gene),
(gene)-[:chromosomeLocation]-(gene_chromosomelocation :Location),
(gene)-[:chromosome]-(gene_chromosome :Chromosome)


RETURN gene.primaryIdentifier,
gene_chromosomelocation.start,
gene_chromosomelocation.end,
gene_chromosomelocation.strand,
gene_chromosome.primaryIdentifier
ORDER BY gene.primaryIdentifier ASC
