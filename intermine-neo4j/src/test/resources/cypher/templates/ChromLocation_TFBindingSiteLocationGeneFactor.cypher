MATCH (tfbindingsite :TFBindingSite),
(tfbindingsite)-[:gene]-(tfbindingsite_gene :Gene),
(tfbindingsite_gene)-[:gene]-(tfbindingsite_gene_regulatoryregions :RegulatoryRegion),
(tfbindingsite_gene_regulatoryregions)-[:dataSets]-(tfbindingsite_gene_regulatoryregions_datasets :DataSet),
(tfbindingsite_gene_regulatoryregions_datasets)-[:dataSets]-(tfbindingsite_gene_regulatoryregions_datasets_datasource :DataSource),
(tfbindingsite)-[:chromosome]-(tfbindingsite_chromosome :Chromosome),
(tfbindingsite)-[:factor]-(tfbindingsite_factor :Gene)
WHERE tfbindingsite_chromosome.primaryIdentifier = '3R' AND tfbindingsite_chromosomelocation.end < 2581753 AND tfbindingsite_chromosomelocation.start > 2576754 AND tfbindingsite_gene_regulatoryregions_datasets.name = 'REDfly Drosophila transcription factor binding sites'
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
