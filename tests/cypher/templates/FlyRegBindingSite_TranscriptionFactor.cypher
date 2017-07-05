MATCH (tfbindingsite :TFBindingSite),
(tfbindingsite)-[]-(tfbindingsite_sequence :sequence),
(tfbindingsite_sequence)-[]-(tfbindingsite_sequence_residues :residues),
(tfbindingsite)-[]-(tfbindingsite_gene :gene),
(tfbindingsite)-[]-(tfbindingsite_chromosomelocation :chromosomeLocation),
(tfbindingsite)-[]-(tfbindingsite_chromosome :chromosome),
(tfbindingsite)-[]-(tfbindingsite_datasets :dataSets),
(tfbindingsite_datasets)-[]-(tfbindingsite_datasets_datasource :dataSource),
(tfbindingsite)-[]-(tfbindingsite_factor :factor),
(tfbindingsite)-[]-(tfbindingsite_publications :publications),
(tfbindingsite_publications)-[]-(tfbindingsite_publications_pubmedid :pubMedId)

WHERE tfbindingsite_datasets.name = 'REDfly Drosophila transcription factor binding sites' AND tfbindingsite.primaryIdentifier = 'TF000031'
RETURN tfbindingsite.primaryIdentifier,
tfbindingsite.length,
tfbindingsite_gene.primaryIdentifier,
tfbindingsite_factor.primaryIdentifier,
tfbindingsite_chromosome.primaryIdentifier,
tfbindingsite_chromosomelocation.start,
tfbindingsite_chromosomelocation.end,
tfbindingsite_datasets_datasource.name
ORDER BY tfbindingsite.primaryIdentifier ASC
