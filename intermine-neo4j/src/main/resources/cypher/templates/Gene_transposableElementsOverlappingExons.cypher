MATCH (gene :Gene),
(gene)-[]-(gene_organism :organism),
(gene)-[]-(gene_chromosomelocation :chromosomeLocation),
(gene)-[]-(gene_exons :exons),
(gene_exons)-[]-(gene_exons_overlappingfeatures :overlappingFeatures),
(gene_exons_overlappingfeatures)-[]-(gene_exons_overlappingfeatures_chromosomelocation :chromosomeLocation),
(gene_exons_overlappingfeatures)-[]-(gene_exons_overlappingfeatures_datasets :dataSets),
(gene_exons_overlappingfeatures_datasets)-[]-(gene_exons_overlappingfeatures_datasets_datasource :dataSource),
(gene)-[]-(gene_chromosome :chromosome)

WHERE ( ANY (key in keys(gene) WHERE gene[key]='CG10011') AND (gene)-[]-(Organism { shortName: 'D. melanogaster' } )) AND gene_exons_overlappingfeatures_datasets.name = 'FlyBase data set for Drosophila melanogaster' AND gene_organism.name = 'Drosophila melanogaster'
RETURN gene.secondaryIdentifier,
gene.symbol,
gene_chromosome.primaryIdentifier,
gene_chromosomelocation.start,
gene_chromosomelocation.end,
gene_exons.primaryIdentifier,
gene_exons_overlappingfeatures.primaryIdentifier,
gene_exons_overlappingfeatures.symbol,
gene_exons_overlappingfeatures_chromosomelocation.start,
gene_exons_overlappingfeatures_chromosomelocation.end,
gene_exons_overlappingfeatures_chromosomelocation.strand,
gene_exons_overlappingfeatures_datasets_datasource.name
ORDER BY gene.secondaryIdentifier ASC
