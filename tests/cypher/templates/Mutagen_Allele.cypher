MATCH (gene :Gene),
(gene)-[]-(gene_organism :organism),
(gene)-[]-(gene_alleles :alleles),
(gene_alleles)-[]-(gene_alleles_datasets :dataSets),
(gene_alleles_datasets)-[]-(gene_alleles_datasets_datasource :dataSource),
(gene_alleles)-[]-(gene_alleles_mutagens :mutagens)

WHERE gene_alleles_mutagens.description = 'P-element activity' AND gene_organism.name = 'Drosophila melanogaster'
RETURN gene_alleles.primaryIdentifier,
gene_alleles.secondaryIdentifier,
gene.secondaryIdentifier,
gene.symbol,
gene_alleles_mutagens.description,
gene_alleles_datasets_datasource.name
ORDER BY gene_alleles.primaryIdentifier ASC
