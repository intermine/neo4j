MATCH (protein :Protein),
(protein)-[:genes]-(protein_genes :Gene),
(protein_genes_chromosome)-[:PART_OF]-(protein_genes_chromosome_organism :Organism),
(protein_genes)-[:diseases]-(protein_genes_diseases :Disease),
(protein_genes_diseases)-[:MENTIONED_IN]-(protein_genes_diseases_publications :Publication)
OPTIONAL MATCH (protein_genes)-[:chromosome]-(protein_genes_chromosome :Chromosome)

RETURN protein_genes_chromosome.primaryIdentifier,
protein.primaryIdentifier,
protein_genes.primaryIdentifier,
protein_genes_diseases.identifier,
protein_genes_diseases_publications.pubMedId,
protein_genes_chromosome_organism.commonName
ORDER BY protein.primaryIdentifier ASC
