MATCH (gene :Gene),
(gene)-[]-(gene_goannotation :goAnnotation),
(gene_goannotation)-[]-(gene_goannotation_ontologyterm :ontologyTerm),
(gene_goannotation_ontologyterm)-[]-(gene_goannotation_ontologyterm_parents :parents),
(gene)-[]-(gene_organism :organism)

WHERE gene_goannotation_ontologyterm_parents.name = 'DNA binding' AND gene_organism.name = 'Drosophila melanogaster'
RETURN gene.secondaryIdentifier,
gene.symbol,
gene_goannotation_ontologyterm_parents.name,
gene_goannotation_ontologyterm_parents.identifier,
gene_goannotation_ontologyterm.name,
gene_goannotation_ontologyterm.identifier
ORDER BY gene.secondaryIdentifier ASC
