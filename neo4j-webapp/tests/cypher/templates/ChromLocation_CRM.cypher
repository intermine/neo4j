MATCH (crm :CRM),
(crm)-[]-(crm_chromosomelocation :chromosomeLocation),
(crm)-[]-(crm_chromosome :chromosome),
(crm)-[]-(crm_publications :publications),
(crm_publications)-[]-(crm_publications_pubmedid :pubMedId)

WHERE crm_chromosome.primaryIdentifier = '3L' AND crm_chromosomelocation.start >= 1500000 AND crm_chromosomelocation.end <= 10000000
RETURN crm_chromosome.primaryIdentifier,
crm_chromosomelocation.start,
crm_chromosomelocation.end,
crm.primaryIdentifier,
crm.length
ORDER BY crm_chromosome.primaryIdentifier ASC
