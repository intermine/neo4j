MATCH (gene :Gene),
(gene)-[:PART_OF]-(gene_organism :Organism),
(gene)-[:gene]-(gene_regulatoryregions :RegulatoryRegion),
(gene_regulatoryregions)-[:chromosome]-(gene_regulatoryregions_chromosome :Chromosome)
WHERE ( ANY (key in keys(gene) WHERE gene[key]='CG6464') AND (gene)-[]-(Organism { shortName: 'D. melanogaster' } )) AND gene_organism.name = 'Drosophila melanogaster'
RETURN gene.primaryIdentifier,
gene.symbol,
gene_regulatoryregions.primaryIdentifier,
gene_regulatoryregions_chromosome.primaryIdentifier,
gene_regulatoryregions_chromosomelocation.start,
gene_regulatoryregions_chromosomelocation.end
ORDER BY gene.primaryIdentifier ASC
