MATCH (disease :Disease),
(disease)-[:genes]-(disease_genes :Gene)

WHERE disease.name CONTAINS 'parkinson'
RETURN disease.identifier,
disease.name,
disease_genes.primaryIdentifier,
disease_genes.symbol,
disease_genes.name
ORDER BY disease.identifier ASC
