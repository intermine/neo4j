MATCH (est :EST),
(est)-[:dataSets]-(est_datasets :DataSet),
(est_datasets)-[:dataSets]-(est_datasets_datasource :DataSource),
(est)-[:overlappingESTSets]-(est_overlappingestsets :OverlappingESTSet),
(est_overlappingestsets)-[:chromosome]-(est_overlappingestsets_chromosome :Chromosome),
(est_overlappingestsets)-[:OVERLAPS]-(est_overlappingestsets_overlappingfeatures :SequenceFeature)
WHERE ( ANY (key in keys(est) WHERE est[key]='BX617645.1') AND (est)-[]-(Organism { shortName: 'A. gambiae' } ))
RETURN est.primaryIdentifier,
est_overlappingestsets.primaryIdentifier,
est_overlappingestsets_chromosome.primaryIdentifier,
est_overlappingestsets_chromosomelocation.start,
est_overlappingestsets_chromosomelocation.end,
est_overlappingestsets_chromosomelocation.strand,
est_overlappingestsets_overlappingfeatures.primaryIdentifier,
est_datasets_datasource.name

