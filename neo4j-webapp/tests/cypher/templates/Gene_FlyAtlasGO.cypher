MATCH (gene :Gene),
(gene)-[]-(gene_goannotation :goAnnotation),
(gene_goannotation)-[]-(gene_goannotation_datasets :dataSets),
(gene_goannotation)-[]-(gene_goannotation_ontologyterm :ontologyTerm),
(gene)-[]-(gene_organism :organism),
(gene)-[]-(gene_microarrayresults :microArrayResults),
(gene_microarrayresults)-[]-(gene_microarrayresults_enrichment :enrichment),
(gene_microarrayresults)-[]-(gene_microarrayresults_affycall :affyCall),
(gene_microarrayresults)-[]-(gene_microarrayresults_material :material),
(gene_microarrayresults)-[]-(gene_microarrayresults_datasets :dataSets),
(gene_microarrayresults)-[]-(gene_microarrayresults_tissue :tissue),
(gene_microarrayresults)-[]-(gene_microarrayresults_presentcall :presentCall)

WHERE ANY (key in keys(gene) WHERE gene[key]='CG9652') AND gene_organism.taxonId = 7227 AND gene_organism.name = 'Drosophila melanogaster'
RETURN gene.secondaryIdentifier,
gene.symbol,
gene_microarrayresults_tissue.name,
gene_microarrayresults_material.primaryIdentifier,
gene_microarrayresults_datasets.name,
gene_goannotation_ontologyterm.identifier,
gene_goannotation_ontologyterm.name,
gene_goannotation_datasets.name,
gene_microarrayresults_tissue.name
ORDER BY gene.secondaryIdentifier ASC
