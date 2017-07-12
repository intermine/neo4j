MATCH (gene :Gene),
(gene)-[]-(gene_goannotation :goAnnotation),
(gene_goannotation)-[]-(gene_goannotation_evidence :evidence),
(gene_goannotation_evidence)-[]-(gene_goannotation_evidence_code :code),
(gene_goannotation)-[]-(gene_goannotation_ontologyterm :ontologyTerm)

WHERE ANY (key in keys(gene) WHERE gene[key]='Notch')
RETURN gene.symbol,
gene.secondaryIdentifier,
gene_goannotation_ontologyterm.identifier,
gene_goannotation_ontologyterm.name,
gene_goannotation_evidence_code.code,
gene_goannotation_ontologyterm.namespace,
gene_goannotation.qualifier
ORDER BY gene.secondaryIdentifier ASC
