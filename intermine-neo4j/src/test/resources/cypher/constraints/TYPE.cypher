MATCH (gene :Gene),
(gene)-[:ANNOTATED_BY]-(gene_goannotation :GOAnnotation),
(gene_goannotation)-[:ontologyTerm]-(gene_goannotation_ontologyterm :OntologyTerm),
(gene_goannotation_ontologyterm)-[:parents]-(gene_goannotation_ontologyterm_parents :OntologyTerm),
(gene)-[:PART_OF]-(gene_organism :Organism)

WHERE gene_goannotation_ontologyterm_parents.name = 'DNA binding' AND gene_organism.name = 'Drosophila melanogaster'
RETURN gene.secondaryIdentifier,
gene.symbol,
gene_goannotation_ontologyterm_parents.name,
gene_goannotation_ontologyterm_parents.identifier,
gene_goannotation_ontologyterm.name,
gene_goannotation_ontologyterm.identifier
ORDER BY gene.secondaryIdentifier ASC
