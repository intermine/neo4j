MATCH (tfbindingsite :TFBindingSite),
(tfbindingsite)-[:chromosomeLocation]-(tfbindingsite_chromosomelocation :Location),
(tfbindingsite)-[:gene]-(tfbindingsite_gene :Gene),
(tfbindingsite_gene)-[:regulatoryRegions]-(tfbindingsite_gene_regulatoryregions :RegulatoryRegion),
(tfbindingsite_gene_regulatoryregions)-[:dataSets]-(tfbindingsite_gene_regulatoryregions_datasets :DataSet),
(tfbindingsite_gene_regulatoryregions_datasets)-[:dataSource]-(tfbindingsite_gene_regulatoryregions_datasets_datasource :DataSource),
(tfbindingsite)-[:chromosome]-(tfbindingsite_chromosome :Chromosome),
(tfbindingsite)-[:factor]-(tfbindingsite_factor :Gene)

WHERE tfbindingsite_chromosome.primaryIdentifier = '3R' AND tfbindingsite_chromosomelocation.end < 10000000 AND tfbindingsite_chromosomelocation.start > 9000000 AND tfbindingsite_gene_regulatoryregions_datasets.name = 'REDfly Drosophila transcription factor binding sites'
RETURN tfbindingsite_chromosome.primaryIdentifier,
tfbindingsite_chromosomelocation.start,
tfbindingsite_chromosomelocation.end,
tfbindingsite.primaryIdentifier,
tfbindingsite_gene.primaryIdentifier,
tfbindingsite_gene.name,
tfbindingsite_factor.primaryIdentifier,
tfbindingsite_factor.name,
tfbindingsite_gene_regulatoryregions_datasets_datasource.name
ORDER BY tfbindingsite_chromosome.primaryIdentifier ASC
