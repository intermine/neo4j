MATCH (gene :Gene),
(gene)-[:PARTICIPATES_IN]-(gene_pathways :Pathway),
(gene_pathways)-[:genes]-(gene_pathways_genes :Gene),
(gene_pathways_genes)-[:PART_OF]-(gene_pathways_genes_organism :Organism),
(gene_pathways)-[:dataSets]-(gene_pathways_datasets :DataSet)

WHERE ( ANY (key in keys(gene) WHERE gene[key]='N') AND (gene)-[]-(Organism { shortName: 'D. melanogaster' } )) AND ( ANY (key in keys(gene_pathways_genes) WHERE gene_pathways_genes[key]='ser') AND (gene_pathways_genes)-[]-(Organism { shortName: 'D. melanogaster' } ))
RETURN gene.primaryIdentifier,
gene.symbol,
gene.name,
gene_pathways_genes.primaryIdentifier,
gene_pathways_genes.symbol,
gene_pathways_genes.name,
gene_pathways.name,
gene_pathways.identifier,
gene_pathways_datasets.name,
gene_pathways_genes_organism.shortName
ORDER BY gene.symbol ASC
