MATCH (gene :Gene),
(gene)-[]-(gene_organism :organism),
(gene)-[]-(gene_mirnatargets :miRNAtargets),
(gene_mirnatargets)-[]-(gene_mirnatargets_target :target),
(gene_mirnatargets_target)-[]-(gene_mirnatargets_target_gene :gene)
OPTIONAL MATCH (gene_mirnatargets)-[]-(gene_mirnatargets_datasets :dataSets)
WHERE ( ANY (key in keys(gene_mirnatargets_target_gene) WHERE gene_mirnatargets_target_gene[key]='FBgn0026876') AND (gene_mirnatargets_target_gene)-[]-(Organism { shortName: 'D. melanogaster' } )) AND gene_organism.name = 'Drosophila melanogaster'
RETURN gene_mirnatargets_target_gene.secondaryIdentifier,
gene_mirnatargets_target_gene.symbol,
gene_mirnatargets_target.primaryIdentifier,
gene.symbol,
gene_mirnatargets.pvalue,
gene_mirnatargets_datasets.name
ORDER BY gene_mirnatargets_target_gene.secondaryIdentifier ASC
