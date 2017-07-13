MATCH (gene :Gene),
(gene)-[:upstreamIntergenicRegion]-(gene_upstreamintergenicregion :IntergenicRegion),
(gene_upstreamintergenicregion)-[:OVERLAPS]-(gene_upstreamintergenicregion_overlappingfeatures :SequenceFeature),
(gene_upstreamintergenicregion_overlappingfeatures)-[:chromosomeLocation]-(gene_upstreamintergenicregion_overlappingfeatures_chromosomelocation :Location),
(gene_upstreamintergenicregion_overlappingfeatures)-[:chromosome]-(gene_upstreamintergenicregion_overlappingfeatures_chromosome :Chromosome),
(gene_upstreamintergenicregion_overlappingfeatures)-[:dataSets]-(gene_upstreamintergenicregion_overlappingfeatures_datasets :DataSet)

WHERE ( ANY (key in keys(gene) WHERE gene[key]='zen') AND (gene)-[]-(Organism { shortName: 'D. melanogaster' } ))
RETURN gene.secondaryIdentifier,
gene.symbol,
gene_upstreamintergenicregion.primaryIdentifier,
gene_upstreamintergenicregion_overlappingfeatures.primaryIdentifier,
gene_upstreamintergenicregion_overlappingfeatures_chromosome.primaryIdentifier,
gene_upstreamintergenicregion_overlappingfeatures_chromosomelocation.start,
gene_upstreamintergenicregion_overlappingfeatures_chromosomelocation.end,
gene_upstreamintergenicregion_overlappingfeatures_datasets.name
ORDER BY gene.secondaryIdentifier ASC
