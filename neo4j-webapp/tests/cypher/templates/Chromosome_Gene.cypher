MATCH (gene :Gene),
(gene)-[]-(gene_organism :organism),
(gene)-[]-(gene_chromosomelocation :chromosomeLocation),
(gene)-[]-(gene_chromosome :chromosome)

WHERE gene_chromosome.primaryIdentifier = '2L' AND gene_organism.name = 'Drosophila melanogaster'
RETURN gene.secondaryIdentifier,
gene.symbol,
gene_chromosome.primaryIdentifier,
gene_chromosomelocation.start,
gene_chromosomelocation.end,
gene_chromosomelocation.strand
ORDER BY gene.secondaryIdentifier ASC
