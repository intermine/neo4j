MATCH (regulatoryregion :RegulatoryRegion),
(regulatoryregion)-[:chromosomeLocation]-(regulatoryregion_chromosomelocation :Location),
(regulatoryregion)-[:chromosome]-(regulatoryregion_chromosome :Chromosome),
(regulatoryregion)-[:dataSets]-(regulatoryregion_datasets :DataSet)

WHERE regulatoryregion_chromosome.primaryIdentifier = '3R' AND regulatoryregion_chromosomelocation.end <= 2920000 AND regulatoryregion_chromosomelocation.start >= 2490000
RETURN regulatoryregion_chromosome.primaryIdentifier,
regulatoryregion_chromosomelocation.start,
regulatoryregion_chromosomelocation.end,
regulatoryregion.primaryIdentifier,
regulatoryregion_datasets.name
ORDER BY regulatoryregion_chromosome.primaryIdentifier ASC
