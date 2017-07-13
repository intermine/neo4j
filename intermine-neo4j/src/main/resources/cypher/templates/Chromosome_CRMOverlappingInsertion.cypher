MATCH (crm :CRM),
(crm)-[:chromosomeLocation]-(crm_chromosomelocation :Location),
(crm)-[:chromosome]-(crm_chromosome :Chromosome),
(crm)-[:dataSets]-(crm_datasets :DataSet),
(crm_datasets)-[:dataSource]-(crm_datasets_datasource :DataSource),
(crm)-[:OVERLAPS]-(crm_overlappingfeatures :SequenceFeature),
(crm_overlappingfeatures)-[:chromosomeLocation]-(crm_overlappingfeatures_chromosomelocation :Location),
(crm_overlappingfeatures)-[:chromosome]-(crm_overlappingfeatures_chromosome :Chromosome),
(crm_overlappingfeatures)-[:dataSets]-(crm_overlappingfeatures_datasets :DataSet),
(crm_overlappingfeatures_datasets)-[:dataSource]-(crm_overlappingfeatures_datasets_datasource :DataSource)

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
