MATCH (gene :Gene),
(gene)-[]-(gene_chromosomelocation :chromosomeLocation),
(gene)-[]-(gene_chromosome :chromosome),
(gene_overlappingfeatures)-[]-(gene_overlappingfeatures_chromosomelocation :chromosomeLocation)
OPTIONAL MATCH (gene)-[]-(gene_overlappingfeatures :overlappingFeatures)
WHERE ANY (key in keys(gene) WHERE gene[key]='CG11566')
RETURN gene.secondaryIdentifier,
gene.symbol,
gene_chromosome.primaryIdentifier,
gene_chromosomelocation.start,
gene_chromosomelocation.end,
gene_chromosomelocation.strand,
gene_overlappingfeatures.primaryIdentifier,
gene_overlappingfeatures.symbol,
gene_overlappingfeatures_chromosomelocation.start,
gene_overlappingfeatures_chromosomelocation.end,
gene_overlappingfeatures_chromosomelocation.strand
ORDER BY gene.secondaryIdentifier ASC
