MATCH (gene :Gene),
(gene)-[]-(gene_goannotation :goAnnotation),
(gene_goannotation)-[]-(gene_goannotation_evidence :evidence),
(gene_goannotation)-[]-(gene_goannotation_qualifier :qualifier),
(gene_goannotation)-[]-(gene_goannotation_ontologyterm :ontologyTerm),
(gene_goannotation_ontologyterm)-[]-(gene_goannotation_ontologyterm_namespace :namespace)

WHERE ANY (key in keys(gene) WHERE gene[key]='Notch')
RETURN gene.symbol,
gene.secondaryIdentifier,
gene_goannotation_ontologyterm.identifier,
gene_goannotation_ontologyterm.name,
gene_goannotation_evidence_code.code
ORDER BY gene.secondaryIdentifier ASC
