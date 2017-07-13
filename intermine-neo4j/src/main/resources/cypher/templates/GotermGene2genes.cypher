MATCH (gene :Gene),
(gene)-[:ANNOTATED_BY]-(gene_goannotation :GOAnnotation),
(gene_goannotation)-[:EVIDENCED_BY]-(gene_goannotation_evidence :GOEvidence),
(gene_goannotation_evidence)-[:code]-(gene_goannotation_evidence_code :GOEvidenceCode),
(gene_goannotation)-[:ontologyTerm]-(gene_goannotation_ontologyterm :OntologyTerm),
(gene)-[:PART_OF]-(gene_organism :Organism)
OPTIONAL MATCH (gene_goannotation_evidence)-[:with]-(gene_goannotation_evidence_with :BioEntity)
WHERE gene_goannotation_evidence_code.code = 'IPI' AND gene_goannotation_ontologyterm.name = 'protein binding' AND gene_organism.name = 'Drosophila melanogaster' AND ( ANY (key in keys(gene) WHERE gene[key]='notch') AND (gene)-[]-(Organism { shortName: 'D. melanogaster' } )) AND gene_goannotation_ontologyterm.namespace = 'cellular_component'
RETURN gene.primaryIdentifier,
gene.symbol,
gene_goannotation_ontologyterm.identifier,
gene_goannotation_ontologyterm.name,
gene_goannotation_evidence_code.code,
gene_goannotation_evidence_with.primaryIdentifier
ORDER BY gene.primaryIdentifier ASC
