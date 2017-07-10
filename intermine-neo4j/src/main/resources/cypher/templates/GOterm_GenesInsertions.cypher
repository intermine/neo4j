MATCH (gene :Gene),
(gene)-[]-(gene_goannotation :goAnnotation),
(gene_goannotation)-[]-(gene_goannotation_ontologyterm :ontologyTerm),
(gene)-[]-(gene_organism :organism),
(gene)-[]-(gene_overlappingfeatures :overlappingFeatures),
(gene_overlappingfeatures)-[]-(gene_overlappingfeatures_datasets :dataSets),
(gene_overlappingfeatures_datasets)-[]-(gene_overlappingfeatures_datasets_datasource :dataSource)

WHERE gene_organism.name = 'Drosophila melanogaster' AND gene_goannotation_ontologyterm.name = 'neuropeptide receptor activity'
RETURN gene.secondaryIdentifier,
gene.symbol,
gene_overlappingfeatures.primaryIdentifier,
gene_overlappingfeatures.symbol,
gene_overlappingfeatures_datasets_datasource.name,
gene_goannotation_ontologyterm.identifier,
gene_goannotation_ontologyterm.name
ORDER BY gene.secondaryIdentifier ASC
