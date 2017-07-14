MATCH (gene :Gene),
(gene)-[:downstreamIntergenicRegion]-(gene_downstreamintergenicregion :IntergenicRegion),
(gene_downstreamintergenicregion)-[:ADJACENT_TO]-(gene_downstreamintergenicregion_adjacentgenes :Gene),
(gene)-[:upstreamIntergenicRegion]-(gene_upstreamintergenicregion :IntergenicRegion),
(gene_upstreamintergenicregion)-[:ADJACENT_TO]-(gene_upstreamintergenicregion_adjacentgenes :Gene)

WHERE ANY (key in keys(gene) WHERE gene[key]='CG10021') AND gene_downstreamintergenicregion_adjacentgenes != gene AND gene_upstreamintergenicregion_adjacentgenes != gene
RETURN gene.secondaryIdentifier,
gene.symbol,
gene_downstreamintergenicregion_adjacentgenes.primaryIdentifier,
gene_downstreamintergenicregion_adjacentgenes.symbol,
gene_upstreamintergenicregion_adjacentgenes.primaryIdentifier,
gene_upstreamintergenicregion_adjacentgenes.symbol
ORDER BY gene.secondaryIdentifier ASC
