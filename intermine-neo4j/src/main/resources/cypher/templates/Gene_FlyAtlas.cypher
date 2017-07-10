MATCH (gene :Gene),
(gene)-[]-(gene_organism :organism),
(gene)-[]-(gene_microarrayresults :microArrayResults),
(gene_microarrayresults)-[]-(gene_microarrayresults_enrichment :enrichment),
(gene_microarrayresults)-[]-(gene_microarrayresults_affycall :affyCall),
(gene_microarrayresults)-[]-(gene_microarrayresults_datasets :dataSets),
(gene_microarrayresults)-[]-(gene_microarrayresults_tissue :tissue),
(gene_microarrayresults)-[]-(gene_microarrayresults_mrnasignal :mRNASignal),
(gene_microarrayresults)-[]-(gene_microarrayresults_mrnasignalsem :mRNASignalSEM),
(gene_microarrayresults)-[]-(gene_microarrayresults_presentcall :presentCall)

WHERE ANY (key in keys(gene) WHERE gene[key]='FBgn0011582') AND gene_organism.name = 'Drosophila melanogaster' AND gene_microarrayresults.affyCall = 'Up' AND gene_microarrayresults.enrichment >= 2 AND gene_microarrayresults.mRNASignal >= 100 AND gene_microarrayresults.presentCall >= 3
RETURN gene.secondaryIdentifier,
gene.symbol,
gene_microarrayresults_datasets.name,
gene_microarrayresults_tissue.name
ORDER BY gene.secondaryIdentifier ASC
