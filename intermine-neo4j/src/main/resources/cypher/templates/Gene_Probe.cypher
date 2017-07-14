MATCH (gene :Gene),
(gene)-[:PART_OF]-(gene_organism :Organism),
(gene)-[:probeSets]-(gene_probesets :ProbeSet),
(gene_probesets)-[:dataSets]-(gene_probesets_datasets :DataSet),
(gene_probesets_datasets)-[:dataSource]-(gene_probesets_datasets_datasource :DataSource)

WHERE gene_organism.name = 'Drosophila melanogaster' AND ANY (key in keys(gene) WHERE gene[key]='CG4722') AND gene_probesets_datasets_datasource.name = 'Ensembl'
RETURN gene.primaryIdentifier,
gene.symbol,
gene_probesets.primaryIdentifier,
gene_probesets_datasets.name
ORDER BY gene.primaryIdentifier ASC
