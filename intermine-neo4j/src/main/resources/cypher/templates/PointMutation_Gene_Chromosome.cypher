MATCH (pointmutation :PointMutation),
(pointmutation)-[]-(pointmutation_chromosomelocation :chromosomeLocation),
(pointmutation)-[]-(pointmutation_chromosome :chromosome),
(pointmutation)-[]-(pointmutation_datasets :dataSets),
(pointmutation_datasets)-[]-(pointmutation_datasets_datasource :dataSource),
(pointmutation)-[]-(pointmutation_overlappingfeatures :overlappingFeatures),
(pointmutation_overlappingfeatures)-[]-(pointmutation_overlappingfeatures_chromosomelocation :chromosomeLocation)

WHERE pointmutation.primaryIdentifier = 'trp[14]-2'
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
