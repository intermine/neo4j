MATCH (gene :Gene),
(gene)-[:homologues]-(gene_homologues :Homologue),
(gene_homologues)-[:dataSets]-(gene_homologues_datasets :DataSet),
(gene_homologues)-[:PARTNER_OF]-(gene_homologues_homologue :Gene),
(gene_homologues_homologue)-[:PART_OF]-(gene_homologues_homologue_organism :Organism)

WHERE ( ANY (key in keys(gene) WHERE gene[key]='zen') AND (gene)-[]-(Organism { shortName: 'D. melanogaster' } )) AND gene_homologues_homologue_organism.name = '*Drosophila*'
RETURN gene.primaryIdentifier,
gene.secondaryIdentifier,
gene.symbol,
gene_homologues_homologue.primaryIdentifier,
gene_homologues_homologue.secondaryIdentifier,
gene_homologues_homologue.symbol,
gene_homologues_homologue_organism.name,
gene_homologues.type,
gene_homologues_datasets.name
ORDER BY gene.secondaryIdentifier ASC
