MATCH (gene :Gene),
(gene)-[]-(gene_organism :organism),
(gene)-[]-(gene_exons :exons),
(gene_exons)-[]-(gene_exons_chromosomelocation :chromosomeLocation),
(gene_exons)-[]-(gene_exons_chromosome :chromosome)

WHERE ANY (key in keys(gene) WHERE gene[key]='CG1046') AND gene_organism.name = 'Drosophila melanogaster'
RETURN gene.secondaryIdentifier,
gene.symbol,
gene_exons.primaryIdentifier,
gene_exons.length,
gene_exons_chromosome.primaryIdentifier,
gene_exons_chromosomelocation.start,
gene_exons_chromosomelocation.end,
gene_exons_chromosomelocation.strand
ORDER BY gene.secondaryIdentifier ASC
