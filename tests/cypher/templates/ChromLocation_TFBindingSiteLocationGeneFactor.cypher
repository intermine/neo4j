MATCH (tfbindingsite :TFBindingSite),
(tfbindingsite)-[]-(tfbindingsite_gene :gene),
(tfbindingsite_gene)-[]-(tfbindingsite_gene_regulatoryregions :regulatoryRegions),
(tfbindingsite_gene_regulatoryregions)-[]-(tfbindingsite_gene_regulatoryregions_datasets :dataSets),
(tfbindingsite_gene_regulatoryregions_datasets)-[]-(tfbindingsite_gene_regulatoryregions_datasets_datasource :dataSource),
(tfbindingsite)-[]-(tfbindingsite_chromosomelocation :chromosomeLocation),
(tfbindingsite)-[]-(tfbindingsite_chromosome :chromosome),
(tfbindingsite)-[]-(tfbindingsite_factor :factor)

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
