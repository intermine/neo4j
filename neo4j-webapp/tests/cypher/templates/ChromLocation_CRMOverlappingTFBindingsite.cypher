MATCH (crm :CRM),
(crm)-[]-(crm_chromosomelocation :chromosomeLocation),
(crm)-[]-(crm_chromosome :chromosome),
(crm)-[]-(crm_overlappingfeatures :overlappingFeatures),
(crm_overlappingfeatures)-[]-(crm_overlappingfeatures_chromosomelocation :chromosomeLocation),
(crm_overlappingfeatures)-[]-(crm_overlappingfeatures_chromosome :chromosome)

WHERE crm_chromosome.primaryIdentifier = '2R' AND crm_chromosomelocation.start >= 9000000 AND crm_chromosomelocation.end <= 10000000
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
