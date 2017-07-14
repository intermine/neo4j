MATCH (gene :Gene),
(gene)-[:ANNOTATED_BY]-(gene_goannotation :GOAnnotation),
(gene_goannotation)-[:ontologyTerm]-(gene_goannotation_ontologyterm :OntologyTerm),
(gene)-[:PART_OF]-(gene_organism :Organism),
(gene)-[:homologues]-(gene_homologues :Homologue),
(gene_homologues)-[:PARTNER_OF]-(gene_homologues_homologue :Gene),
(gene_homologues_homologue)-[:PART_OF]-(gene_homologues_homologue_organism :Organism)

WHERE gene_organism.name = 'Drosophila melanogaster' AND gene_homologues_homologue_organism.name = 'Homo sapiens' AND gene_homologues.type = 'orthologue'
RETURN gene.secondaryIdentifier,
gene.symbol,
gene_homologues_homologue.primaryIdentifier,
gene_homologues_homologue.symbol,
gene_goannotation_ontologyterm.identifier,
gene_goannotation_ontologyterm.name
ORDER BY gene.secondaryIdentifier ASC
