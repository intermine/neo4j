MATCH (gene :Gene),
(gene)-[]-(gene_pathways :pathways),
(gene_pathways)-[]-(gene_pathways_genes :genes),
(gene_pathways_genes)-[]-(gene_pathways_genes_organism :organism),
(gene_pathways_genes_organism)-[]-(gene_pathways_genes_organism_shortname :shortName),
(gene_pathways)-[]-(gene_pathways_datasets :dataSets)

WHERE ( ANY (key in keys(gene) WHERE gene[key]='N') AND (gene)-[]-(Organism { shortName: 'D. melanogaster' } )) AND ( ANY (key in keys(gene_pathways_genes) WHERE gene_pathways_genes[key]='ser') AND (gene_pathways_genes)-[]-(Organism { shortName: 'D. melanogaster' } ))
RETURN gene.primaryIdentifier,
gene.symbol,
gene.name,
gene_pathways_genes.primaryIdentifier,
gene_pathways_genes.symbol,
gene_pathways_genes.name,
gene_pathways.name,
gene_pathways.identifier,
gene_pathways_datasets.name
ORDER BY gene.symbol ASC
