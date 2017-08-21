MATCH (gene :Gene),
(gene)-[:ANNOTATED_BY]-(gene_goannotation :GOAnnotation),
(gene_goannotation)-[:evidence]-(gene_goannotation_evidence :GOEvidence),
(gene_goannotation_evidence)-[:code]-(gene_goannotation_evidence_code :GOEvidenceCode),
(gene_goannotation)-[:ANNOTATED_BY]-(gene_goannotation_ontologyterm :OntologyTerm)
WHERE ANY (key in keys(gene) WHERE gene[key]='CG11348')
RETURN gene.primaryIdentifier,
gene.secondaryIdentifier,
gene.symbol,
gene_goannotation_ontologyterm.identifier,
gene_goannotation_ontologyterm.name,
gene_goannotation_evidence_code.code,
gene_goannotation_ontologyterm.namespace,
gene_goannotation.qualifier
ORDER BY gene.primaryIdentifier ASC
