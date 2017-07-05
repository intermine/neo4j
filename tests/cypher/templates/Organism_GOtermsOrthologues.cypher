MATCH (gene :Gene),
(gene)-[]-(gene_goannotation :goAnnotation),
(gene_goannotation)-[]-(gene_goannotation_ontologyterm :ontologyTerm),
(gene)-[]-(gene_organism :organism),
(gene)-[]-(gene_homologues :homologues),
(gene_homologues)-[]-(gene_homologues_homologue :homologue),
(gene_homologues_homologue)-[]-(gene_homologues_homologue_organism :organism)

WHERE gene_organism.name = 'Drosophila melanogaster' AND gene_homologues_homologue_organism.name = 'Homo sapiens' AND gene_homologues.type = 'orthologue'
RETURN gene.secondaryIdentifier,
gene.symbol,
gene_homologues_homologue.primaryIdentifier,
gene_homologues_homologue.symbol,
gene_goannotation_ontologyterm.identifier,
gene_goannotation_ontologyterm.name
ORDER BY gene.secondaryIdentifier ASC
