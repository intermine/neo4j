MATCH (crm :CRM),
(crm)-[:chromosomeLocation]-(crm_chromosomelocation :Location),
(crm)-[:chromosome]-(crm_chromosome :Chromosome),
(crm)-[:OVERLAPS]-(crm_overlappingfeatures :SequenceFeature),
(crm_overlappingfeatures)-[:chromosomeLocation]-(crm_overlappingfeatures_chromosomelocation :Location),
(crm_overlappingfeatures)-[:chromosome]-(crm_overlappingfeatures_chromosome :Chromosome)

WHERE crm_chromosome.primaryIdentifier = '2R' AND crm_chromosomelocation.start >= 2000000 AND crm_chromosomelocation.end <= 5000000
RETURN crm.primaryIdentifier,
crm.length,
crm_chromosome.primaryIdentifier,
crm_chromosomelocation.start,
crm_chromosomelocation.end,
crm_overlappingfeatures.primaryIdentifier,
crm_overlappingfeatures.length,
crm_overlappingfeatures_chromosome.primaryIdentifier,
crm_overlappingfeatures_chromosomelocation.start,
crm_overlappingfeatures_chromosomelocation.end
ORDER BY crm.primaryIdentifier ASC
