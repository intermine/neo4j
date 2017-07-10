MATCH (gene :Gene),
(gene)-[]-(gene_chromosomelocation :chromosomeLocation),
(gene)-[]-(gene_chromosome :chromosome)


RETURN gene.primaryIdentifier,
gene_chromosomelocation.start,
gene_chromosomelocation.end,
gene_chromosomelocation.strand,
gene_chromosome.primaryIdentifier
ORDER BY gene.primaryIdentifier ASC
