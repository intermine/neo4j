MATCH (gene :Gene),
(gene)-[]-(gene_chromosomelocation :chromosomeLocation),
(gene)-[]-(gene_transcripts :transcripts),
(gene_transcripts)-[]-(gene_transcripts_microarrayoligos :microarrayOligos),
(gene_transcripts_microarrayoligos)-[]-(gene_transcripts_microarrayoligos_sequence :sequence),
(gene)-[]-(gene_chromosome :chromosome)

WHERE gene_chromosome.primaryIdentifier = '2R' AND gene_chromosomelocation.end < 20450439 AND gene_chromosomelocation.start > 19910266
RETURN gene_chromosome.primaryIdentifier,
gene_chromosomelocation.start,
gene_chromosomelocation.end,
gene.secondaryIdentifier,
gene.symbol,
gene_transcripts.primaryIdentifier,
gene_transcripts_microarrayoligos.primaryIdentifier,
gene_transcripts_microarrayoligos_sequence.residues
ORDER BY gene_chromosome.primaryIdentifier ASC
