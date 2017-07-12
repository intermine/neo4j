MATCH (intergenicregion :IntergenicRegion),
(intergenicregion)-[]-(intergenicregion_organism :organism),
(intergenicregion)-[]-(intergenicregion_chromosomelocation :chromosomeLocation),
(intergenicregion_chromosomelocation)-[]-(intergenicregion_chromosomelocation_locatedon :locatedOn),
(intergenicregion)-[]-(intergenicregion_datasets :dataSets),
(intergenicregion_datasets)-[]-(intergenicregion_datasets_datasource :dataSource)

WHERE intergenicregion_organism.shortName = 'D. melanogaster' AND intergenicregion_chromosomelocation_locatedon.primaryIdentifier = '2L'
RETURN intergenicregion_chromosomelocation.start,
intergenicregion_chromosomelocation.end,
intergenicregion_chromosomelocation_locatedon.primaryIdentifier,
intergenicregion.length,
intergenicregion.primaryIdentifier,
intergenicregion_datasets_datasource.name
ORDER BY intergenicregion_chromosomelocation.start ASC
