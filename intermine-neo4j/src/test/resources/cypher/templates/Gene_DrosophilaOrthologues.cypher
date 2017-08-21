MATCH (gene :Gene),
(gene)-[:PARTNER_OF]-(gene_homologues :Homologue),
(gene_homologues)-[:PARTNER_OF]-(gene_homologues_homologue :Gene),
(gene_homologues_homologue)-[:PART_OF]-(gene_homologues_homologue_organism :Organism)
WHERE ( ANY (key in keys(gene) WHERE gene[key]='zen') AND (gene)-[]-(Organism { shortName: 'D. melanogaster' } )) AND gene_homologues_homologue_organism.name = '*Drosophila*'
RETURN gene.primaryIdentifier,
gene.secondaryIdentifier,
gene.symbol,
gene_homologues_homologue.primaryIdentifier,
gene_homologues_homologue_organism.name
ORDER BY gene.primaryIdentifier ASC
