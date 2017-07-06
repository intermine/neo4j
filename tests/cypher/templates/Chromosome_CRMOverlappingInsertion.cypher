MATCH (crm :CRM),
(crm)-[]-(crm_chromosomelocation :chromosomeLocation),
(crm)-[]-(crm_chromosome :chromosome),
(crm)-[]-(crm_datasets :dataSets),
(crm_datasets)-[]-(crm_datasets_datasource :dataSource),
(crm)-[]-(crm_overlappingfeatures :overlappingFeatures),
(crm_overlappingfeatures)-[]-(crm_overlappingfeatures_chromosomelocation :chromosomeLocation),
(crm_overlappingfeatures)-[]-(crm_overlappingfeatures_chromosome :chromosome),
(crm_overlappingfeatures)-[]-(crm_overlappingfeatures_datasets :dataSets),
(crm_overlappingfeatures_datasets)-[]-(crm_overlappingfeatures_datasets_datasource :dataSource)

WHERE crm_chromosome.primaryIdentifier = '2L'
RETURN crm.primaryIdentifier,
crm_chromosome.primaryIdentifier,
crm_chromosomelocation.start,
crm_chromosomelocation.end,
crm_datasets_datasource.name,
crm_overlappingfeatures.primaryIdentifier,
crm_overlappingfeatures_chromosome.primaryIdentifier,
crm_overlappingfeatures_chromosomelocation.start,
crm_overlappingfeatures_chromosomelocation.end,
crm_overlappingfeatures_datasets_datasource.name
ORDER BY crm.primaryIdentifier ASC
