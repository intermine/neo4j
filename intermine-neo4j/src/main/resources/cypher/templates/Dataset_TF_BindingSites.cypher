MATCH (tfbindingsite :TFBindingSite),
(tfbindingsite)-[:chromosomeLocation]-(tfbindingsite_chromosomelocation :Location),
(tfbindingsite_chromosomelocation)-[:locatedOn]-(tfbindingsite_chromosomelocation_locatedon :BioEntity),
(tfbindingsite)-[:gene]-(tfbindingsite_gene :Gene),
(tfbindingsite)-[:dataSets]-(tfbindingsite_datasets :DataSet),
(tfbindingsite)-[:factor]-(tfbindingsite_factor :Gene)
OPTIONAL MATCH (tfbindingsite)-[:sequence]-(tfbindingsite_sequence :Sequence)
WHERE tfbindingsite_datasets.name = 'REDfly Drosophila transcription factor binding sites'
RETURN tfbindingsite_factor.primaryIdentifier,
tfbindingsite_factor.symbol,
tfbindingsite.secondaryIdentifier,
tfbindingsite_chromosomelocation_locatedon.primaryIdentifier,
tfbindingsite_chromosomelocation.start,
tfbindingsite_chromosomelocation.end,
tfbindingsite_gene.secondaryIdentifier,
tfbindingsite_gene.symbol,
tfbindingsite_sequence.residues,
tfbindingsite_datasets.name
ORDER BY tfbindingsite_factor.primaryIdentifier ASC
