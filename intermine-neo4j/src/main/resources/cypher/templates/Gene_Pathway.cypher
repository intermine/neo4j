MATCH (gene :Gene),
(gene)-[]-(gene_pathways :pathways),
(gene_pathways)-[]-(gene_pathways_datasets :dataSets)

WHERE ( ANY (key in keys(gene) WHERE gene[key]='bsk') AND (gene)-[]-(Organism { shortName: 'D. melanogaster' } ))
RETURN gene.secondaryIdentifier,
gene.symbol,
gene_pathways.identifier,
gene_pathways.name,
gene_pathways_datasets.name
ORDER BY gene.secondaryIdentifier ASC
