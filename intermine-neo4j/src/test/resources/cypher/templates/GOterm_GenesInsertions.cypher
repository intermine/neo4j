MATCH (gene :Gene),
(gene)-[:ANNOTATED_BY]-(gene_goannotation :GOAnnotation),
(gene_goannotation)-[:ANNOTATED_BY]-(gene_goannotation_ontologyterm :OntologyTerm),
(gene)-[:PART_OF]-(gene_organism :Organism),
(gene)-[:OVERLAPS]-(gene_overlappingfeatures :SequenceFeature),
(gene_overlappingfeatures)-[:dataSets]-(gene_overlappingfeatures_datasets :DataSet),
(gene_overlappingfeatures_datasets)-[:dataSets]-(gene_overlappingfeatures_datasets_datasource :DataSource)
WHERE gene_organism.name = 'Drosophila melanogaster' AND gene_goannotation_ontologyterm.name = 'neuropeptide receptor activity'
RETURN gene.primaryIdentifier,
gene.symbol,
gene_overlappingfeatures.primaryIdentifier,
gene_overlappingfeatures.symbol,
gene_overlappingfeatures_datasets_datasource.name,
gene_goannotation_ontologyterm.identifier,
gene_goannotation_ontologyterm.name
ORDER BY gene.primaryIdentifier ASC
