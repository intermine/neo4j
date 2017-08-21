MATCH (pointmutation :PointMutation),
(pointmutation)-[:chromosome]-(pointmutation_chromosome :Chromosome),
(pointmutation)-[:dataSets]-(pointmutation_datasets :DataSet),
(pointmutation_datasets)-[:dataSets]-(pointmutation_datasets_datasource :DataSource),
(pointmutation)-[:OVERLAPS]-(pointmutation_overlappingfeatures :SequenceFeature)
WHERE pointmutation.primaryIdentifier = 'Eip93F[1]'
RETURN pointmutation.primaryIdentifier,
pointmutation_chromosome.primaryIdentifier,
pointmutation_chromosomelocation.start,
pointmutation_chromosomelocation.end,
pointmutation_chromosomelocation.strand,
pointmutation_overlappingfeatures.primaryIdentifier,
pointmutation_overlappingfeatures.symbol,
pointmutation_overlappingfeatures_chromosomelocation.start,
pointmutation_overlappingfeatures_chromosomelocation.end,
pointmutation_overlappingfeatures_chromosomelocation.strand,
pointmutation_datasets_datasource.name
ORDER BY pointmutation.primaryIdentifier ASC
