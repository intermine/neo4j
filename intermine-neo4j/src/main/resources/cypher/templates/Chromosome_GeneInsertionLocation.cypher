MATCH (gene :Gene),
(gene)-[]-(gene_chromosomelocation :chromosomeLocation),
(gene)-[]-(gene_chromosome :chromosome),
(gene)-[]-(gene_overlappingfeatures :overlappingFeatures),
(gene_overlappingfeatures)-[]-(gene_overlappingfeatures_chromosomelocation :chromosomeLocation),
(gene_overlappingfeatures)-[]-(gene_overlappingfeatures_datasets :dataSets),
(gene_overlappingfeatures_datasets)-[]-(gene_overlappingfeatures_datasets_datasource :dataSource)

WHERE gene_chromosome.primaryIdentifier = '2L'
RETURN gene_chromosome.primaryIdentifier,
gene.secondaryIdentifier,
gene.symbol,
gene_chromosomelocation.start,
gene_chromosomelocation.end,
gene_overlappingfeatures.primaryIdentifier,
gene_overlappingfeatures.symbol,
gene_overlappingfeatures_chromosomelocation.start,
gene_overlappingfeatures_chromosomelocation.end,
gene_overlappingfeatures_datasets_datasource.name
ORDER BY gene_chromosome.primaryIdentifier ASC
