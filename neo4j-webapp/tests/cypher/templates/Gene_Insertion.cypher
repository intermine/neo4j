MATCH (gene :Gene),
(gene)-[]-(gene_overlappingfeatures :overlappingFeatures),
(gene_overlappingfeatures)-[]-(gene_overlappingfeatures_chromosomelocation :chromosomeLocation),
(gene_overlappingfeatures)-[]-(gene_overlappingfeatures_datasets :dataSets),
(gene_overlappingfeatures_datasets)-[]-(gene_overlappingfeatures_datasets_datasource :dataSource)

WHERE ( ANY (key in keys(gene) WHERE gene[key]='CG4722') AND (gene)-[]-(Organism { shortName: 'D. melanogaster' } ))
RETURN gene.secondaryIdentifier,
gene.symbol,
gene_overlappingfeatures.primaryIdentifier,
gene_overlappingfeatures.name,
gene_overlappingfeatures_chromosomelocation.start,
gene_overlappingfeatures_chromosomelocation.end,
gene_overlappingfeatures_chromosomelocation.strand,
gene_overlappingfeatures_datasets_datasource.name
ORDER BY gene.secondaryIdentifier ASC
