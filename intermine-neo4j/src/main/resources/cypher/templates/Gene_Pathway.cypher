MATCH (gene :Gene),
(gene)-[:PARTICIPATES_IN]-(gene_pathways :Pathway),
(gene_pathways)-[:dataSets]-(gene_pathways_datasets :DataSet)

WHERE ( ANY (key in keys(gene) WHERE gene[key]='bsk') AND (gene)-[]-(Organism { shortName: 'D. melanogaster' } ))
RETURN gene.secondaryIdentifier,
gene.symbol,
gene_pathways.identifier,
gene_pathways.name,
gene_pathways_datasets.name
ORDER BY gene.secondaryIdentifier ASC
