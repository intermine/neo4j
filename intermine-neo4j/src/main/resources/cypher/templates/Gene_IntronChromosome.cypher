MATCH (gene :Gene),
(gene)-[]-(gene_organism :organism),
(gene)-[]-(gene_transcripts :transcripts),
(gene_transcripts)-[]-(gene_transcripts_introns :introns),
(gene_transcripts_introns)-[]-(gene_transcripts_introns_chromosomelocation :chromosomeLocation),
(gene_transcripts_introns)-[]-(gene_transcripts_introns_chromosome :chromosome)

WHERE ANY (key in keys(gene) WHERE gene[key]='CG10016') AND gene_organism.name = 'Drosophila melanogaster'
RETURN gene.secondaryIdentifier,
gene.symbol,
gene_transcripts_introns.primaryIdentifier,
gene_transcripts_introns.length,
gene_transcripts_introns_chromosome.primaryIdentifier,
gene_transcripts_introns_chromosomelocation.start,
gene_transcripts_introns_chromosomelocation.end,
gene_transcripts_introns_chromosomelocation.strand
ORDER BY gene.secondaryIdentifier ASC
