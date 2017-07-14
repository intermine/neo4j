MATCH (disease :Disease),
(disease)-[:genes]-(disease_genes :Gene),
(disease_genes)-[:PART_OF]-(disease_genes_organism :Organism),
(disease_genes)-[:homologues]-(disease_genes_homologues :Homologue),
(disease_genes_homologues)-[:dataSets]-(disease_genes_homologues_datasets :DataSet),
(disease_genes_homologues)-[:PARTNER_OF]-(disease_genes_homologues_homologue :Gene),
(disease_genes_homologues_homologue)-[:PART_OF]-(disease_genes_homologues_homologue_organism :Organism)

WHERE disease.name CONTAINS 'parkinson' AND disease_genes_homologues_homologue_organism.name = 'Drosophila melanogaster' AND disease_genes_organism.name = 'Homo sapiens' AND disease_genes_homologues_homologue_organism != disease_genes_organism
RETURN disease.identifier,
disease.name,
disease_genes.symbol,
disease_genes.name,
disease_genes_homologues_homologue.primaryIdentifier,
disease_genes_homologues.type,
disease_genes_homologues_datasets.name,
disease_genes_homologues_homologue_organism.name
ORDER BY disease.identifier ASC
