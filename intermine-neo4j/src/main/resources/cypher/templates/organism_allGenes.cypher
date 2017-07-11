MATCH (gene :Gene),
(gene)-[]-(gene_organism :organism),
(gene)-[]-(gene_chromosomelocation :chromosomeLocation),
(gene)-[]-(gene_chromosome :chromosome)

WHERE gene_organism.name = 'Drosophila melanogaster'
RETURN gene.primaryIdentifier,
gene.secondaryIdentifier,
gene.symbol,
gene_chromosome.primaryIdentifier,
gene_chromosomelocation.start,
gene_chromosomelocation.end,
gene_chromosomelocation.strand
ORDER BY gene.symbol ASC
