MATCH (gene :Gene),
(gene)-[]-(gene_cdss :CDSs),
(gene_cdss)-[]-(gene_cdss_ontologyannotations :ontologyAnnotations),
(gene_cdss_ontologyannotations)-[]-(gene_cdss_ontologyannotations_datasets :dataSets),
(gene_cdss_ontologyannotations_datasets)-[]-(gene_cdss_ontologyannotations_datasets_bioentities :bioEntities),
(gene_cdss_ontologyannotations_datasets_bioentities)-[]-(gene_cdss_ontologyannotations_datasets_bioentities_synonyms :synonyms),
(gene_cdss_ontologyannotations_datasets_bioentities)-[]-(gene_cdss_ontologyannotations_datasets_bioentities_crossreferences :crossReferences)

WHERE gene_cdss_ontologyannotations_datasets_bioentities_synonyms.value CONTAINS '5asdsadaf'
RETURN gene_cdss_ontologyannotations_datasets_bioentities_crossreferences.identifier,
gene_cdss_ontologyannotations_datasets_bioentities_synonyms.value
