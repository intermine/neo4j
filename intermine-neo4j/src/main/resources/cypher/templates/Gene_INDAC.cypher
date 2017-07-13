MATCH (gene :Gene),
(gene)-[:transcripts]-(gene_transcripts :Transcript),
(gene_transcripts)-[:microarrayOligos]-(gene_transcripts_microarrayoligos :MicroarrayOligo)

WHERE ( ANY (key in keys(gene) WHERE gene[key]='CG1046') AND (gene)-[]-(Organism { shortName: 'D. melanogaster' } ))
RETURN gene.secondaryIdentifier,
gene.symbol,
gene_transcripts.primaryIdentifier,
gene_transcripts_microarrayoligos.primaryIdentifier,
gene_transcripts_microarrayoligos.length,
gene_transcripts_microarrayoligos.tm
ORDER BY gene.secondaryIdentifier ASC
