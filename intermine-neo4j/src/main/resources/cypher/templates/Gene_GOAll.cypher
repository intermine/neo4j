MATCH (gene :Gene),
(gene)-[:ANNOTATED_BY]-(gene_goannotation :GOAnnotation),
(gene_goannotation)-[:ontologyTerm]-(gene_goannotation_ontologyterm :OntologyTerm),
(gene_goannotation_ontologyterm)-[:parents]-(gene_goannotation_ontologyterm_parents :OntologyTerm)

WHERE ( ANY (key in keys(gene) WHERE gene[key]='CG11348') AND (gene)-[]-(Organism { shortName: 'D. melanogaster' } ))
RETURN gene.secondaryIdentifier,
gene.symbol,
gene_goannotation_ontologyterm.name,
gene_goannotation_ontologyterm_parents.name,
gene_goannotation.qualifier
ORDER BY gene.secondaryIdentifier ASC
