MATCH (gene :Gene),
(gene)-[:PART_OF]-(gene_organism :Organism),
(gene)-[:homologues]-(gene_homologues :Homologue),
(gene_homologues)-[:PARTNER_OF]-(gene_homologues_homologue :Gene)

WHERE gene_organism.shortName = 'D. melanogaster'
RETURN gene_homologues_homologue.primaryIdentifier
ORDER BY gene_homologues_homologue.primaryIdentifier ASC
