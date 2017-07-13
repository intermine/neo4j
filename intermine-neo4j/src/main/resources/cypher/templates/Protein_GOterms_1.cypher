MATCH (protein :Protein),
(protein)-[:genes]-(protein_genes :Gene),
(protein_genes)-[:ANNOTATED_BY]-(protein_genes_goannotation :GOAnnotation),
(protein_genes_goannotation)-[:ontologyTerm]-(protein_genes_goannotation_ontologyterm :OntologyTerm)

WHERE protein.primaryAccession = 'P04755' AND protein_genes_goannotation_ontologyterm.namespace = 'cellular_component'
RETURN protein.primaryAccession,
protein_genes.symbol,
protein_genes_goannotation_ontologyterm.identifier,
protein_genes_goannotation_ontologyterm.name,
protein_genes_goannotation_ontologyterm.namespace
ORDER BY protein.primaryAccession ASC
