MATCH (gene :Gene),
(gene)-[:PART_OF]-(gene_organism :Organism),
(gene)-[:gene]-(gene_exons :Exon),
(gene_exons)-[:OVERLAPS]-(gene_exons_overlappingfeatures :SequenceFeature),
(gene_exons_overlappingfeatures)-[:dataSets]-(gene_exons_overlappingfeatures_datasets :DataSet),
(gene_exons_overlappingfeatures_datasets)-[:dataSets]-(gene_exons_overlappingfeatures_datasets_datasource :DataSource),
(gene)-[:chromosome]-(gene_chromosome :Chromosome)
WHERE ( ANY (key in keys(gene) WHERE gene[key]='CG10011') AND (gene)-[]-(Organism { shortName: 'D. melanogaster' } )) AND gene_exons_overlappingfeatures_datasets.name = 'FlyBase data set for Drosophila melanogaster' AND gene_organism.name = 'Drosophila melanogaster'
RETURN gene.primaryIdentifier,
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
ORDER BY gene.primaryIdentifier ASC
