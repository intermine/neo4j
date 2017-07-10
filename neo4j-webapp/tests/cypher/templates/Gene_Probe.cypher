MATCH (gene :Gene),
(gene)-[]-(gene_organism :organism),
(gene)-[]-(gene_probesets :probeSets),
(gene_probesets)-[]-(gene_probesets_datasets :dataSets),
(gene_probesets_datasets)-[]-(gene_probesets_datasets_datasource :dataSource)

WHERE gene_organism.name = 'Drosophila melanogaster' AND ANY (key in keys(gene) WHERE gene[key]='CG4722') AND gene_probesets_datasets_datasource.name = 'Ensembl'
RETURN gene.secondaryIdentifier,
gene.symbol,
gene_probesets.primaryIdentifier,
gene_probesets_datasets.name
ORDER BY gene.secondaryIdentifier ASC
