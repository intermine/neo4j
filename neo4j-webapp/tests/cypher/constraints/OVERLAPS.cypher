MATCH (gene :Gene),
(gene)-[]-(gene_chromosomelocation :chromosomeLocation),
(gene)-[]-(gene_pathways :pathways)

WHERE (gene_chromosomelocation.start <= 45678 AND gene_chromosomelocation.end >= 12345 AND (gene_chromosomelocation)--(:Chromosome {primaryIdentifier:'X'})) OR (gene_chromosomelocation.start <= 45678 AND gene_chromosomelocation.end >= 12345 AND (gene_chromosomelocation)--(:Chromosome {primaryIdentifier:'2L'})) OR (gene_chromosomelocation.start <= 12345 AND gene_chromosomelocation.end >= 12345 AND (gene_chromosomelocation)--(:Chromosome {primaryIdentifier:'3R'}))
RETURN gene.symbol,
gene_pathways.identifier

