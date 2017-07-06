MATCH (gene :Gene),
(gene)-[]-(gene_goannotation :goAnnotation),
(gene_goannotation)-[]-(gene_goannotation_ontologyterm :ontologyTerm),
(gene_goannotation_ontologyterm)-[]-(gene_goannotation_ontologyterm_parents :parents),
(gene)-[]-(gene_organism :organism),
(gene)-[]-(gene_homologues :homologues),
(gene_homologues)-[]-(gene_homologues_datasets :dataSets),
(gene_homologues)-[]-(gene_homologues_homologue :homologue),
(gene_homologues_homologue)-[]-(gene_homologues_homologue_organism :organism)

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
