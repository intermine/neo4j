MATCH (gene :Gene),
(gene)-[]-(gene_upstreamintergenicregion :upstreamIntergenicRegion),
(gene_upstreamintergenicregion)-[]-(gene_upstreamintergenicregion_overlappingfeatures :overlappingFeatures),
(gene_upstreamintergenicregion_overlappingfeatures)-[]-(gene_upstreamintergenicregion_overlappingfeatures_chromosomelocation :chromosomeLocation),
(gene_upstreamintergenicregion_overlappingfeatures)-[]-(gene_upstreamintergenicregion_overlappingfeatures_chromosome :chromosome),
(gene_upstreamintergenicregion_overlappingfeatures)-[]-(gene_upstreamintergenicregion_overlappingfeatures_datasets :dataSets)

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
