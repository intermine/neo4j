MATCH (gene :Gene),
(gene)-[]-(gene_chromosomelocation :chromosomeLocation),
(gene)-[]-(gene_chromosome :chromosome),
(gene)-[]-(gene_downstreamintergenicregion :downstreamIntergenicRegion),
(gene_downstreamintergenicregion)-[]-(gene_downstreamintergenicregion_adjacentgenes :adjacentGenes),
(gene_downstreamintergenicregion_adjacentgenes)-[]-(gene_downstreamintergenicregion_adjacentgenes_chromosomelocation :chromosomeLocation),
(gene)-[]-(gene_upstreamintergenicregion :upstreamIntergenicRegion),
(gene_upstreamintergenicregion)-[]-(gene_upstreamintergenicregion_chromosomelocation :chromosomeLocation),
(gene_upstreamintergenicregion)-[]-(gene_upstreamintergenicregion_adjacentgenes :adjacentGenes)

WHERE ANY (key in keys(gene) WHERE gene[key]='CG10021') AND gene_downstreamintergenicregion_adjacentgenes != gene AND gene_upstreamintergenicregion_adjacentgenes != gene
RETURN gene.secondaryIdentifier,
gene.symbol,
gene_chromosome.primaryIdentifier,
gene_chromosomelocation.start,
gene_chromosomelocation.end,
gene_chromosomelocation.strand,
gene_downstreamintergenicregion_adjacentgenes.primaryIdentifier,
gene_downstreamintergenicregion_adjacentgenes.symbol,
gene_downstreamintergenicregion_adjacentgenes_chromosomelocation.start,
gene_downstreamintergenicregion_adjacentgenes_chromosomelocation.end,
gene_downstreamintergenicregion_adjacentgenes_chromosomelocation.strand,
gene_upstreamintergenicregion_adjacentgenes.primaryIdentifier,
gene_upstreamintergenicregion_adjacentgenes.symbol,
gene_upstreamintergenicregion_chromosomelocation.start,
gene_upstreamintergenicregion_chromosomelocation.end,
gene_upstreamintergenicregion_chromosomelocation.strand
ORDER BY gene.secondaryIdentifier ASC
