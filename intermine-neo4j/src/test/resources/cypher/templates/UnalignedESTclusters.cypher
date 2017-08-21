MATCH (overlappingestset :OverlappingESTSet),
(overlappingestset)-[:PART_OF]-(overlappingestset_organism :Organism),
(overlappingestset)-[:dataSets]-(overlappingestset_datasets :DataSet),
(overlappingestset_datasets)-[:dataSets]-(overlappingestset_datasets_datasource :DataSource)
WHERE overlappingestset_organism.name = 'Anopheles gambiae' AND overlappingestset.primaryIdentifier = 'UCLAG*'
RETURN overlappingestset.primaryIdentifier,
overlappingestset_datasets_datasource.name
ORDER BY overlappingestset.primaryIdentifier ASC
