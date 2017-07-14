MATCH (gene :Gene),
(gene)-[:PART_OF]-(gene_organism :Organism),
(gene)-[:miRNAtargets]-(gene_mirnatargets :MiRNATarget),
(gene_mirnatargets)-[:target]-(gene_mirnatargets_target :MRNA),
(gene_mirnatargets_target)-[:gene]-(gene_mirnatargets_target_gene :Gene)
OPTIONAL MATCH (gene_mirnatargets)-[:dataSets]-(gene_mirnatargets_datasets :DataSet)
WHERE ( ANY (key in keys(gene_mirnatargets_target_gene) WHERE gene_mirnatargets_target_gene[key]='FBgn0026876') AND (gene_mirnatargets_target_gene)-[]-(Organism { shortName: 'D. melanogaster' } )) AND gene_organism.name = 'Drosophila melanogaster'
RETURN gene_mirnatargets_target_gene.primaryIdentifier,
gene_mirnatargets_target_gene.symbol,
gene_mirnatargets_target.primaryIdentifier,
gene.symbol,
gene.primaryIdentifier,
gene_mirnatargets.pvalue,
gene_mirnatargets_datasets.name
ORDER BY gene_mirnatargets_target_gene.primaryIdentifier ASC
