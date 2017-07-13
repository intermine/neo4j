MATCH (gene :Gene),
(gene)-[:ANNOTATED_BY]-(gene_goannotation :GOAnnotation),
(gene_goannotation)-[:ontologyTerm]-(gene_goannotation_ontologyterm :OntologyTerm),
(gene)-[:PART_OF]-(gene_organism :Organism),
(gene)-[:OVERLAPS]-(gene_overlappingfeatures :SequenceFeature),
(gene_overlappingfeatures)-[:dataSets]-(gene_overlappingfeatures_datasets :DataSet),
(gene_overlappingfeatures_datasets)-[:dataSource]-(gene_overlappingfeatures_datasets_datasource :DataSource)

WHERE gene_organism.name = 'Drosophila melanogaster' AND gene_goannotation_ontologyterm.name = '*ion channel*'
RETURN gene.secondaryIdentifier,
gene.symbol,
gene_overlappingfeatures.primaryIdentifier,
gene_overlappingfeatures_datasets_datasource.name,
gene_goannotation_ontologyterm.identifier,
gene_goannotation_ontologyterm.name
ORDER BY gene.secondaryIdentifier ASC
