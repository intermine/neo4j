MATCH (protein :Protein),
(protein)-[:genes]-(protein_genes :Gene),
(protein_genes)-[:ANNOTATED_BY]-(protein_genes_goannotation :GOAnnotation)
OPTIONAL MATCH (protein_genes_goannotation)-[:ontologyTerm]-(protein_genes_goannotation_ontologyterm :OntologyTerm)
WHERE ( ANY (key in keys(protein) WHERE protein[key]='P09089') AND (protein)-[]-(Organism { shortName: 'D. melanogaster' } ))
RETURN protein.primaryAccession,
protein_genes.secondaryIdentifier,
protein_genes.symbol,
protein_genes_goannotation_ontologyterm.namespace,
protein_genes_goannotation_ontologyterm.identifier,
protein_genes_goannotation_ontologyterm.name
ORDER BY protein.primaryAccession ASC
