MATCH (gene :Gene),
(gene)-[:PART_OF]-(gene_organism :Organism),
(gene)-[:transcripts]-(gene_transcripts :Transcript),
(gene_transcripts)-[:introns]-(gene_transcripts_introns :Intron),
(gene_transcripts_introns)-[:chromosomeLocation]-(gene_transcripts_introns_chromosomelocation :Location),
(gene_transcripts_introns)-[:chromosome]-(gene_transcripts_introns_chromosome :Chromosome)

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
