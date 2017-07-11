MATCH (gene :Gene),
(gene)-[]-(gene_homologues :homologues),
(gene_homologues)-[]-(gene_homologues_datasets :dataSets),
(gene_homologues)-[]-(gene_homologues_homologue :homologue),
(gene_homologues_homologue)-[]-(gene_homologues_homologue_organism :organism),
(gene_homologues_homologue_organism)-[]-(gene_homologues_homologue_organism_shortname :shortName)

WHERE ( ANY (key in keys(gene) WHERE gene[key]='cdk1') AND (gene)-[]-(Organism { shortName: 'D. melanogaster' } )) AND gene_homologues_homologue_organism.shortName = 'C. elegans' AND gene_homologues_datasets.name = 'Panther data set'
RETURN gene.primaryIdentifier,
gene.symbol,
gene_homologues_homologue.primaryIdentifier,
gene_homologues_homologue.symbol,
gene_homologues_datasets.name,
gene_homologues.type
ORDER BY gene.symbol ASC
