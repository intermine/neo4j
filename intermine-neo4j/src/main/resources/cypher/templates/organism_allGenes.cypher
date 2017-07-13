MATCH (gene :Gene),
(gene)-[:PART_OF]-(gene_organism :Organism),
(gene)-[:chromosomeLocation]-(gene_chromosomelocation :Location),
(gene)-[:chromosome]-(gene_chromosome :Chromosome)

WHERE gene_organism.name = 'Drosophila melanogaster'
RETURN gene.primaryIdentifier,
gene.secondaryIdentifier,
gene.symbol,
gene_chromosome.primaryIdentifier,
gene_chromosomelocation.start,
gene_chromosomelocation.end,
gene_chromosomelocation.strand
ORDER BY gene.symbol ASC
