MATCH (gene :Gene),
(gene)-[:PART_OF]-(gene_organism :Organism),
(gene)-[:regulatoryRegions]-(gene_regulatoryregions :RegulatoryRegion),
(gene_regulatoryregions)-[:sequence]-(gene_regulatoryregions_sequence :Sequence),
(gene_regulatoryregions)-[:HAS_TERM]-(gene_regulatoryregions_sequenceontologyterm :SOTerm),
(gene_regulatoryregions)-[:chromosomeLocation]-(gene_regulatoryregions_chromosomelocation :Location),
(gene_regulatoryregions)-[:chromosome]-(gene_regulatoryregions_chromosome :Chromosome),
(gene_regulatoryregions)-[:dataSets]-(gene_regulatoryregions_datasets :DataSet),
(gene_regulatoryregions_datasets)-[:dataSource]-(gene_regulatoryregions_datasets_datasource :DataSource)

WHERE ANY (key in keys(gene) WHERE gene[key]='dpp') AND gene_organism.name = 'Drosophila melanogaster'
RETURN gene.primaryIdentifier,
gene.symbol,
gene_regulatoryregions.primaryIdentifier,
gene_regulatoryregions_chromosome.primaryIdentifier,
gene_regulatoryregions_sequenceontologyterm.name,
gene_regulatoryregions_chromosomelocation.end,
gene_regulatoryregions_chromosomelocation.start,
gene_regulatoryregions_datasets_datasource.name,
gene_regulatoryregions_sequence.residues
ORDER BY gene_regulatoryregions.primaryIdentifier ASC
