MATCH (gene :Gene),
(gene)-[:chromosomeLocation]-(gene_chromosomelocation :Location),
(gene)-[:chromosome]-(gene_chromosome :Chromosome),
(gene)-[:OVERLAPS]-(gene_overlappingfeatures :SequenceFeature),
(gene_overlappingfeatures)-[:chromosomeLocation]-(gene_overlappingfeatures_chromosomelocation :Location),
(gene_overlappingfeatures)-[:dataSets]-(gene_overlappingfeatures_datasets :DataSet),
(gene_overlappingfeatures_datasets)-[:dataSource]-(gene_overlappingfeatures_datasets_datasource :DataSource)

WHERE gene_chromosome.primaryIdentifier = '2L'
RETURN gene_chromosome.primaryIdentifier,
gene.primaryIdentifier,
gene.symbol,
gene_chromosomelocation.start,
gene_chromosomelocation.end,
gene_overlappingfeatures.primaryIdentifier,
gene_overlappingfeatures.symbol,
gene_overlappingfeatures_chromosomelocation.start,
gene_overlappingfeatures_chromosomelocation.end,
gene_overlappingfeatures_datasets_datasource.name
ORDER BY gene_chromosome.primaryIdentifier ASC
