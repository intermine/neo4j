MATCH (intergenicregion :IntergenicRegion),
(intergenicregion)-[]-(intergenicregion_organism :organism),
(intergenicregion)-[]-(intergenicregion_chromosomelocation :chromosomeLocation),
(intergenicregion)-[]-(intergenicregion_chromosome :chromosome),
(intergenicregion)-[]-(intergenicregion_adjacentgenes :adjacentGenes),
(intergenicregion)-[]-(intergenicregion_datasets :dataSets),
(intergenicregion_datasets)-[]-(intergenicregion_datasets_datasource :dataSource)

WHERE intergenicregion_organism.name = 'Drosophila melanogaster'
RETURN intergenicregion.primaryIdentifier,
intergenicregion.length,
intergenicregion_chromosome.primaryIdentifier,
intergenicregion_chromosomelocation.start,
intergenicregion_chromosomelocation.end,
intergenicregion_adjacentgenes.primaryIdentifier,
intergenicregion_datasets_datasource.name
ORDER BY intergenicregion.primaryIdentifier ASC
