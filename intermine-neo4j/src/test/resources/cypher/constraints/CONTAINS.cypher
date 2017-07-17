MATCH (gene :Gene),
(gene)-[:CDSs]-(gene_cdss :CDS),
(gene_cdss)-[:ANNOTATED_BY]-(gene_cdss_ontologyannotations :OntologyAnnotation),
(gene_cdss_ontologyannotations)-[:dataSets]-(gene_cdss_ontologyannotations_datasets :DataSet),
(gene_cdss_ontologyannotations_datasets)-[:bioEntities]-(gene_cdss_ontologyannotations_datasets_bioentities :BioEntity),
(gene_cdss_ontologyannotations_datasets_bioentities)-[:SYNONYM_OF]-(gene_cdss_ontologyannotations_datasets_bioentities_synonyms :Synonym),
(gene_cdss_ontologyannotations_datasets_bioentities)-[:CROSS_REFERENCED_BY]-(gene_cdss_ontologyannotations_datasets_bioentities_crossreferences :CrossReference)

WHERE gene_cdss_ontologyannotations_datasets_bioentities_synonyms.value CONTAINS '5asdsadaf'
RETURN gene_cdss_ontologyannotations_datasets_bioentities_crossreferences.identifier,
gene_cdss_ontologyannotations_datasets_bioentities_synonyms.value

