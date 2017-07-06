MATCH (gene :Gene),
(gene)-[]-(gene_transcripts :transcripts),
(gene_transcripts)-[]-(gene_transcripts_microarrayoligos :microarrayOligos),
(gene_transcripts_microarrayoligos)-[]-(gene_transcripts_microarrayoligos_tm :tm)

WHERE ( ANY (key in keys(gene) WHERE gene[key]='CG1046') AND (gene)-[]-(Organism { shortName: 'D. melanogaster' } ))
RETURN gene.secondaryIdentifier,
gene.symbol,
gene_transcripts.primaryIdentifier,
gene_transcripts_microarrayoligos.primaryIdentifier,
gene_transcripts_microarrayoligos.length
ORDER BY gene.secondaryIdentifier ASC
