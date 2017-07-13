MATCH (intergenicregion :IntergenicRegion),
(intergenicregion)-[:PART_OF]-(intergenicregion_organism :Organism),
(intergenicregion)-[:chromosomeLocation]-(intergenicregion_chromosomelocation :Location),
(intergenicregion_chromosomelocation)-[:locatedOn]-(intergenicregion_chromosomelocation_locatedon :BioEntity),
(intergenicregion)-[:dataSets]-(intergenicregion_datasets :DataSet),
(intergenicregion_datasets)-[:dataSource]-(intergenicregion_datasets_datasource :DataSource)

WHERE intergenicregion_organism.shortName = 'D. melanogaster' AND intergenicregion_chromosomelocation_locatedon.primaryIdentifier = '2L'
RETURN intergenicregion_chromosomelocation.start,
intergenicregion_chromosomelocation.end,
intergenicregion_chromosomelocation_locatedon.primaryIdentifier,
intergenicregion.length,
intergenicregion.primaryIdentifier,
intergenicregion_datasets_datasource.name
ORDER BY intergenicregion_chromosomelocation.start ASC
