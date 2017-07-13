MATCH (gene :Gene),
(gene)-[:chromosomeLocation]-(gene_chromosomelocation :Location),
(gene)-[:chromosome]-(gene_chromosome :Chromosome),
(gene)-[:downstreamIntergenicRegion]-(gene_downstreamintergenicregion :IntergenicRegion),
(gene_downstreamintergenicregion)-[:ADJACENT_TO]-(gene_downstreamintergenicregion_adjacentgenes :Gene),
(gene_downstreamintergenicregion_adjacentgenes)-[:chromosomeLocation]-(gene_downstreamintergenicregion_adjacentgenes_chromosomelocation :Location),
(gene)-[:upstreamIntergenicRegion]-(gene_upstreamintergenicregion :IntergenicRegion),
(gene_upstreamintergenicregion)-[:chromosomeLocation]-(gene_upstreamintergenicregion_chromosomelocation :Location),
(gene_upstreamintergenicregion)-[:ADJACENT_TO]-(gene_upstreamintergenicregion_adjacentgenes :Gene)

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
