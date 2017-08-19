MATCH (gene :Gene),
(gene)-[:PARTNER_OF]-(gene_homologues :Homologue),
(gene_homologues)-[:PARTNER_OF]-(gene_homologues_homologue :Gene),
(gene)-[:PARTICIPATES_IN]-(gene_pathways :Pathway),
(gene_pathways)-[:PARTICIPATES_IN]-(gene_pathways_genes :Gene)
WHERE gene_homologues_homologue = gene_pathways_genes
RETURN gene.symbol,
gene_pathways.identifier

