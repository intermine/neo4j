MATCH (gene :Gene),
(gene)-[]-(gene_upstreamintergenicregion :upstreamIntergenicRegion),
(gene_upstreamintergenicregion)-[]-(gene_upstreamintergenicregion_adjacentgenes :adjacentGenes),
(gene_upstreamintergenicregion_adjacentgenes)-[]-(gene_upstreamintergenicregion_adjacentgenes_microarrayresults :microArrayResults),
(gene_upstreamintergenicregion_adjacentgenes_microarrayresults)-[]-(gene_upstreamintergenicregion_adjacentgenes_microarrayresults_enrichment :enrichment),
(gene_upstreamintergenicregion_adjacentgenes_microarrayresults)-[]-(gene_upstreamintergenicregion_adjacentgenes_microarrayresults_affycall :affyCall),
(gene_upstreamintergenicregion_adjacentgenes_microarrayresults)-[]-(gene_upstreamintergenicregion_adjacentgenes_microarrayresults_tissue :tissue),
(gene_upstreamintergenicregion_adjacentgenes_microarrayresults)-[]-(gene_upstreamintergenicregion_adjacentgenes_microarrayresults_mrnasignal :mRNASignal)

WHERE ANY (key in keys(gene) WHERE gene[key]='CG10009') AND gene_upstreamintergenicregion_adjacentgenes_microarrayresults.mRNASignal >= 100.0 AND gene_upstreamintergenicregion_adjacentgenes_microarrayresults.enrichment >= 2.0 AND gene_upstreamintergenicregion_adjacentgenes_microarrayresults.affyCall = 'Up'
RETURN gene.secondaryIdentifier,
gene_upstreamintergenicregion_adjacentgenes.secondaryIdentifier,
gene_upstreamintergenicregion_adjacentgenes_microarrayresults_tissue.name
ORDER BY gene.secondaryIdentifier ASC
