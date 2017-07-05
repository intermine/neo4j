MATCH (protein :Protein),
(protein)-[]-(protein_primaryaccession :primaryAccession),
(protein)-[]-(protein_genes :genes),
(protein_genes)-[]-(protein_genes_goannotation :goAnnotation),
(protein_genes_goannotation)-[]-(protein_genes_goannotation_ontologyterm :ontologyTerm),
(protein_genes_goannotation_ontologyterm)-[]-(protein_genes_goannotation_ontologyterm_namespace :namespace)

WHERE protein.primaryAccession = 'P04755' AND protein_genes_goannotation_ontologyterm.namespace = 'cellular_component'
RETURN protein_genes.symbol,
protein_genes_goannotation_ontologyterm.identifier,
protein_genes_goannotation_ontologyterm.name
ORDER BY protein.primaryAccession ASC
