MATCH (regulatoryregion :RegulatoryRegion),
(regulatoryregion)-[]-(regulatoryregion_chromosomelocation :chromosomeLocation),
(regulatoryregion)-[]-(regulatoryregion_chromosome :chromosome),
(regulatoryregion)-[]-(regulatoryregion_datasets :dataSets)

WHERE regulatoryregion_chromosome.primaryIdentifier = '3R' AND regulatoryregion_chromosomelocation.end <= 29200000 AND regulatoryregion_chromosomelocation.start >= 24900000
RETURN regulatoryregion_chromosome.primaryIdentifier,
regulatoryregion_chromosomelocation.start,
regulatoryregion_chromosomelocation.end,
regulatoryregion.primaryIdentifier,
regulatoryregion_datasets.name
ORDER BY regulatoryregion_chromosome.primaryIdentifier ASC
