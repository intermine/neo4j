MATCH (gene :Gene),
(gene)-[:ANNOTATED_BY]-(gene_goannotation :GOAnnotation),
(gene_goannotation)-[:ontologyTerm]-(gene_goannotation_ontologyterm :OntologyTerm)

WHERE gene_goannotation_ontologyterm.identifier = 'GO:0008270'
RETURN gene.id,
gene.primaryIdentifier,
gene_goannotation_ontologyterm.identifier,
gene_goannotation_ontologyterm.name
ORDER BY gene.id ASC
