MATCH (gene :Gene),
(gene)-[]-(gene_goannotation :goAnnotation),
(gene_goannotation)-[]-(gene_goannotation_evidence :evidence),
(gene_goannotation_evidence)-[]-(gene_goannotation_evidence_with :with),
(gene_goannotation)-[]-(gene_goannotation_ontologyterm :ontologyTerm),
(gene)-[]-(gene_organism :organism)

WHERE gene_goannotation_ontologyterm.name = 'protein binding' AND gene_organism.name = 'Drosophila melanogaster'
RETURN gene.primaryIdentifier,
gene.symbol,
gene_goannotation_ontologyterm.identifier,
gene_goannotation_ontologyterm.name,
gene_goannotation_evidence_code.code,
gene_goannotation_evidence_with.primaryIdentifier
ORDER BY gene.primaryIdentifier ASC
