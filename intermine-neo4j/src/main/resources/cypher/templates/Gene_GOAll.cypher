MATCH (gene :Gene),
(gene)-[]-(gene_goannotation :goAnnotation),
(gene_goannotation)-[]-(gene_goannotation_ontologyterm :ontologyTerm),
(gene_goannotation_ontologyterm)-[]-(gene_goannotation_ontologyterm_parents :parents)

WHERE ( ANY (key in keys(gene) WHERE gene[key]='CG11348') AND (gene)-[]-(Organism { shortName: 'D. melanogaster' } ))
RETURN gene.secondaryIdentifier,
gene.symbol,
gene_goannotation_ontologyterm.name,
gene_goannotation_ontologyterm_parents.name,
gene_goannotation.qualifier
ORDER BY gene.secondaryIdentifier ASC
