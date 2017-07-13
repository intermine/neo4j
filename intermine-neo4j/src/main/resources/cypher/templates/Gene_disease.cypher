MATCH (disease :Disease),
(disease)-[:genes]-(disease_genes :Gene),
(disease_genes)-[:homologues]-(disease_genes_homologues :Homologue),
(disease_genes_homologues)-[:dataSets]-(disease_genes_homologues_datasets :DataSet),
(disease_genes_homologues)-[:PARTNER_OF]-(disease_genes_homologues_homologue :Gene)

WHERE ( ANY (key in keys(disease_genes_homologues_homologue) WHERE disease_genes_homologues_homologue[key]='psn') AND (disease_genes_homologues_homologue)-[]-(Organism { shortName: 'D. melanogaster' } ))
RETURN disease_genes_homologues_homologue.primaryIdentifier,
disease_genes_homologues_homologue.symbol,
disease_genes.primaryIdentifier,
disease_genes.symbol,
disease.identifier,
disease.name,
disease_genes_homologues_datasets.name
ORDER BY disease.identifier ASC
