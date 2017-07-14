MATCH (tfbindingsite :TFBindingSite),
(tfbindingsite)-[:sequence]-(tfbindingsite_sequence :Sequence),
(tfbindingsite)-[:gene]-(tfbindingsite_gene :Gene),
(tfbindingsite)-[:chromosomeLocation]-(tfbindingsite_chromosomelocation :Location),
(tfbindingsite)-[:chromosome]-(tfbindingsite_chromosome :Chromosome),
(tfbindingsite)-[:dataSets]-(tfbindingsite_datasets :DataSet),
(tfbindingsite_datasets)-[:dataSource]-(tfbindingsite_datasets_datasource :DataSource),
(tfbindingsite)-[:factor]-(tfbindingsite_factor :Gene),
(tfbindingsite)-[:MENTIONED_IN]-(tfbindingsite_publications :Publication)

WHERE tfbindingsite_datasets.name = 'REDfly Drosophila transcription factor binding sites' AND tfbindingsite.primaryIdentifier = 'TF000031'
RETURN tfbindingsite.primaryIdentifier,
tfbindingsite.length,
tfbindingsite_gene.primaryIdentifier,
tfbindingsite_factor.primaryIdentifier,
tfbindingsite_chromosome.primaryIdentifier,
tfbindingsite_chromosomelocation.start,
tfbindingsite_chromosomelocation.end,
tfbindingsite_sequence.residues,
tfbindingsite_publications.pubMedId,
tfbindingsite_datasets_datasource.name
ORDER BY tfbindingsite.primaryIdentifier ASC
