MATCH (gene :Gene),
(gene)-[]-(gene_organism :organism),
(gene)-[]-(gene_utrs :UTRs),
(gene_utrs)-[]-(gene_utrs_chromosomelocation :chromosomeLocation)

WHERE gene_organism.name = 'Drosophila melanogaster'
RETURN gene_utrs.primaryIdentifier,
gene_utrs.length,
gene_utrs_chromosomelocation.start,
gene_utrs_chromosomelocation.end,
gene.secondaryIdentifier
ORDER BY gene_utrs.primaryIdentifier ASC
