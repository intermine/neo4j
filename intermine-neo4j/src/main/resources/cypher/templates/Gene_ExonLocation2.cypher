MATCH (gene :Gene),
(gene)-[:PART_OF]-(gene_organism :Organism),
(gene)-[:exons]-(gene_exons :Exon),
(gene_exons)-[:chromosomeLocation]-(gene_exons_chromosomelocation :Location),
(gene_exons)-[:chromosome]-(gene_exons_chromosome :Chromosome)

WHERE ANY (key in keys(gene) WHERE gene[key]='CG1046') AND gene_organism.name = 'Drosophila melanogaster'
RETURN gene.secondaryIdentifier,
gene.symbol,
gene_exons.primaryIdentifier,
gene_exons.length,
gene_exons_chromosome.primaryIdentifier,
gene_exons_chromosomelocation.start,
gene_exons_chromosomelocation.end,
gene_exons_chromosomelocation.strand
ORDER BY gene.secondaryIdentifier ASC
