MATCH (gene :Gene),
(gene)-[]-(gene_organism :organism),
(gene)-[]-(gene_chromosomelocation :chromosomeLocation),
(gene)-[]-(gene_chromosome :chromosome)

WHERE gene_organism.name = 'Drosophila melanogaster'
RETURN gene_chromosome.primaryIdentifier,
gene_chromosomelocation.strand,
gene_chromosomelocation.start,
gene_chromosomelocation.end,
gene.length,
gene.symbol,
gene.secondaryIdentifier
ORDER BY gene_chromosome.primaryIdentifier ASC
