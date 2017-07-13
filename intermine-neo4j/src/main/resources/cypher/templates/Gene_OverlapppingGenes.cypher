MATCH (gene :Gene),
(gene)-[:chromosomeLocation]-(gene_chromosomelocation :Location),
(gene)-[:chromosome]-(gene_chromosome :Chromosome),
(gene_overlappingfeatures)-[:chromosomeLocation]-(gene_overlappingfeatures_chromosomelocation :Location)
OPTIONAL MATCH (gene)-[:OVERLAPS]-(gene_overlappingfeatures :SequenceFeature)
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
