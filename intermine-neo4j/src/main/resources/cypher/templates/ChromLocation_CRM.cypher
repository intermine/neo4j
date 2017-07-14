MATCH (crm :CRM),
(crm)-[:chromosomeLocation]-(crm_chromosomelocation :Location),
(crm)-[:chromosome]-(crm_chromosome :Chromosome),
(crm)-[:MENTIONED_IN]-(crm_publications :Publication)

WHERE crm_chromosome.primaryIdentifier = '3L' AND crm_chromosomelocation.start >= 1500000 AND crm_chromosomelocation.end <= 10000000
RETURN crm_chromosome.primaryIdentifier,
crm_chromosomelocation.start,
crm_chromosomelocation.end,
crm.primaryIdentifier,
crm.length,
crm.evidenceMethod,
crm_publications.pubMedId
ORDER BY crm_chromosome.primaryIdentifier ASC
