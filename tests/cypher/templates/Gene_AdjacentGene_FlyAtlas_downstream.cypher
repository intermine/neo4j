MATCH (gene :Gene),
(gene)-[]-(gene_downstreamintergenicregion :downstreamIntergenicRegion),
(gene_downstreamintergenicregion)-[]-(gene_downstreamintergenicregion_adjacentgenes :adjacentGenes),
(gene_downstreamintergenicregion_adjacentgenes)-[]-(gene_downstreamintergenicregion_adjacentgenes_microarrayresults :microArrayResults),
(gene_downstreamintergenicregion_adjacentgenes_microarrayresults)-[]-(gene_downstreamintergenicregion_adjacentgenes_microarrayresults_enrichment :enrichment),
(gene_downstreamintergenicregion_adjacentgenes_microarrayresults)-[]-(gene_downstreamintergenicregion_adjacentgenes_microarrayresults_affycall :affyCall),
(gene_downstreamintergenicregion_adjacentgenes_microarrayresults)-[]-(gene_downstreamintergenicregion_adjacentgenes_microarrayresults_tissue :tissue),
(gene_downstreamintergenicregion_adjacentgenes_microarrayresults)-[]-(gene_downstreamintergenicregion_adjacentgenes_microarrayresults_mrnasignal :mRNASignal)

WHERE ( ANY (key in keys(gene) WHERE gene[key]='CG10267') AND (gene)-[]-(Organism { shortName: 'D. melanogaster' } )) AND gene_downstreamintergenicregion_adjacentgenes_microarrayresults.mRNASignal >= 100.0 AND gene_downstreamintergenicregion_adjacentgenes_microarrayresults.enrichment >= 2.0 AND gene_downstreamintergenicregion_adjacentgenes_microarrayresults.affyCall = 'Up' AND gene_downstreamintergenicregion_adjacentgenes != gene
RETURN gene.secondaryIdentifier,
gene_downstreamintergenicregion_adjacentgenes.secondaryIdentifier,
gene_downstreamintergenicregion_adjacentgenes_microarrayresults_tissue.name
ORDER BY gene.secondaryIdentifier ASC
