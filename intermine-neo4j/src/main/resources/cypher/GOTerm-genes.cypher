MATCH (gene :Gene),
(gene)-[]-(gene_goannotation :goAnnotation),
(gene_goannotation)-[]-(gene_goannotation_ontologyterm :ontologyTerm)

WHERE gene_goannotation_ontologyterm.identifier = 'GO:0008270'
RETURN gene.id,
gene.primaryIdentifier,
gene_goannotation_ontologyterm.identifier,
gene_goannotation_ontologyterm.name
ORDER BY gene.id ASC
