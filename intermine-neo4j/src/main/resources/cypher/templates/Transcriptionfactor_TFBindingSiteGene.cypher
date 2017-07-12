MATCH (tfbindingsite :TFBindingSite),
(tfbindingsite)-[]-(tfbindingsite_chromosomelocation :chromosomeLocation),
(tfbindingsite_chromosomelocation)-[]-(tfbindingsite_chromosomelocation_locatedon :locatedOn),
(tfbindingsite)-[]-(tfbindingsite_gene :gene),
(tfbindingsite)-[]-(tfbindingsite_datasets :dataSets),
(tfbindingsite)-[]-(tfbindingsite_factor :factor)
OPTIONAL MATCH (tfbindingsite)-[]-(tfbindingsite_sequence :sequence)
WHERE ( ANY (key in keys(tfbindingsite_factor) WHERE tfbindingsite_factor[key]='E(spl)') AND (tfbindingsite_factor)-[]-(Organism { shortName: 'D. melanogaster' } )) AND tfbindingsite_datasets.name = 'REDfly Drosophila transcription factor binding sites'
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
