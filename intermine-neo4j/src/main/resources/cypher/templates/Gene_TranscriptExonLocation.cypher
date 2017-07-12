MATCH (gene :Gene),
(gene)-[]-(gene_transcripts :transcripts),
(gene_transcripts)-[]-(gene_transcripts_chromosomelocation :chromosomeLocation),
(gene_transcripts_exons)-[]-(gene_transcripts_exons_chromosomelocation :chromosomeLocation),
(gene_transcripts)-[]-(gene_transcripts_chromosome :chromosome),
(gene_transcripts_introns)-[]-(gene_transcripts_introns_chromosomelocation :chromosomeLocation)
OPTIONAL MATCH (gene_transcripts)-[]-(gene_transcripts_exons :exons),
(gene_transcripts)-[]-(gene_transcripts_introns :introns)
WHERE ANY (key in keys(gene) WHERE gene[key]='CG10016')
RETURN gene.secondaryIdentifier,
gene.symbol,
gene_transcripts_chromosome.primaryIdentifier,
gene_transcripts.primaryIdentifier,
gene_transcripts_chromosomelocation.start,
gene_transcripts_chromosomelocation.end,
gene_transcripts.length,
gene_transcripts_exons.primaryIdentifier,
gene_transcripts_exons_chromosomelocation.start,
gene_transcripts_exons_chromosomelocation.end,
gene_transcripts_exons.length,
gene_transcripts_introns.primaryIdentifier,
gene_transcripts_introns_chromosomelocation.start,
gene_transcripts_introns_chromosomelocation.end,
gene_transcripts_introns.length
ORDER BY gene.secondaryIdentifier ASC
