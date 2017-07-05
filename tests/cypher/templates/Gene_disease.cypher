MATCH (disease :Disease),
(disease)-[]-(disease_genes :genes),
(disease_genes)-[]-(disease_genes_homologues :homologues),
(disease_genes_homologues)-[]-(disease_genes_homologues_datasets :dataSets),
(disease_genes_homologues)-[]-(disease_genes_homologues_homologue :homologue)

WHERE ( ANY (key in keys(disease_genes_homologues_homologue) WHERE disease_genes_homologues_homologue[key]='psn') AND (disease_genes_homologues_homologue)-[]-(Organism { shortName: 'D. melanogaster' } ))
RETURN disease_genes_homologues_homologue.primaryIdentifier,
disease_genes_homologues_homologue.symbol,
disease_genes.primaryIdentifier,
disease_genes.symbol,
disease.identifier,
disease.name,
disease_genes_homologues_datasets.name
ORDER BY disease.identifier ASC
