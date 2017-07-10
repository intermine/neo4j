MATCH (gene :Gene),
(gene)-[]-(gene_organism :organism),
(gene)-[]-(gene_microarrayresults :microArrayResults),
(gene_microarrayresults)-[]-(gene_microarrayresults_enrichment :enrichment),
(gene_microarrayresults)-[]-(gene_microarrayresults_affycall :affyCall),
(gene_microarrayresults)-[]-(gene_microarrayresults_material :material),
(gene_microarrayresults)-[]-(gene_microarrayresults_datasets :dataSets),
(gene_microarrayresults)-[]-(gene_microarrayresults_tissue :tissue),
(gene_microarrayresults)-[]-(gene_microarrayresults_presentcall :presentCall)

WHERE gene_organism.taxonId = 7227 AND ANY (key in keys(gene) WHERE gene[key]='CG9652') AND gene_organism.name = 'Drosophila melanogaster' AND gene_microarrayresults_tissue.name = 'Brain'
RETURN gene.secondaryIdentifier,
gene.symbol,
gene_microarrayresults_material.primaryIdentifier,
gene_microarrayresults_datasets.name,
gene_microarrayresults_tissue.name
ORDER BY gene.secondaryIdentifier ASC
