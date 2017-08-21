MATCH (est :EST),
(est)-[:dataSets]-(est_datasets :DataSet),
(est_datasets)-[:dataSets]-(est_datasets_datasource :DataSource),
(est)-[:overlappingESTSets]-(est_overlappingestsets :OverlappingESTSet),
(est_overlappingestsets)-[:chromosome]-(est_overlappingestsets_chromosome :Chromosome)
WHERE ANY (key in keys(est) WHERE est[key]='BX617645.1 ')
RETURN est.primaryIdentifier,
est_overlappingestsets.primaryIdentifier,
est_overlappingestsets_chromosome.primaryIdentifier,
est_overlappingestsets_chromosomelocation.start,
est_overlappingestsets_chromosomelocation.end,
est_overlappingestsets_chromosomelocation.strand,
est_datasets_datasource.name

