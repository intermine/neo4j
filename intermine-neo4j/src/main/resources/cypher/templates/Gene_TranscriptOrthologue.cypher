MATCH (gene :Gene),
(gene)-[]-(gene_transcripts :transcripts),
(gene)-[]-(gene_homologues :homologues),
(gene_homologues)-[]-(gene_homologues_datasets :dataSets),
(gene_homologues)-[]-(gene_homologues_homologue :homologue),
(gene_homologues_homologue)-[]-(gene_homologues_homologue_organism :organism),
(gene_homologues_homologue)-[]-(gene_homologues_homologue_transcripts :transcripts)

WHERE ( ANY (key in keys(gene) WHERE gene[key]='CG12399') AND (gene)-[]-(Organism { shortName: 'D. melanogaster' } ))
RETURN gene.secondaryIdentifier,
gene.symbol,
gene_transcripts.primaryIdentifier,
gene_homologues_homologue.symbol,
gene_homologues.type,
gene_homologues_homologue_organism.name,
gene_homologues_homologue_transcripts.primaryIdentifier,
gene_homologues_datasets.name
ORDER BY gene.secondaryIdentifier ASC
