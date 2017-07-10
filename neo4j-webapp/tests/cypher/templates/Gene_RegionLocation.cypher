MATCH (gene :Gene),
(gene)-[]-(gene_organism :organism),
(gene)-[]-(gene_regulatoryregions :regulatoryRegions),
(gene_regulatoryregions)-[]-(gene_regulatoryregions_chromosomelocation :chromosomeLocation),
(gene_regulatoryregions)-[]-(gene_regulatoryregions_chromosome :chromosome)

WHERE gene_organism.name = 'Drosophila melanogaster' AND ( ANY (key in keys(gene) WHERE gene[key]='CG1454') AND (gene)-[]-(Organism { shortName: 'D. melanogaster' } ))
RETURN gene.secondaryIdentifier,
gene.symbol,
gene_regulatoryregions.primaryIdentifier,
gene_regulatoryregions_chromosome.primaryIdentifier,
gene_regulatoryregions_chromosomelocation.start,
gene_regulatoryregions_chromosomelocation.end
ORDER BY gene.secondaryIdentifier ASC
