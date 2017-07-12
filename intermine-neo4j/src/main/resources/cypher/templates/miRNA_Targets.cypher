MATCH (gene :Gene),
(gene)-[]-(gene_organism :organism),
(gene)-[]-(gene_mirnatargets :miRNAtargets),
(gene_mirnatargets)-[]-(gene_mirnatargets_target :target),
(gene_mirnatargets_target)-[]-(gene_mirnatargets_target_gene :gene)
OPTIONAL MATCH (gene_mirnatargets)-[]-(gene_mirnatargets_datasets :dataSets)
WHERE ANY (key in keys(gene) WHERE gene[key]='mir-79') AND gene_organism.name = 'Drosophila melanogaster'
RETURN gene.secondaryIdentifier,
gene.symbol,
gene_mirnatargets_target.primaryIdentifier,
gene_mirnatargets_target_gene.secondaryIdentifier,
gene_mirnatargets_target.symbol,
gene_mirnatargets.pvalue,
gene_mirnatargets_datasets.name
ORDER BY gene_mirnatargets.pvalue ASC
