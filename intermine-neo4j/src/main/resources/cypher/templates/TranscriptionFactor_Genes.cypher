MATCH (gene :Gene),
(gene)-[]-(gene_chromosomelocation :chromosomeLocation),
(gene)-[]-(gene_regulatoryregions :regulatoryRegions),
(gene_regulatoryregions)-[]-(gene_regulatoryregions_factor :factor),
(gene)-[]-(gene_chromosome :chromosome)

WHERE ANY (key in keys(gene_regulatoryregions_factor) WHERE gene_regulatoryregions_factor[key]='CG2328')
RETURN gene_regulatoryregions_factor.secondaryIdentifier,
gene_regulatoryregions_factor.symbol,
gene.secondaryIdentifier,
gene.symbol,
gene_chromosome.primaryIdentifier,
gene_chromosomelocation.start,
gene_chromosomelocation.end,
gene_chromosomelocation.strand
ORDER BY gene_regulatoryregions_factor.secondaryIdentifier ASC
