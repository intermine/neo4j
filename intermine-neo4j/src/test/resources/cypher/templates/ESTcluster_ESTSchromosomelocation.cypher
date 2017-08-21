MATCH (overlappingestset :OverlappingESTSet),
(overlappingestset)-[:chromosome]-(overlappingestset_chromosome :Chromosome),
(overlappingestset)-[:dataSets]-(overlappingestset_datasets :DataSet),
(overlappingestset_datasets)-[:dataSets]-(overlappingestset_datasets_datasource :DataSource),
(overlappingestset)-[:ESTs]-(overlappingestset_ests :EST)
WHERE overlappingestset.primaryIdentifier = 'NCLAG186007'
RETURN overlappingestset.primaryIdentifier,
overlappingestset_ests.primaryIdentifier,
overlappingestset_chromosome.primaryIdentifier,
overlappingestset_chromosomelocation.start,
overlappingestset_chromosomelocation.end,
overlappingestset_chromosomelocation.strand,
overlappingestset_datasets_datasource.name
ORDER BY overlappingestset.primaryIdentifier ASC
