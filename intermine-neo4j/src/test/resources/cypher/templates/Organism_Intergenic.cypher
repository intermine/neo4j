MATCH (intergenicregion :IntergenicRegion),
(intergenicregion)-[:PART_OF]-(intergenicregion_organism :Organism),
(intergenicregion)-[:chromosome]-(intergenicregion_chromosome :Chromosome),
(intergenicregion)-[:ADJACENT_TO]-(intergenicregion_adjacentgenes :Gene),
(intergenicregion)-[:dataSets]-(intergenicregion_datasets :DataSet),
(intergenicregion_datasets)-[:dataSets]-(intergenicregion_datasets_datasource :DataSource)
WHERE intergenicregion_organism.name = 'Drosophila melanogaster'
RETURN intergenicregion.primaryIdentifier,
intergenicregion.length,
intergenicregion_chromosome.primaryIdentifier,
intergenicregion_chromosomelocation.start,
intergenicregion_chromosomelocation.end,
intergenicregion_adjacentgenes.primaryIdentifier,
intergenicregion_datasets_datasource.name
ORDER BY intergenicregion.primaryIdentifier ASC
