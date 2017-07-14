MATCH (gene :Gene),
(gene)-[:PART_OF]-(gene_organism :Organism),
(gene)-[:alleles]-(gene_alleles :Allele),
(gene_alleles)-[:dataSets]-(gene_alleles_datasets :DataSet),
(gene_alleles_datasets)-[:dataSource]-(gene_alleles_datasets_datasource :DataSource),
(gene_alleles)-[:mutagens]-(gene_alleles_mutagens :Mutagen)

WHERE gene_alleles_mutagens.description = 'P-element activity' AND gene_organism.name = 'Drosophila melanogaster'
RETURN gene_alleles.primaryIdentifier,
gene_alleles.secondaryIdentifier,
gene.secondaryIdentifier,
gene.symbol,
gene_alleles_mutagens.description,
gene_alleles_datasets_datasource.name
ORDER BY gene_alleles.primaryIdentifier ASC
