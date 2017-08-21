MATCH (gene :Gene),
(gene)-[:gene]-(gene_transcripts :Transcript),
(gene_transcripts)-[:chromosome]-(gene_transcripts_chromosome :Chromosome)
OPTIONAL MATCH (gene_transcripts)-[:exons]-(gene_transcripts_exons :Exon),
(gene_transcripts)-[:introns]-(gene_transcripts_introns :Intron)
WHERE ANY (key in keys(gene) WHERE gene[key]='CG10016')
RETURN gene.primaryIdentifier,
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
ORDER BY gene.primaryIdentifier ASC
