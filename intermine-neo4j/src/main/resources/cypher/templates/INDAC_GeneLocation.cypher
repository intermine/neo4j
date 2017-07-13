MATCH (microarrayoligo :MicroarrayOligo),
(microarrayoligo)-[:transcript]-(microarrayoligo_transcript :Transcript),
(microarrayoligo_transcript)-[:gene]-(microarrayoligo_transcript_gene :Gene),
(microarrayoligo_transcript_gene)-[:chromosomeLocation]-(microarrayoligo_transcript_gene_chromosomelocation :Location),
(microarrayoligo_transcript_gene)-[:chromosome]-(microarrayoligo_transcript_gene_chromosome :Chromosome)

WHERE microarrayoligo.primaryIdentifier = 1000030702
RETURN microarrayoligo.primaryIdentifier,
microarrayoligo_transcript.primaryIdentifier,
microarrayoligo_transcript_gene.secondaryIdentifier,
microarrayoligo_transcript_gene.symbol,
microarrayoligo_transcript_gene_chromosome.primaryIdentifier,
microarrayoligo_transcript_gene_chromosomelocation.start,
microarrayoligo_transcript_gene_chromosomelocation.end
ORDER BY microarrayoligo.primaryIdentifier ASC
