MATCH (protein :Protein),
(protein)-[]-(protein_genes :genes),
(protein_genes)-[]-(protein_genes_goannotation :goAnnotation),
(protein_genes_goannotation)-[]-(protein_genes_goannotation_ontologyterm :ontologyTerm)

WHERE protein.primaryAccession = 'P04755' AND protein_genes_goannotation_ontologyterm.namespace = 'cellular_component'
RETURN protein.primaryAccession,
protein_genes.symbol,
protein_genes_goannotation_ontologyterm.identifier,
protein_genes_goannotation_ontologyterm.name,
protein_genes_goannotation_ontologyterm.namespace
ORDER BY protein.primaryAccession ASC
