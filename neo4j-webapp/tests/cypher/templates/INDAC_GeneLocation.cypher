MATCH (microarrayoligo :MicroarrayOligo),
(microarrayoligo)-[]-(microarrayoligo_transcript :transcript),
(microarrayoligo_transcript)-[]-(microarrayoligo_transcript_gene :gene),
(microarrayoligo_transcript_gene)-[]-(microarrayoligo_transcript_gene_chromosomelocation :chromosomeLocation),
(microarrayoligo_transcript_gene)-[]-(microarrayoligo_transcript_gene_chromosome :chromosome)

WHERE microarrayoligo.primaryIdentifier = 1000030702
RETURN microarrayoligo.primaryIdentifier,
microarrayoligo_transcript.primaryIdentifier,
microarrayoligo_transcript_gene.secondaryIdentifier,
microarrayoligo_transcript_gene.symbol,
microarrayoligo_transcript_gene_chromosome.primaryIdentifier,
microarrayoligo_transcript_gene_chromosomelocation.start,
microarrayoligo_transcript_gene_chromosomelocation.end
ORDER BY microarrayoligo.primaryIdentifier ASC
