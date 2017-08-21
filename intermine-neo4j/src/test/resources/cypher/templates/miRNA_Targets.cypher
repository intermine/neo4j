MATCH (gene :Gene),
(gene)-[:PART_OF]-(gene_organism :Organism),
(gene)-[:mirnagene]-(gene_mirnatargets :MiRNATarget),
(gene_mirnatargets)-[:target]-(gene_mirnatargets_target :MRNA),
(gene_mirnatargets_target)-[:gene]-(gene_mirnatargets_target_gene :Gene)
OPTIONAL MATCH (gene_mirnatargets)-[:dataSets]-(gene_mirnatargets_datasets :DataSet)
WHERE ANY (key in keys(gene) WHERE gene[key]='mir-79') AND gene_organism.name = 'Drosophila melanogaster'
RETURN gene.primaryIdentifier,
gene.symbol,
gene_mirnatargets_target.primaryIdentifier,
gene_mirnatargets_target_gene.primaryIdentifier,
gene_mirnatargets_target.symbol,
gene_mirnatargets.pvalue,
gene_mirnatargets_datasets.name
ORDER BY gene_mirnatargets.pvalue ASC
