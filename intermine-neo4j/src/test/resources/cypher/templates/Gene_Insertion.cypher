MATCH (gene :Gene),
(gene)-[:OVERLAPS]-(gene_overlappingfeatures :SequenceFeature),
(gene_overlappingfeatures)-[:dataSets]-(gene_overlappingfeatures_datasets :DataSet),
(gene_overlappingfeatures_datasets)-[:dataSets]-(gene_overlappingfeatures_datasets_datasource :DataSource)
WHERE ( ANY (key in keys(gene) WHERE gene[key]='CG4722') AND (gene)-[]-(Organism { shortName: 'D. melanogaster' } ))
RETURN gene.primaryIdentifier,
gene.symbol,
gene_overlappingfeatures.primaryIdentifier,
gene_overlappingfeatures.name,
gene_overlappingfeatures_chromosomelocation.start,
gene_overlappingfeatures_chromosomelocation.end,
gene_overlappingfeatures_chromosomelocation.strand,
gene_overlappingfeatures_datasets_datasource.name
ORDER BY gene.primaryIdentifier ASC
