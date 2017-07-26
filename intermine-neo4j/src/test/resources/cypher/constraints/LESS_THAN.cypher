MATCH (gene :Gene),
(gene)-[:PARTNER_OF]-(gene_homologues :Homologue),
(gene_homologues)-[:PARTNER_OF]-(gene_homologues_homologue :Gene),
(gene_homologues_homologue)-[:PART_OF]-(gene_homologues_homologue_organism :Organism),
(gene)-[:PARTICIPATES_IN]-(gene_pathways :Pathway)

WHERE gene_homologues_homologue_organism.taxonId < 123
RETURN gene.symbol,
gene_pathways.identifier

