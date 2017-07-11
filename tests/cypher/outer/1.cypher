MATCH (protein :Protein),
(protein)-[]-(protein_genes :genes),
(protein_genes)-[]-(protein_genes_diseases :diseases),
(protein_genes_diseases)-[]-(protein_genes_diseases_publications :publications),
(protein_genes_diseases_publications)-[]-(protein_genes_diseases_publications_pubmedid :pubMedId)
OPTIONAL MATCH (protein_genes)-[]-(protein_genes_chromosome :chromosome),
(protein_genes_chromosome)-[]-(protein_genes_chromosome_organism :organism),
(protein_genes_chromosome_organism)-[]-(protein_genes_chromosome_organism_commonname :commonName)

RETURN protein_genes_chromosome.primaryIdentifier,
protein.primaryIdentifier,
protein_genes.primaryIdentifier,
protein_genes_diseases.identifier
ORDER BY protein.primaryIdentifier ASC
