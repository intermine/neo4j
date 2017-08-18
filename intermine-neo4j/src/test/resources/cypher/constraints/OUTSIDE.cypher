MATCH (gene :Gene),
(gene)-[gene_chromosomelocation:LOCATED_ON]-()

WHERE (gene_chromosomelocation.start <= 14619002 AND gene_chromosomelocation.end >= 14615435 AND ()-[gene_chromosomelocation]-(:Chromosome {primaryIdentifier:'2L'}))
RETURN gene.primaryIdentifier
ORDER BY gene.primaryIdentifier ASC
