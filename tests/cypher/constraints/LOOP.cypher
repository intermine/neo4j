MATCH (gene :Gene),
(gene)-[]-(gene_homologues :homologues),
(gene_homologues)-[]-(gene_homologues_homologue :homologue),
(gene)-[]-(gene_pathways :pathways),
(gene_pathways)-[]-(gene_pathways_genes :genes)

WHERE gene_homologues_homologue = gene_pathways_genes
RETURN gene.symbol,
gene_pathways.identifier

