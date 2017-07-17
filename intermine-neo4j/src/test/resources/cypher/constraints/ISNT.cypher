MATCH (sequencefeature :SequenceFeature),
(sequencefeature)-[:chromosomeLocation]-(sequencefeature_chromosomelocation :Location),
(sequencefeature_chromosomelocation)-[:locatedOn]-(sequencefeature_chromosomelocation_locatedon :BioEntity)

WHERE NOT ANY(x IN labels(sequencefeature) WHERE x IN ['Gene'])
RETURN sequencefeature.primaryIdentifier,
sequencefeature_chromosomelocation_locatedon.primaryIdentifier,
sequencefeature_chromosomelocation.start,
sequencefeature_chromosomelocation.end

