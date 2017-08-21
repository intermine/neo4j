MATCH (gene :Gene),
(gene)-[:OVERLAPS]-(gene_overlappingfeatures :SequenceFeature),
(gene_overlappingfeatures)-[:chromosome]-(gene_overlappingfeatures_chromosome :Chromosome),
(gene_overlappingfeatures)-[:dataSets]-(gene_overlappingfeatures_datasets :DataSet),
(gene_overlappingfeatures_datasets)-[:dataSets]-(gene_overlappingfeatures_datasets_datasource :DataSource)
WHERE ( ANY (key in keys(gene) WHERE gene[key]='CG10021') AND (gene)-[]-(Organism { shortName: 'D. melanogaster' } ))
RETURN gene.primaryIdentifier,
gene.symbol,
gene_overlappingfeatures.primaryIdentifier,
gene_overlappingfeatures_chromosome.primaryIdentifier,
gene_overlappingfeatures_chromosomelocation.start,
gene_overlappingfeatures_chromosomelocation.end,
gene_overlappingfeatures_datasets_datasource.name
ORDER BY gene.primaryIdentifier ASC
