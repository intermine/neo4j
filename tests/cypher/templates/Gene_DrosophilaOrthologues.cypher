MATCH (gene :Gene),
(gene)-[]-(gene_homologues :homologues),
(gene_homologues)-[]-(gene_homologues_datasets :dataSets),
(gene_homologues)-[]-(gene_homologues_homologue :homologue),
(gene_homologues_homologue)-[]-(gene_homologues_homologue_organism :organism)

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
