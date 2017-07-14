MATCH (gene :Gene),
(gene)-[:ANNOTATED_BY]-(gene_goannotation :GOAnnotation),
(gene_goannotation)-[:ontologyTerm]-(gene_goannotation_ontologyterm :OntologyTerm),
(gene)-[:PART_OF]-(gene_organism :Organism)

WHERE gene_organism.name = 'Drosophila melanogaster'
RETURN gene.secondaryIdentifier,
gene.symbol,
gene_goannotation_ontologyterm.identifier,
gene_goannotation_ontologyterm.name,
gene_goannotation_ontologyterm.description
ORDER BY gene.secondaryIdentifier ASC
