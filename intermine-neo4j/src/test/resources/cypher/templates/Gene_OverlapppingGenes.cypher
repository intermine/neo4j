MATCH (gene :Gene),
(gene)-[:chromosome]-(gene_chromosome :Chromosome)
OPTIONAL MATCH (gene)-[:OVERLAPS]-(gene_overlappingfeatures :SequenceFeature)
WHERE ANY (key in keys(gene) WHERE gene[key]='CG11566')
RETURN gene.primaryIdentifier,
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
ORDER BY gene.primaryIdentifier ASC
