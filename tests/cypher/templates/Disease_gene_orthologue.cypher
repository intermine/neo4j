MATCH (disease :Disease),
(disease)-[]-(disease_genes :genes),
(disease_genes)-[]-(disease_genes_organism :organism),
(disease_genes)-[]-(disease_genes_homologues :homologues),
(disease_genes_homologues)-[]-(disease_genes_homologues_datasets :dataSets),
(disease_genes_homologues)-[]-(disease_genes_homologues_homologue :homologue),
(disease_genes_homologues_homologue)-[]-(disease_genes_homologues_homologue_organism :organism)

WHERE disease.name CONTAINS 'parkinson' AND disease_genes_homologues_homologue_organism.name = 'Drosophila melanogaster' AND disease_genes_organism.name = 'Homo sapiens' AND NOT disease_genes_homologues_homologue.organism = 'Disease.genes.organism'
RETURN disease.identifier,
disease.name,
disease_genes.symbol,
disease_genes.name,
disease_genes_homologues_homologue.primaryIdentifier,
disease_genes_homologues.type,
disease_genes_homologues_datasets.name,
disease_genes_homologues_homologue_organism.name
ORDER BY disease.identifier ASC
