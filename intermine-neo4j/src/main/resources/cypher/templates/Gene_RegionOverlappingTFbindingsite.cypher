MATCH (gene :Gene),
(gene)-[:PART_OF]-(gene_organism :Organism),
(gene)-[:regulatoryRegions]-(gene_regulatoryregions :RegulatoryRegion),
(gene_regulatoryregions)-[:chromosomeLocation]-(gene_regulatoryregions_chromosomelocation :Location),
(gene_regulatoryregions)-[:chromosome]-(gene_regulatoryregions_chromosome :Chromosome),
(gene_regulatoryregions)-[:dataSets]-(gene_regulatoryregions_datasets :DataSet),
(gene_regulatoryregions)-[:OVERLAPS]-(gene_regulatoryregions_overlappingfeatures :SequenceFeature),
(gene_regulatoryregions_overlappingfeatures)-[:chromosomeLocation]-(gene_regulatoryregions_overlappingfeatures_chromosomelocation :Location),
(gene_regulatoryregions_overlappingfeatures)-[:chromosome]-(gene_regulatoryregions_overlappingfeatures_chromosome :Chromosome),
(gene_regulatoryregions_overlappingfeatures)-[:dataSets]-(gene_regulatoryregions_overlappingfeatures_datasets :DataSet)

WHERE ( ANY (key in keys(gene) WHERE gene[key]='CG2328') AND (gene)-[]-(Organism { shortName: 'D. melanogaster' } )) AND gene_regulatoryregions_overlappingfeatures_datasets.name = 'REDfly Drosophila transcription factor binding sites' AND gene_regulatoryregions_datasets.name = 'REDfly Drosophila transcriptional cis-regulatory modules' AND gene_organism.name = 'Drosophila melanogaster'
RETURN gene.primaryIdentifier,
gene.symbol,
gene_regulatoryregions.primaryIdentifier,
gene_regulatoryregions.length,
gene_regulatoryregions_chromosome.primaryIdentifier,
gene_regulatoryregions_chromosomelocation.start,
gene_regulatoryregions_chromosomelocation.end,
gene_regulatoryregions_overlappingfeatures.primaryIdentifier,
gene_regulatoryregions_overlappingfeatures.length,
gene_regulatoryregions_overlappingfeatures_chromosome.primaryIdentifier,
gene_regulatoryregions_overlappingfeatures_chromosomelocation.start,
gene_regulatoryregions_overlappingfeatures_chromosomelocation.end
ORDER BY gene.primaryIdentifier ASC
