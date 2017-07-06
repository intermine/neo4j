MATCH (gene :Gene),
(gene)-[]-(gene_goannotation :goAnnotation),
(gene_goannotation)-[]-(gene_goannotation_ontologyterm :ontologyTerm),
(gene)-[]-(gene_organism :organism),
(gene)-[]-(gene_overlappingfeatures :overlappingFeatures),
(gene_overlappingfeatures)-[]-(gene_overlappingfeatures_available :available)

WHERE gene_organism.name = 'Drosophila melanogaster' AND gene_goannotation_ontologyterm.name = 'gap junction'
RETURN gene.secondaryIdentifier,
gene.symbol,
gene_goannotation_ontologyterm.identifier,
gene_goannotation_ontologyterm.name,
gene_overlappingfeatures.secondaryIdentifier
ORDER BY gene.secondaryIdentifier ASC
