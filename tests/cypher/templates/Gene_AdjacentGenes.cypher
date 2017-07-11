MATCH (gene :Gene),
(gene)-[]-(gene_downstreamintergenicregion :downstreamIntergenicRegion),
(gene_downstreamintergenicregion)-[]-(gene_downstreamintergenicregion_adjacentgenes :adjacentGenes),
(gene)-[]-(gene_upstreamintergenicregion :upstreamIntergenicRegion),
(gene_upstreamintergenicregion)-[]-(gene_upstreamintergenicregion_adjacentgenes :adjacentGenes)

WHERE ANY (key in keys(gene) WHERE gene[key]='CG10021') AND gene_downstreamintergenicregion_adjacentgenes != gene AND gene_upstreamintergenicregion_adjacentgenes != gene
RETURN gene.secondaryIdentifier,
gene.symbol,
gene_downstreamintergenicregion_adjacentgenes.primaryIdentifier,
gene_downstreamintergenicregion_adjacentgenes.symbol,
gene_upstreamintergenicregion_adjacentgenes.primaryIdentifier,
gene_upstreamintergenicregion_adjacentgenes.symbol
ORDER BY gene.secondaryIdentifier ASC
