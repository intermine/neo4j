MATCH (gene :Gene),
(gene)-[]-(gene_organism :organism),
(gene)-[]-(gene_regulatoryregions :regulatoryRegions),
(gene_regulatoryregions)-[]-(gene_regulatoryregions_sequence :sequence),
(gene_regulatoryregions_sequence)-[]-(gene_regulatoryregions_sequence_residues :residues),
(gene_regulatoryregions)-[]-(gene_regulatoryregions_chromosomelocation :chromosomeLocation),
(gene_regulatoryregions)-[]-(gene_regulatoryregions_chromosome :chromosome),
(gene_regulatoryregions)-[]-(gene_regulatoryregions_datasets :dataSets),
(gene_regulatoryregions_datasets)-[]-(gene_regulatoryregions_datasets_datasource :dataSource),
(gene_regulatoryregions)-[]-(gene_regulatoryregions_factor :factor)

WHERE ANY (key in keys(gene) WHERE gene[key]='dpp') AND gene_organism.name = 'Drosophila melanogaster'
RETURN gene.primaryIdentifier,
gene.symbol,
gene_regulatoryregions.primaryIdentifier,
gene_regulatoryregions_chromosome.primaryIdentifier,
gene_regulatoryregions_chromosomelocation.end,
gene_regulatoryregions_chromosomelocation.start,
gene_regulatoryregions_datasets_datasource.name,
gene_regulatoryregions_factor.primaryIdentifier,
gene_regulatoryregions_factor.symbol
ORDER BY gene_regulatoryregions.primaryIdentifier ASC
