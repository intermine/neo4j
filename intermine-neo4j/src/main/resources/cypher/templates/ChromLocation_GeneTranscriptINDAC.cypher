MATCH (gene :Gene),
(gene)-[:chromosomeLocation]-(gene_chromosomelocation :Location),
(gene)-[:transcripts]-(gene_transcripts :Transcript),
(gene_transcripts)-[:microarrayOligos]-(gene_transcripts_microarrayoligos :MicroarrayOligo),
(gene_transcripts_microarrayoligos)-[:sequence]-(gene_transcripts_microarrayoligos_sequence :Sequence),
(gene)-[:chromosome]-(gene_chromosome :Chromosome)

WHERE gene_chromosome.primaryIdentifier = '2R' AND gene_chromosomelocation.end < 20450439 AND gene_chromosomelocation.start > 19910266
RETURN gene_chromosome.primaryIdentifier,
gene_chromosomelocation.start,
gene_chromosomelocation.end,
gene.primaryIdentifier,
gene.secondaryIdentifier,
gene.symbol,
gene_transcripts.primaryIdentifier,
gene_transcripts_microarrayoligos.primaryIdentifier,
gene_transcripts_microarrayoligos_sequence.residues
ORDER BY gene_chromosome.primaryIdentifier ASC
