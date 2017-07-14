MATCH (gene :Gene),
(gene)-[:ANNOTATED_BY]-(gene_goannotation :GOAnnotation),
(gene_goannotation)-[:ontologyTerm]-(gene_goannotation_ontologyterm :OntologyTerm),
(gene_goannotation_ontologyterm)-[:parents]-(gene_goannotation_ontologyterm_parents :OntologyTerm),
(gene)-[:PART_OF]-(gene_organism :Organism),
(gene)-[:homologues]-(gene_homologues :Homologue),
(gene_homologues)-[:dataSets]-(gene_homologues_datasets :DataSet),
(gene_homologues)-[:PARTNER_OF]-(gene_homologues_homologue :Gene),
(gene_homologues_homologue)-[:PART_OF]-(gene_homologues_homologue_organism :Organism)

WHERE gene_goannotation_ontologyterm_parents.name = 'DNA binding' AND gene_organism.name = 'Drosophila melanogaster' AND gene_homologues_homologue_organism.name = 'Caenorhabditis elegans' AND gene_homologues.type = 'orthologue'
RETURN gene.secondaryIdentifier,
gene.symbol,
gene_goannotation_ontologyterm_parents.name,
gene_goannotation_ontologyterm_parents.identifier,
gene_goannotation_ontologyterm.name,
gene_goannotation_ontologyterm.identifier,
gene_homologues_homologue.primaryIdentifier,
gene_homologues_homologue.secondaryIdentifier,
gene_homologues_homologue.symbol,
gene_homologues.type,
gene_homologues_datasets.name
ORDER BY gene.secondaryIdentifier ASC
