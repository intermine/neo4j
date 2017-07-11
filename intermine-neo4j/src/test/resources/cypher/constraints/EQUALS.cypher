MATCH (gene :Gene),
(gene)-[]-(gene_homologues :homologues),
(gene_homologues)-[]-(gene_homologues_homologue :homologue),
(gene_homologues_homologue)-[]-(gene_homologues_homologue_organism :organism),
(gene)-[]-(gene_pathways :pathways)

WHERE gene_homologues_homologue_organism.taxonId = 123
RETURN gene.symbol,
gene_pathways.identifier

