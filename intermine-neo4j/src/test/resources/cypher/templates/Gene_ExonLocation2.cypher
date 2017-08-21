MATCH (gene :Gene),
(gene)-[:gene]-(gene_exons :Exon),
(gene_exons)-[:chromosome]-(gene_exons_chromosome :Chromosome)
WHERE ANY (key in keys(gene) WHERE gene[key]='CG1046')
RETURN gene.primaryIdentifier,
gene.symbol,
gene_exons.primaryIdentifier,
gene_exons.length,
gene_exons_chromosome.primaryIdentifier,
gene_exons_chromosomelocation.start,
gene_exons_chromosomelocation.end,
gene_exons_chromosomelocation.strand
ORDER BY gene.primaryIdentifier ASC
