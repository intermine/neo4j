MATCH (gene :Gene),
(gene)-[:PART_OF]-(gene_organism :Organism),
(gene)-[:chromosome]-(gene_chromosome :Chromosome),
(gene)-[gene_locations:LOCATED_ON]-(gene_locations_locatedon :BioEntity)
WHERE ANY (key in keys(gene) WHERE gene[key]='CG1046') AND gene_organism.name = 'Drosophila melanogaster'
RETURN gene.primaryIdentifier,
gene.secondaryIdentifier,
gene.symbol,
gene.name,
gene_chromosome.primaryIdentifier,
gene_locations.start,
gene_locations.end,
gene_locations.strand
ORDER BY gene.primaryIdentifier ASC
