MATCH (gene :Gene),
(gene)-[:homologues]-(gene_homologues :Homologue),
(gene_homologues)-[:dataSets]-(gene_homologues_datasets :DataSet),
(gene_homologues)-[:PARTNER_OF]-(gene_homologues_homologue :Gene),
(gene_homologues_homologue)-[:PART_OF]-(gene_homologues_homologue_organism :Organism)

WHERE ( ANY (key in keys(gene) WHERE gene[key]='cdk1') AND (gene)-[]-(Organism { shortName: 'D. melanogaster' } )) AND gene_homologues_homologue_organism.shortName = 'C. elegans' AND gene_homologues_datasets.name = 'Panther data set'
RETURN gene.primaryIdentifier,
gene.symbol,
gene_homologues_homologue.primaryIdentifier,
gene_homologues_homologue.symbol,
gene_homologues_homologue_organism.shortName,
gene_homologues_datasets.name,
gene_homologues.type
ORDER BY gene.symbol ASC
