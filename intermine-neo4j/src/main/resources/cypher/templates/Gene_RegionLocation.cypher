MATCH (gene :Gene),
(gene)-[:PART_OF]-(gene_organism :Organism),
(gene)-[:regulatoryRegions]-(gene_regulatoryregions :RegulatoryRegion),
(gene_regulatoryregions)-[:chromosomeLocation]-(gene_regulatoryregions_chromosomelocation :Location),
(gene_regulatoryregions)-[:chromosome]-(gene_regulatoryregions_chromosome :Chromosome)

WHERE gene_organism.name = 'Drosophila melanogaster' AND ( ANY (key in keys(gene) WHERE gene[key]='CG1454') AND (gene)-[]-(Organism { shortName: 'D. melanogaster' } ))
RETURN gene.secondaryIdentifier,
gene.symbol,
gene_regulatoryregions.primaryIdentifier,
gene_regulatoryregions_chromosome.primaryIdentifier,
gene_regulatoryregions_chromosomelocation.start,
gene_regulatoryregions_chromosomelocation.end
ORDER BY gene.secondaryIdentifier ASC
